package io.kristixlab.notion.api.http.interceptor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/** Unit tests for {@link RateLimiter}. */
class RateLimiterTest {

  @Test
  @DisplayName("tryConsume succeeds while under the per-second capacity (3 tokens)")
  void tryConsume_succeedsWithinCapacity() {
    RateLimiter limiter = new RateLimiter();

    assertDoesNotThrow(() -> limiter.tryConsume("Notion"));
    assertDoesNotThrow(() -> limiter.tryConsume("Notion"));
    assertDoesNotThrow(() -> limiter.tryConsume("Notion"));
  }

  @Test
  @DisplayName("tryConsume throws when the bucket is exhausted")
  void tryConsume_throwsWhenExhausted() {
    RateLimiter limiter = new RateLimiter();

    limiter.tryConsume("Notion");
    limiter.tryConsume("Notion");
    limiter.tryConsume("Notion");

    assertThrows(RuntimeException.class, () -> limiter.tryConsume("Notion"));
  }

  @Test
  @DisplayName("tryConsume with an unregistered key is a no-op (no exception)")
  void tryConsume_unknownKey_isNoOp() {
    RateLimiter limiter = new RateLimiter();

    assertDoesNotThrow(() -> limiter.tryConsume("UnknownApi"));
    assertDoesNotThrow(() -> limiter.tryConsume("UnknownApi"));
    assertDoesNotThrow(() -> limiter.tryConsume("UnknownApi"));
    assertDoesNotThrow(() -> limiter.tryConsume("UnknownApi"));
  }

  @Test
  @DisplayName("tryConsume with null key is a no-op (no exception)")
  void tryConsume_nullKey_isNoOp() {
    RateLimiter limiter = new RateLimiter();

    assertDoesNotThrow(() -> limiter.tryConsume(null));
  }
}

