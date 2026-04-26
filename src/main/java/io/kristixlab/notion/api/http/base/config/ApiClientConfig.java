package io.kristixlab.notion.api.http.base.config;

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

  public static final ConfigKey<String> API_BASE_URL = ConfigKey.of("apiBaseUrl");

  private final Map<ConfigKey<?>, Object> values;

  private ApiClientConfig(Map<ConfigKey<?>, Object> values) {
    this.values = Map.copyOf(values);
  }

  public static Builder builder() {
    return new Builder();
  }

  /** Returns a config with no entries set. Use {@link #getOrDefault} for fallback values. */
  public static ApiClientConfig defaults() {
    return new ApiClientConfig(Map.of());
  }

  /** Returns the value for the given key, or empty if not set. */
  public <T> Optional<T> get(ConfigKey<T> key) {
    @SuppressWarnings("unchecked")
    T value = (T) values.get(key);
    return Optional.ofNullable(value);
  }

  /** Returns the value for the given key, or {@code fallback} if absent. */
  public <T> T getOrDefault(ConfigKey<T> key, T fallback) {
    return get(key).orElse(fallback);
  }

  public static final class Builder {

    private final Map<ConfigKey<?>, Object> values = new LinkedHashMap<>();

    private Builder() {}

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
