package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.base.interceptor.HttpClientInterceptor;

/**
 * Rate-limiting interceptor. Delegates to {@link RateLimiter} — if the limit is exceeded, the
 * request is rejected before being sent.
 */
public class RateLimitHttpInterceptor implements HttpClientInterceptor {

  private final RateLimiter rateLimiter;
  private final String rateLimiterKey;

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
