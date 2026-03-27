package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.client.HttpClient.Body;
import io.kristixlab.notion.api.http.client.HttpClient.BytesBody;
import io.kristixlab.notion.api.http.client.HttpClient.EmptyBody;
import io.kristixlab.notion.api.http.client.HttpClient.FileBody;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.client.HttpClient.InputStreamBody;
import io.kristixlab.notion.api.http.client.HttpClient.MultipartBody;
import io.kristixlab.notion.api.http.client.HttpClient.StringBody;
import io.kristixlab.notion.api.json.JsonSerializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.Builder;
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

  private static final Pattern UUID_WITH_HYPHENS_PATTERN =
      Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
  private static final Pattern UUID_COMPACT_PATTERN = Pattern.compile("[0-9a-f]{32}");

  private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRecordingInterceptor.class);

  private final Path dir;
  private final JsonSerializer serializer;

  /** Carries the base filename prefix from {@link #beforeSend} to {@link #afterReceive}. */
  private final ThreadLocal<String> pendingBaseName = new ThreadLocal<>();

  /**
   * Constructs an ExchangeRecordingInterceptor with a custom JsonSerializer.
   *
   * @param dir directory where exchange files are written; created eagerly if it does not exist
   * @param serializer the serializer used to write exchange files
   */
  public ExchangeRecordingInterceptor(Path dir, JsonSerializer serializer) {
    this.dir = Objects.requireNonNull(dir, "dir");
    this.serializer = Objects.requireNonNull(serializer, "json");
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      LOGGER.warn("Could not create exchange log directory: {}", dir, e);
    }
  }

  /**
   * Intercepts the HTTP request before it is sent, records the request details, and writes them to
   * a JSON file.
   *
   * @param request the HTTP request to be sent
   * @return the (possibly modified) HTTP request
   */
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
            .requestBody(describeBody(request.body(), serializer))
            .build();

    write(baseName + "_rq.json", record);
    return request;
  }

  /**
   * Intercepts the HTTP response after it is received, records the response details, and writes
   * them to a JSON file.
   *
   * @param request the original HTTP request
   * @param response the HTTP response received
   */
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
            .responseBody(serializer.toObject(response.bodyAsString(), Object.class))
            .build();

    write(baseName + "_rs.json", record);
  }

  /**
   * Writes the given record object as a pretty-printed JSON file in the configured directory.
   *
   * @param fileName the name of the file to write
   * @param record the record object to serialize
   */
  private void write(String fileName, Object record) {
    Path file = dir.resolve(fileName);
    try {
      Files.writeString(file, serializer.toJson(record));
    } catch (IOException e) {
      LOGGER.warn("Failed to write exchange log to: {}", file, e);
    }
  }

  /**
   * Returns a copy of the headers map with sensitive values (e.g., Authorization) redacted.
   *
   * @param headers the original headers map
   * @return a copy of the headers map with sensitive values redacted, or null if headers are empty
   */
  private static Map<String, String> redactedHeaders(Map<String, String> headers) {
    if (headers == null || headers.isEmpty()) {
      return null;
    }
    Map<String, String> copy = new LinkedHashMap<>(headers);
    copy.computeIfPresent("Authorization", (k, v) -> "[redacted]");
    return copy;
  }

  /**
   * Describes the HTTP request or response body for logging purposes.
   *
   * @param body the HTTP body
   * @param serializer the JSON serializer
   * @return a parsed JSON object, or a string description for non-JSON bodies
   */
  private static Object describeBody(Body body, JsonSerializer serializer) {
    if (body == null || body instanceof EmptyBody) {
      return null;
    }
    if (body instanceof StringBody sb) {
      return serializer.toObject(sb.content(), Object.class);
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
   * <p>Strategy: parse the URL path, strip the /v1 prefix, discard UUID-looking segments (the
   * dynamic ID values that replaced {param} placeholders), join the remaining segments with _, then
   * append a method suffix.
   *
   * @param url the request URL
   * @param method the HTTP method
   * @return a human-readable service name for the exchange log
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
  /**
   * Returns true when the segment looks like a Notion resource ID — a UUID with hyphens or a
   * compact 32-character hex string — rather than a path keyword like pages.
   *
   * @param segment the path segment to check
   * @return true if the segment is likely a Notion resource ID
   */
  private static boolean isLikelyId(String segment) {
    return UUID_WITH_HYPHENS_PATTERN.matcher(segment).matches()
        || UUID_COMPACT_PATTERN.matcher(segment).matches();
  }

  /**
   * Record representing an HTTP request for exchange logging.
   *
   * @param method HTTP method (e.g., GET, POST)
   * @param url Request URL
   * @param requestHeaders Map of request headers (with sensitive values redacted)
   * @param requestBody Request body, parsed or described as appropriate
   */
  @Builder
  public record RequestRecord(
      String method, String url, Map<String, String> requestHeaders, Object requestBody) {}

  /**
   * Record representing an HTTP response for exchange logging.
   *
   * @param statusCode HTTP status code
   * @param responseHeaders Map of response headers
   * @param responseBody Response body, parsed as JSON if possible
   */
  @Builder
  public record ResponseRecord(
      Integer statusCode, Map<String, List<String>> responseHeaders, Object responseBody) {}
}
