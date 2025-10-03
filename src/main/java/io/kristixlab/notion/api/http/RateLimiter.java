package io.kristixlab.notion.api.http;

import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

  private Map<String, Bucket> rateLimiters = new HashMap<>();

  // TODO implement mechnism for apis to register their rate limiters
  // TODO implement 429 + retry after processing (with error if retry after is mrore than 5 seecs)
  public RateLimiter() {
    Bucket bucket =
        Bucket.builder()
            .addLimit(limit -> limit.capacity(3).refillGreedy(3, Duration.ofSeconds(1)))
            .build();
    rateLimiters.put("Notion", bucket);
  }

  // TODO implement safe multithread access
  public synchronized void tryConsume(String apiName) {
    if (rateLimiters.get(apiName) == null) {
      return;
    }
    if (!rateLimiters.get(apiName).tryConsume(1)) {
      throw new RuntimeException("Rate Limiter disrupted the call. Try later. ");
    }
  }
}
