package io.kristaxlab.notion.http.base.client;

import io.kristaxlab.notion.http.base.client.HttpClient.HttpRequest;
import io.kristaxlab.notion.http.base.client.HttpClient.HttpResponse;

/**
 * Strategy for inspecting HTTP responses and throwing domain-specific exceptions on errors.
 *
 * <p>Implementations decide what constitutes an error (typically {@code status >= 400}) and map the
 * raw response into a meaningful exception. This keeps error interpretation separate from both the
 * transport layer ({@link HttpClient}) and cross-cutting interceptors.
 *
 * @see ErrorHandlingHttpClient
 */
@FunctionalInterface
public interface ErrorResponseHandler {

  /** No-op handler for callers that handle raw status codes directly. */
  ErrorResponseHandler QUIET = (request, response) -> {};

  /**
   * Inspects the response and throws if it represents an error. Must return normally for non-error
   * responses.
   *
   * @param request original HTTP request
   * @param response HTTP response to inspect
   * @throws RuntimeException a domain-specific exception if the response is an error
   */
  void handle(HttpRequest request, HttpResponse response);
}
