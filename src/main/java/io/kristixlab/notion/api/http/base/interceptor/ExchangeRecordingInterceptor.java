package io.kristixlab.notion.api.http.base.interceptor;

import io.kristixlab.notion.api.http.base.client.HttpClient.Body;
import io.kristixlab.notion.api.http.base.client.HttpClient.BytesBody;
import io.kristixlab.notion.api.http.base.client.HttpClient.EmptyBody;
import io.kristixlab.notion.api.http.base.client.HttpClient.FileBody;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.base.client.HttpClient.InputStreamBody;
import io.kristixlab.notion.api.http.base.client.HttpClient.MultipartBody;
import io.kristixlab.notion.api.http.base.client.HttpClient.StringBody;
import io.kristixlab.notion.api.http.base.json.JsonSerializer;
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
 * Writes each HTTP exchange as a pair of JSON files ({@code *_rq.json} / {@code *_rs.json}) into a
 * caller-supplied directory.
 *
 * <p>Both files share a {@code <epoch_ms>_<thread_id>_<service>} prefix generated in {@link
 * #beforeSend} and carried to {@link #afterReceive} via a {@link ThreadLocal}. The target directory
 * is created eagerly; write failures are logged at WARN level and never propagate.
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
   * @param dir directory for exchange files; created eagerly if absent
   * @param serializer serializer used to write exchange files
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

  private void write(String fileName, Object record) {
    Path file = dir.resolve(fileName);
    try {
      Files.writeString(file, serializer.toJson(record));
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
   * Derives a human-readable service name from the request URL and HTTP method.
   *
   * <p>Strips the {@code /v1} prefix, drops UUID-looking segments, and appends a method suffix
   * (e.g. {@code GET /v1/blocks/abc-123/children} → {@code blocks_children_retrieve}).
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

  /** Returns {@code true} when the segment looks like a UUID (hyphenated or compact 32-hex). */
  private static boolean isLikelyId(String segment) {
    return UUID_WITH_HYPHENS_PATTERN.matcher(segment).matches()
        || UUID_COMPACT_PATTERN.matcher(segment).matches();
  }

  /** Recorded HTTP request (written to {@code *_rq.json}). */
  @Builder
  public record RequestRecord(
      String method, String url, Map<String, String> requestHeaders, Object requestBody) {}

  /** Recorded HTTP response (written to {@code *_rs.json}). */
  @Builder
  public record ResponseRecord(
      Integer statusCode, Map<String, List<String>> responseHeaders, Object responseBody) {}
}
