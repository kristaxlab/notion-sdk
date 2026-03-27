package io.kristixlab.notion.api.http.client;

import java.io.IOException;
import java.util.Objects;

/**
 * Decorator that passes every response through an {@link ErrorResponseHandler} before returning it.
 *
 * <p>Place <em>outside</em> the {@link InterceptingHttpClient} so interceptors (logging,
 * rate-limiting) still observe the raw response before any exception is thrown.
 *
 * @see ErrorResponseHandler
 * @see InterceptingHttpClient
 */
public class ErrorHandlingHttpClient implements HttpClient {

  private final HttpClient delegate;
  private final ErrorResponseHandler errorHandler;

  public ErrorHandlingHttpClient(HttpClient delegate, ErrorResponseHandler errorHandler) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
    this.errorHandler = Objects.requireNonNull(errorHandler, "errorHandler");
  }

  @Override
  public HttpResponse send(HttpRequest request) throws IOException {
    HttpResponse response = delegate.send(request);
    errorHandler.handle(request, response);
    return response;
  }
}
