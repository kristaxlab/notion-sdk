package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.request.ApiPath;
import java.util.Map;

/**
 * High-level typed client for URL building, body serialization, request dispatch, and response
 * deserialization.
 *
 * <p>Cross-cutting concerns (authentication, rate-limiting, logging, error mapping) are handled by
 * the {@link HttpClient} pipeline injected into the implementation — not by this interface.
 *
 * @see ApiClientImpl
 */
public interface ApiClient {

  /** Sends a bodyless request and deserializes the response. */
  <T> T call(String method, ApiPath apiPath, Class<T> responseType);

  /**
   * Sends a request with a body and deserializes the response. A {@code null} body produces no
   * request entity.
   */
  <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType);

  /**
   * Sends a request with extra headers and a body, then deserializes the response.
   *
   * <p>Values in {@code extraHeaders} take precedence over pipeline-injected headers with the same
   * name.
   *
   * @param method HTTP method (case-insensitive)
   * @param apiPath path template with optional path/query parameters
   * @param extraHeaders additional request headers; may be {@code null}
   * @param body request payload; may be {@code null}
   * @param responseType target deserialization class
   */
  <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType);
}
