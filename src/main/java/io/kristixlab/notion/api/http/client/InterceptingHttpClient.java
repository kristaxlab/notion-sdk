package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.interceptor.HttpClientInterceptor;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Decorator that runs an ordered list of {@link HttpClientInterceptor}s around each request.
 *
 * <p>Flow: {@code beforeSend} (all interceptors) → {@code delegate.send()} → {@code afterReceive}
 * (all interceptors). If {@code beforeSend} throws, the request is not sent. If {@code send}
 * throws, {@code afterReceive} is not called.
 */
public class InterceptingHttpClient implements HttpClient {

  private final HttpClient delegate;
  private final List<HttpClientInterceptor> interceptors;

  public InterceptingHttpClient(HttpClient delegate, List<HttpClientInterceptor> interceptors) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
    this.interceptors = List.copyOf(Objects.requireNonNull(interceptors, "interceptors"));
  }

  @Override
  public HttpResponse send(HttpRequest request) throws IOException {
    for (HttpClientInterceptor interceptor : interceptors) {
      request = interceptor.beforeSend(request);
    }

    HttpResponse response = delegate.send(request);

    for (HttpClientInterceptor interceptor : interceptors) {
      interceptor.afterReceive(request, response);
    }

    return response;
  }
}
