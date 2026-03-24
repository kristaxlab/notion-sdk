package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.interceptor.HttpClientInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * A decorator that wraps any {@link HttpClient} and runs an ordered list of {@link
 * HttpClientInterceptor}s before and after each request.
 *
 * <p>Execution flow:
 *
 * <pre>
 *   for each interceptor  →  request = interceptor.beforeSend(request)
 *   response = delegate.send(request)
 *   for each interceptor  →  interceptor.afterReceive(request, response)
 *   return response
 * </pre>
 *
 * <p>If any {@code beforeSend} throws, the request is <em>not</em> sent and the exception
 * propagates immediately. If {@code delegate.send()} throws {@link IOException}, {@code
 * afterReceive} is <em>not</em> called (there is no response to observe).
 *
 * <p>Usage:
 *
 * <pre>{@code
 * HttpClient raw    = new OkHttp3Client();
 * HttpClient client = new InterceptingHttpClient(raw, List.of(
 *     new RateLimitHttpInterceptor(rateLimiter, "Notion"),
 *     new LoggingHttpInterceptor()
 * ));
 * HttpResponse rs = client.send(request);
 * }</pre>
 */
public class InterceptingHttpClient implements HttpClient {

  private final HttpClient delegate;
  private final List<HttpClientInterceptor> interceptors;

  /**
   * @param delegate the actual HTTP client that sends requests over the network
   * @param interceptors ordered list of interceptors; must not be {@code null}
   */
  public InterceptingHttpClient(HttpClient delegate, List<HttpClientInterceptor> interceptors) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
    this.interceptors = List.copyOf(Objects.requireNonNull(interceptors, "interceptors"));
  }

  @Override
  public HttpResponse send(HttpRequest request) throws IOException {
    // before
    for (HttpClientInterceptor interceptor : interceptors) {
      request = interceptor.beforeSend(request);
    }

    // delegate
    HttpResponse response = delegate.send(request);

    // after
    for (HttpClientInterceptor interceptor : interceptors) {
      interceptor.afterReceive(request, response);
    }

    return response;
  }
}
