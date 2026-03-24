package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.client.HttpClient;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.client.InterceptingHttpClient;

/**
 * Callback interface for observing and modifying HTTP exchanges in the {@link HttpClient} pipeline.
 *
 * <p>Interceptors are invoked in registration order by {@link InterceptingHttpClient}:
 *
 * <ol>
 *   <li>{@link #beforeSend} — called before the request is sent. May return a <em>modified</em>
 *       request (e.g. with extra headers). If an interceptor throws, the request is never sent and
 *       subsequent interceptors are skipped.
 *   <li>{@link #afterReceive} — called after the response is received. Useful for logging and
 *       metrics. Receives both the (possibly modified) request and the response.
 * </ol>
 *
 * <p>Both methods default to no-ops so implementations only need to override the callbacks they
 * care about.
 *
 * @see InterceptingHttpClient
 */
public interface HttpClientInterceptor {

  /**
   * Called before the request is sent. May return a modified request.
   *
   * <p>Use {@link HttpRequest#toBuilder()} to create a modified copy:
   *
   * <pre>{@code
   * @Override
   * public HttpRequest beforeSend(HttpRequest request) {
   *     return request.toBuilder()
   *         .header("X-Custom", "value")
   *         .build();
   * }
   * }</pre>
   *
   * @param request the request about to be sent
   * @return the (possibly modified) request to actually send
   */
  default HttpRequest beforeSend(HttpRequest request) {
    return request;
  }

  /**
   * Called after the response is received (regardless of status code).
   *
   * @param request the request that was sent (after all {@code beforeSend} modifications)
   * @param response the response received from the server
   */
  default void afterReceive(HttpRequest request, HttpResponse response) {}
}
