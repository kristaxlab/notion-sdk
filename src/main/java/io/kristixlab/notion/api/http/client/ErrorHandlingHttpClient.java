package io.kristixlab.notion.api.http.client;

import java.io.IOException;
import java.util.Objects;

/**
 * A decorator that delegates to an underlying {@link HttpClient} and passes every response through
 * an {@link ErrorResponseHandler} before returning it to the caller.
 *
 * <p>If the handler considers the response an error it throws a domain-specific {@link
 * RuntimeException}; otherwise the response is returned unchanged.
 *
 * <p><b>Composition order matters.</b> Place this decorator <em>outside</em> the {@link
 * InterceptingHttpClient} so that cross-cutting interceptors (logging, rate-limiting) still observe
 * the raw response before the exception is thrown:
 *
 * <pre>{@code
 * HttpClient raw         = new OkHttp3Client();
 * HttpClient intercepted = new InterceptingHttpClient(raw, List.of(
 *     new RateLimitHttpInterceptor(limiter, "Notion"),
 *     new LoggingHttpInterceptor("Notion")
 * ));
 * HttpClient safe        = new ErrorHandlingHttpClient(intercepted, notionErrorHandler);
 *
 * // Response flow:  OkHttp3  →  interceptors (logging)  →  error handler (throws if 4xx/5xx)
 * HttpResponse rs = safe.send(request);
 * }</pre>
 *
 * @see ErrorResponseHandler
 * @see InterceptingHttpClient
 */
public class ErrorHandlingHttpClient implements HttpClient {

  private final HttpClient delegate;
  private final ErrorResponseHandler errorHandler;

  /**
   * @param delegate the HTTP client to delegate actual sending to
   * @param errorHandler strategy that inspects each response and may throw; must not be {@code
   *     null} — use {@link ErrorResponseHandler#QUIET} for no error mapping
   */
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
