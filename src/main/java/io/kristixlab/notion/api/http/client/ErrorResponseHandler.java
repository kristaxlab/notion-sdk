package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.http.interceptor.HttpClientInterceptor;

/**
 * Strategy for inspecting HTTP responses and throwing domain-specific exceptions when the response
 * represents an error.
 *
 * <p>Implementations decide what constitutes an error (typically {@code status >= 400}) and how to
 * map the raw response into a meaningful exception. This keeps error interpretation separate from
 * the transport pipeline ({@link HttpClient}) and from cross-cutting interceptors ({@link
 * HttpClientInterceptor}).
 *
 * <p>Example for a Notion-specific handler:
 *
 * <pre>{@code
 * ErrorResponseHandler notionErrors = (request, response) -> {
 *     if (response.statusCode() < 400) return;
 *     NotionError error = parseBody(response);
 *     switch (response.statusCode()) {
 *         case 400 -> throw new ValidationException(...);
 *         case 401 -> throw new UnauthorizedException(...);
 *         ...
 *     }
 * };
 * }</pre>
 *
 * @see ErrorHandlingHttpClient
 */
@FunctionalInterface
public interface ErrorResponseHandler {

  /**
   * A handler that never throws an exception. Use this when no error mapping is needed (e.g. the
   * caller handles raw status codes directly).
   */
  ErrorResponseHandler QUIET = (request, response) -> {};

  /**
   * Inspects the response and throws an appropriate exception if it represents an error.
   *
   * <p>If the response is <em>not</em> an error (e.g. a 2xx status), this method <b>must</b> return
   * normally (no exception, no side-effects).
   *
   * @param request the request that was sent (useful for context in error messages)
   * @param response the raw HTTP response to inspect
   * @throws RuntimeException a domain-specific exception if the response is an error
   */
  void handle(HttpRequest request, HttpResponse response);
}
