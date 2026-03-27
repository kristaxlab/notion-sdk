package io.kristixlab.notion.api.http.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable, type-safe configuration bag for {@link
 * io.kristixlab.notion.api.http.client.ApiClientImpl}.
 *
 * <p>Settings are stored as typed key/value pairs keyed by {@link ConfigKey} instances. Well-known
 * SDK settings are exposed as {@code public static final} key constants on this class. External
 * projects can define and attach their own settings without modifying this class:
 */
public final class ApiClientConfig {

  public static final ConfigKey<String> API_BASE_URL = ConfigKey.of("apiBaseUrl");

  private final Map<ConfigKey<?>, Object> values;

  private ApiClientConfig(Map<ConfigKey<?>, Object> values) {
    this.values = Map.copyOf(values);
  }

  /** Returns a new {@link Builder}. */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns a config with all settings at their defaults (no entries set). Callers should use
   * {@link #getOrDefault(ConfigKey, Object)} to supply fallback values.
   */
  public static ApiClientConfig defaults() {
    return new ApiClientConfig(Map.of());
  }

  /**
   * Returns the value for the given key, or {@link Optional#empty()} if not set.
   *
   * @param key the config key
   * @param <T> the value type
   * @return the stored value, if present
   */
  public <T> Optional<T> get(ConfigKey<T> key) {
    @SuppressWarnings("unchecked")
    T value = (T) values.get(key);
    return Optional.ofNullable(value);
  }

  /**
   * Returns the value for the given key, or {@code fallback} if not set.
   *
   * @param key the config key
   * @param fallback the default value returned when the key is absent
   * @param <T> the value type
   * @return the stored value, or {@code fallback}
   */
  public <T> T getOrDefault(ConfigKey<T> key, T fallback) {
    return get(key).orElse(fallback);
  }

  /** Fluent builder for {@link ApiClientConfig}. */
  public static final class Builder {

    private final Map<ConfigKey<?>, Object> values = new LinkedHashMap<>();

    private Builder() {}

    /**
     * Sets a typed config value.
     *
     * @param key the config key; must be a {@code static final} constant
     * @param value the value to store; must not be {@code null}
     * @param <T> the value type
     * @return this builder
     */
    public <T> Builder set(ConfigKey<T> key, T value) {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(value, "value");
      values.put(key, value);
      return this;
    }

    public Builder apiBaseUrl(String apiBaseUrl) {
      return set(API_BASE_URL, apiBaseUrl);
    }

    public ApiClientConfig build() {
      return new ApiClientConfig(values);
    }
  }
}
