package io.kristaxlab.notion.http.base.client;

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

  /**
   * @param delegate the client to delegate to
   * @param errorHandler the handler to use for processing responses
   */
  public ErrorHandlingHttpClient(HttpClient delegate, ErrorResponseHandler errorHandler) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
    this.errorHandler = Objects.requireNonNull(errorHandler, "errorHandler");
  }

  /**
   * Delegates the request to the underlying client and then processes the response with the error
   * handler.
   *
   * @param request the request to send
   * @return the response from the underlying client
   * @throws IOException if an I/O error occurs when sending the request
   */
  @Override
  public HttpResponse send(HttpRequest request) throws IOException {
    HttpResponse response = delegate.send(request);
    errorHandler.handle(request, response);
    return response;
  }
}
