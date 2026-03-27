package io.kristixlab.notion.api.http.interceptor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.http.client.HttpClient.Body;
import io.kristixlab.notion.api.http.client.HttpClient.BytesBody;
import io.kristixlab.notion.api.http.client.HttpClient.EmptyBody;
import io.kristixlab.notion.api.http.client.HttpClient.FileBody;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.client.HttpClient.InputStreamBody;
import io.kristixlab.notion.api.http.client.HttpClient.MultipartBody;
import io.kristixlab.notion.api.http.client.HttpClient.StringBody;
import io.kristixlab.notion.api.json.JsonConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Builder;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor that writes each HTTP exchange as two pretty-printed JSON files into a
 * caller-supplied directory:
 *
 * <ul>
 *   <li>{@code <epoch_ms>_<thread_id>_rq.json} — written in {@link #beforeSend}
 *   <li>{@code <epoch_ms>_<thread_id>_rs.json} — written in {@link #afterReceive}
 * </ul>
 *
 * <p>The two files share the same {@code <epoch_ms>_<thread_id>} prefix so they can always be
 * associated. The prefix is generated once in {@code beforeSend} and carried to {@code
 * afterReceive} via a {@link ThreadLocal}.
 *
 * <p>The target directory is created eagerly at construction time. Write failures are logged at
 * {@code WARN} level and never propagate to the caller.
 *
 * <p><b>Typical test usage:</b>
 *
 * <pre>{@code
 * Path dir = Paths.get("exchanges", testClass, testMethod);
 * NotionClient notion = NotionClient.builder()
 *     .auth(token)
 *     .recordExchanges(dir)
 *     .build();
 * }</pre>
 *
 * @see RequestRecord
 * @see ResponseRecord
 */
public class ExchangeRecordingInterceptor implements HttpClientInterceptor {


  /** Immutable snapshot of the request side of an HTTP exchange. */
  @Value
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class RequestRecord {

    @JsonProperty("method")
    String method;

    @JsonProperty("url")
    String url;

    /** {@code Authorization} header value is always replaced with {@code "[redacted]"}. */
    @JsonProperty("request_headers")
    Map<String, String> requestHeaders;

    /**
     * JSON bodies are stored as a parsed object; binary/multipart bodies as a short string
     * descriptor.
     */
    @JsonProperty("request_body")
    Object requestBody;
  }

  /** Immutable snapshot of the response side of an HTTP exchange. */
  @Value
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ResponseRecord {

    @JsonProperty("status_code")
    Integer statusCode;

    @JsonProperty("response_headers")
    Map<String, List<String>> responseHeaders;

    /** Parsed as a JSON object when the response body is valid JSON, raw string otherwise. */
    @JsonProperty("response_body")
    Object responseBody;
  }


  private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRecordingInterceptor.class);

  private final Path dir;

  /** Carries the base filename prefix from {@link #beforeSend} to {@link #afterReceive}. */
  private final ThreadLocal<String> pendingBaseName = new ThreadLocal<>();

  /**
   * @param dir directory where exchange files are written; created eagerly if it does not exist
   */
  public ExchangeRecordingInterceptor(Path dir) {
    this.dir = Objects.requireNonNull(dir, "dir");
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      LOGGER.warn("Could not create exchange log directory: {}", dir, e);
    }
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    String baseName =
        String.format(
            "%d_%d_%s",
            System.currentTimeMillis(),
            Thread.currentThread().getId(),
            serviceNameFrom(request.url(), request.method().name()));
    pendingBaseName.set(baseName);

    RequestRecord record =
        RequestRecord.builder()
            .method(request.method().name())
            .url(request.url())
            .requestHeaders(redactedHeaders(request.headers()))
            .requestBody(describeBody(request.body()))
            .build();

    write(baseName + "_rq.json", record);
    return request;
  }

  @Override
  public void afterReceive(HttpRequest request, HttpResponse response) {
    String baseName = pendingBaseName.get();
    pendingBaseName.remove();

    if (baseName == null) {
      baseName = String.format("%d_%d", System.currentTimeMillis(), Thread.currentThread().getId());
    }

    ResponseRecord record =
        ResponseRecord.builder()
            .statusCode(response.statusCode())
            .responseHeaders(response.headers().isEmpty() ? null : response.headers())
            .responseBody(JsonConverter.getInstance().parseJson(response.bodyAsString()))
            .build();

    write(baseName + "_rs.json", record);
  }


  private void write(String fileName, Object record) {
    Path file = dir.resolve(fileName);
    try {
      Files.writeString(file, JsonConverter.getInstance().toJson(record, true));
    } catch (IOException e) {
      LOGGER.warn("Failed to write exchange log to: {}", file, e);
    }
  }

  private static Map<String, String> redactedHeaders(Map<String, String> headers) {
    if (headers == null || headers.isEmpty()) {
      return null;
    }
    Map<String, String> copy = new LinkedHashMap<>(headers);
    copy.computeIfPresent("Authorization", (k, v) -> "[redacted]");
    return copy;
  }

  private static Object describeBody(Body body) {
    if (body == null || body instanceof EmptyBody) {
      return null;
    }
    if (body instanceof StringBody sb) {
      return JsonConverter.getInstance().parseJson(sb.content());
    }
    if (body instanceof BytesBody bb) {
      return "[bytes: " + bb.bytes().length + "]";
    }
    if (body instanceof MultipartBody mb) {
      return "[multipart: " + mb.parts().size() + " parts]";
    }
    if (body instanceof FileBody fb) {
      return "[file: " + fb.file().getName() + "]";
    }
    if (body instanceof InputStreamBody isb) {
      return "[stream: " + isb.contentLength() + " bytes]";
    }
    return "[body]";
  }

  /**
   * Derives a human-readable service name from the request URL and method.
   *
   * <p>Strategy: parse the URL path, strip the {@code /v1} prefix, discard UUID-looking segments
   * (the dynamic ID values that replaced {@code {param}} placeholders), join the remaining segments
   * with {@code _}, then append a method suffix.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>{@code GET /v1/pages} → {@code pages_retrieve}
   *   <li>{@code GET /v1/blocks/abc-123/children} → {@code blocks_children_retrieve}
   *   <li>{@code POST /v1/databases/abc-123/query} → {@code databases_query_create}
   *   <li>{@code PATCH /v1/pages/abc-123} → {@code pages_update}
   *   <li>{@code DELETE /v1/blocks/abc-123} → {@code blocks_delete}
   * </ul>
   */
  static String serviceNameFrom(String url, String method) {
    try {
      String path = java.net.URI.create(url).getPath();
      if (path.startsWith("/v1")) {
        path = path.substring(3);
      }

      StringBuilder name = new StringBuilder();
      for (String segment : path.split("/")) {
        if (segment.isEmpty() || isLikelyId(segment)) {
          continue;
        }
        if (name.length() > 0) {
          name.append('_');
        }
        name.append(segment);
      }

      String suffix =
          switch (method) {
            case "GET" -> "_retrieve";
            case "POST" -> "_create";
            case "PATCH" -> "_update";
            case "DELETE" -> "_delete";
            default -> "";
          };

      return name.append(suffix).toString();
    } catch (Exception e) {
      return "unknown";
    }
  }

  /**
   * Returns {@code true} when {@code segment} looks like a Notion resource ID — a UUID with hyphens
   * or a compact 32-character hex string — rather than a path keyword like {@code pages}.
   */
  private static boolean isLikelyId(String segment) {
    return segment.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
        || segment.matches("[0-9a-f]{32}");
  }
}
