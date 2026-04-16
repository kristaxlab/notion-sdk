package io.kristaxlab.notion.http.base.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable, type-safe configuration bag keyed by {@link ConfigKey} instances.
 *
 * <p>Well-known settings are exposed as constants on this class. External projects can define
 * additional keys without modifying SDK code.
 */
public final class ApiClientConfig {

  /** Configuration key for overriding the API base URL used by {@code ApiClient}. */
  public static final ConfigKey<String> API_BASE_URL = ConfigKey.of("apiBaseUrl");

  private final Map<ConfigKey<?>, Object> values;

  private ApiClientConfig(Map<ConfigKey<?>, Object> values) {
    this.values = Map.copyOf(values);
  }

  /**
   * Creates a builder for assembling a typed configuration map.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns a config with no entries set.
   *
   * @return an empty config
   */
  public static ApiClientConfig defaults() {
    return new ApiClientConfig(Map.of());
  }

  /**
   * Returns the value for the given key, or empty if not set.
   *
   * @param key configuration key
   * @param <T> configuration value type
   * @return value wrapped in {@link Optional}, or empty if absent
   */
  public <T> Optional<T> get(ConfigKey<T> key) {
    @SuppressWarnings("unchecked")
    T value = (T) values.get(key);
    return Optional.ofNullable(value);
  }

  /**
   * Returns the value for the given key, or {@code fallback} if absent.
   *
   * @param key configuration key
   * @param fallback fallback value when key is not present
   * @param <T> configuration value type
   * @return configured value or fallback
   */
  public <T> T getOrDefault(ConfigKey<T> key, T fallback) {
    return get(key).orElse(fallback);
  }

  /** Builder for immutable {@link ApiClientConfig} instances. */
  public static final class Builder {

    private final Map<ConfigKey<?>, Object> values = new LinkedHashMap<>();

    private Builder() {}

    /**
     * Sets a non-null configuration value for the given key.
     *
     * @param key configuration key
     * @param value value to store
     * @param <T> configuration value type
     * @return this builder
     * @throws NullPointerException if {@code key} or {@code value} is null
     */
    public <T> Builder set(ConfigKey<T> key, T value) {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(value, "value");
      values.put(key, value);
      return this;
    }

    /**
     * Sets the API base URL.
     *
     * @param apiBaseUrl API root URL (for example, {@code https://api.notion.com/v1})
     * @return this builder
     */
    public Builder apiBaseUrl(String apiBaseUrl) {
      return set(API_BASE_URL, apiBaseUrl);
    }

    /**
     * Builds an immutable {@link ApiClientConfig}.
     *
     * @return immutable config snapshot
     */
    public ApiClientConfig build() {
      return new ApiClientConfig(values);
    }
  }
}
