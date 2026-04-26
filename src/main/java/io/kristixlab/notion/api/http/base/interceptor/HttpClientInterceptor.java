package io.kristixlab.notion.api.http.base.interceptor;

import io.kristixlab.notion.api.http.base.client.HttpClient;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.base.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.base.client.InterceptingHttpClient;

/**
 * Callback for observing and modifying HTTP exchanges in the {@link HttpClient} pipeline.
 *
 * <p>Invoked in registration order by {@link InterceptingHttpClient}. Both methods default to
 * no-ops so implementations only override the hooks they need.
 *
 * @see InterceptingHttpClient
 */
public interface HttpClientInterceptor {

  /**
   * Called before the request is sent. Return a modified copy (via {@link HttpRequest#toBuilder()})
   * or the original request.
   */
  default HttpRequest beforeSend(HttpRequest request) {
    return request;
  }

  /** Called after the response is received, regardless of status code. */
  default void afterReceive(HttpRequest request, HttpResponse response) {}
}
