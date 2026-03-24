package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.client.HttpClient;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;

/**
 * Library-agnostic rate-limiting interceptor for the {@link HttpClient} pipeline.
 *
 * <p>Delegates to the existing {@link RateLimiter}. If the rate limit is exceeded, {@link
 * RateLimiter#tryConsume(String)} throws and the request is never sent.
 *
 * @see RateLimiter
 */
public class RateLimitHttpInterceptor implements HttpClientInterceptor {

  private final RateLimiter rateLimiter;
  private final String rateLimiterKey;

  /**
   * @param rateLimiter the limiter instance to consult
   * @param rateLimiterKey the key used to look up the bucket (e.g. {@code "Notion"})
   */
  public RateLimitHttpInterceptor(RateLimiter rateLimiter, String rateLimiterKey) {
    this.rateLimiter = rateLimiter;
    this.rateLimiterKey = rateLimiterKey;
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    rateLimiter.tryConsume(rateLimiterKey);
    return request;
  }
}
