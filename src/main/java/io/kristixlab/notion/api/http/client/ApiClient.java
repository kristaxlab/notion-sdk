package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.request.ApiPath;
import java.util.Map;

/**
 * High-level typed client interface responsible for URL building, body serialization, request
 * dispatch, and response deserialization.
 *
 * <p>Implementations handle:
 *
 * <ul>
 *   <li>Merging {@code baseUrl} + {@link ApiPath} path/query params into a final request URL
 *   <li>Serializing the request body (JSON or multipart/form-data)
 *   <li>Delegating to an underlying {@link HttpClient} pipeline
 *   <li>Deserializing the response body into a typed object via {@link
 *       io.kristixlab.notion.api.json.JsonConverter}
 * </ul>
 *
 * <p>All cross-cutting concerns (authentication, rate-limiting, logging, error mapping) are
 * supposed to be handled by the decorators and interceptors in the underlying {@link HttpClient}
 * pipeline injected into the implementation — not by this interface.
 *
 * <p>Use {@link ApiClientImpl} as the standard implementation, or provide a test double that
 * implements this interface directly (e.g. {@code ApiClientStub}).
 *
 * @see ApiClientImpl
 */
public interface ApiClient {

  /**
   * Sends a bodyless request and deserializes the response.
   *
   * @param method the HTTP method string (case-insensitive), e.g. {@code "GET"}
   * @param apiPath url template with optional path and query parameters
   * @param responseType the class to deserialize the response body into
   * @param <T> the response type
   * @return the deserialized response body
   */
  <T> T call(String method, ApiPath apiPath, Class<T> responseType);

  /**
   * Sends a request with an optional body and deserializes the response.
   *
   * <p>A {@code null} body produces no request entity (suitable for GET / DELETE).
   *
   * @param method the HTTP method string (case-insensitive), e.g. {@code "POST"}
   * @param apiPath url template with optional path and query parameters
   * @param body the request payload; may be {@code null}
   * @param responseType the class to deserialize the response body into
   * @param <T> the response type
   * @return the deserialized response body
   */
  <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType);

  /**
   * Sends a request with extra HTTP headers and deserializes the response.
   *
   * <p>The {@code extraHeaders} map is merged into the request on top of any headers already added
   * by the pipeline. A non-{@code null} value in {@code extraHeaders} takes precedence over
   * pipeline-injected values for the same header name.
   *
   * @param method the HTTP method string (case-insensitive)
   * @param apiPath url template with optional path and query parameters
   * @param extraHeaders additional headers to include in the request; may be {@code null}
   * @param body the request payload; may be {@code null}
   * @param responseType the class to deserialize the response body into
   * @param <T> the response type
   * @return the deserialized response body
   */
  <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType);
}
