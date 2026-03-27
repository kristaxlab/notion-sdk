
    assertSame(request, result);
  }

  @Test
  @DisplayName("beforeSend allows requests up to the configured capacity")
  void beforeSend_allowsRequestsWithinCapacity() {
    RateLimiter limiter = new RateLimiter();
    RateLimitHttpInterceptor interceptor = new RateLimitHttpInterceptor(limiter, "Notion");
    HttpRequest request = anyRequest();

    assertDoesNotThrow(() -> interceptor.beforeSend(request));
    assertDoesNotThrow(() -> interceptor.beforeSend(request));
    assertDoesNotThrow(() -> interceptor.beforeSend(request));
  }

  @Test
  @DisplayName("beforeSend throws when the rate limit bucket is exhausted")
  void beforeSend_throwsWhenLimitExceeded() {
    RateLimiter limiter = new RateLimiter();
    RateLimitHttpInterceptor interceptor = new RateLimitHttpInterceptor(limiter, "Notion");
    HttpRequest request = anyRequest();

    limiter.tryConsume("Notion");
    limiter.tryConsume("Notion");
    limiter.tryConsume("Notion");

    assertThrows(RuntimeException.class, () -> interceptor.beforeSend(request));
  }

  @Test
  @DisplayName("beforeSend is a no-op for an unregistered rate-limiter key")
  void beforeSend_unknownKey_isNoOp() {
    RateLimiter limiter = new RateLimiter();
    RateLimitHttpInterceptor interceptor = new RateLimitHttpInterceptor(limiter, "UnknownApi");
    HttpRequest request = anyRequest();

    assertDoesNotThrow(() -> interceptor.beforeSend(request));
    assertDoesNotThrow(() -> interceptor.beforeSend(request));
    assertDoesNotThrow(() -> interceptor.beforeSend(request));
    assertDoesNotThrow(() -> interceptor.beforeSend(request));
  }

  @Test
  @DisplayName("afterReceive is a no-op (default interface implementation)")
  void afterReceive_isNoOp() {
    RateLimiter limiter = new RateLimiter();
    RateLimitHttpInterceptor interceptor = new RateLimitHttpInterceptor(limiter, "Notion");

    HttpRequest request = anyRequest();
    HttpResponse response = new HttpResponse(200, Map.of(), "{}".getBytes());

    assertDoesNotThrow(() -> interceptor.afterReceive(request, response));
  }
}

