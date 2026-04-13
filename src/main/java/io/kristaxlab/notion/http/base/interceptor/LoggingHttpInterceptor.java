package io.kristaxlab.notion.http.base.interceptor;

import io.kristaxlab.notion.http.base.client.HttpClient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs request method/URL/body-type and response status at {@code DEBUG} level via SLF4J. Non-2xx
 * responses are additionally logged at {@code WARN}.
 */
public class LoggingHttpInterceptor implements HttpClientInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHttpInterceptor.class);

  private final String apiName;

  /**
   * @param apiName label used in log messages (e.g. {@code "Notion"})
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
          body != null ? truncate(body, null) : "[empty]");
    }

    if (status >= 400) {
      LOGGER.warn(
          "[{}] {} {} responded with status {}", apiName, request.method(), request.url(), status);
    }
  }

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

  private static String truncate(String s, Integer maxLength) {
    if (maxLength == null || s.length() <= maxLength) {
      return s;
    }
    return s.length() <= maxLength ? s : s.substring(0, maxLength) + "...[truncated]";
  }
}
