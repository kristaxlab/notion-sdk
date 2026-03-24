package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.client.HttpClient;
import io.kristixlab.notion.api.http.client.HttpClient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Library-agnostic logging interceptor for the {@link HttpClient} pipeline.
 *
 * <p>Logs request method/URL/body-type and response status/body at {@code DEBUG} level via SLF4J.
 * Non-2xx responses are additionally logged at {@code WARN} level.
 *
 * <p>This interceptor is intentionally simple — it does <b>not</b> depend on OkHttp, {@code
 * ExchangeContext}, or {@code ExchangeLogger}. File-based exchange logging can be added as a
 * separate interceptor when needed.
 */
public class LoggingHttpInterceptor implements HttpClientInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHttpInterceptor.class);

  private final String apiName;

  /**
   * @param apiName human-readable API name used in log messages (e.g. {@code "Notion"})
   */
  public LoggingHttpInterceptor(String apiName) {
    this.apiName = apiName;
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "[{}] >> {} {} | body: {}",
          apiName,
          request.method(),
          request.url(),
          describeBody(request.body()));
    }
    return request;
  }

  @Override
  public void afterReceive(HttpRequest request, HttpResponse response) {
    int status = response.statusCode();

    if (LOGGER.isDebugEnabled()) {
      String body = response.bodyAsString();
      LOGGER.debug(
          "[{}] << {} {} | status: {} | body: {}",
          apiName,
          request.method(),
          request.url(),
          status,
          body != null ? truncate(body, 1024) : "[empty]");
    }

    if (status >= 400) {
      LOGGER.warn(
          "[{}] {} {} responded with status {}", apiName, request.method(), request.url(), status);
    }
  }

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  private static String describeBody(Body body) {
    if (body == null) return "none";
    if (body instanceof EmptyBody) return "empty";
    if (body instanceof StringBody sb)
      return sb.contentType() + " (" + sb.content().length() + " chars)";
    if (body instanceof BytesBody bb)
      return bb.contentType() + " (" + bb.bytes().length + " bytes)";
    if (body instanceof FileBody fb)
      return fb.contentType() + " (file: " + fb.file().getName() + ")";
    if (body instanceof InputStreamBody isb)
      return isb.contentType() + " (stream, " + isb.contentLength() + " bytes)";
    if (body instanceof MultipartBody mb) return "multipart (" + mb.parts().size() + " parts)";
    return body.getClass().getSimpleName();
  }

  private static String truncate(String s, int maxLength) {
    return s.length() <= maxLength ? s : s.substring(0, maxLength) + "...[truncated]";
  }
}
