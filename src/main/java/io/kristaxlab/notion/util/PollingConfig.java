package io.kristaxlab.notion.util;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for polling operations that wait for asynchronous Notion updates to complete.
 *
 * <p>Supports both time-based (timeout) and attempt-based limits, as well as configurable polling
 * intervals. Use {@link #of(Duration, Duration)} for simple cases or {@link #builder()} for full
 * control.
 */
@Getter
@Setter
public class PollingConfig {

  /** Maximum time to wait before timing out. If null, only {@link #maxAttempts} is used. */
  private Duration timeout;

  /**
   * Maximum number of polling attempts. If null, only {@link #timeout} is used. At least one of
   * {@code timeout} or {@code maxAttempts} must be set.
   */
  private Integer maxAttempts;

  /** Time to wait between polling attempts. Defaults to 500ms. */
  private Duration pollingInterval;

  /**
   * Creates a config with both timeout and max attempts. Uses 500ms polling interval.
   *
   * @param timeout maximum time to poll before giving up
   * @param pollingInterval time between polling attempts
   * @return configured polling config
   */
  public static PollingConfig of(Duration timeout, Duration pollingInterval) {
    PollingConfig config = new PollingConfig();
    config.setTimeout(timeout);
    config.setPollingInterval(pollingInterval);
    return config;
  }

  /**
   * Creates a config with max attempts only. Uses 500ms polling interval.
   *
   * @param maxAttempts maximum number of polling attempts
   * @return configured polling config
   */
  public static PollingConfig ofAttempts(int maxAttempts) {
    PollingConfig config = new PollingConfig();
    config.setMaxAttempts(maxAttempts);
    config.setPollingInterval(Duration.ofMillis(500));
    return config;
  }

  /**
   * Creates a config with timeout only. Uses 500ms polling interval.
   *
   * @param timeout maximum time to poll before giving up
   * @return configured polling config
   */
  public static PollingConfig ofTimeout(Duration timeout) {
    PollingConfig config = new PollingConfig();
    config.setTimeout(timeout);
    config.setPollingInterval(Duration.ofMillis(500));
    return config;
  }

  /**
   * Creates a builder for full control over polling configuration.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final PollingConfig config = new PollingConfig();

    public Builder timeout(Duration timeout) {
      config.setTimeout(timeout);
      return this;
    }

    public Builder maxAttempts(int maxAttempts) {
      config.setMaxAttempts(maxAttempts);
      return this;
    }

    public Builder pollingInterval(Duration pollingInterval) {
      config.setPollingInterval(pollingInterval);
      return this;
    }

    public PollingConfig build() {
      if (config.getTimeout() == null && config.getMaxAttempts() == null) {
        throw new IllegalStateException(
            "At least one of timeout or maxAttempts must be configured");
      }
      if (config.getPollingInterval() == null) {
        config.setPollingInterval(Duration.ofMillis(500));
      }
      return config;
    }
  }
}
