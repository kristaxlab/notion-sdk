package io.kristixlab.notion.api.http.config;

import java.util.Objects;

/**
 * A type-safe key for {@link ApiClientConfig}.
 *
 * <p>Equality is intentionally <strong>identity-based</strong> (reference equality). Two calls to
 * {@code ConfigKey.of("same-name")} produce two distinct, non-equal keys. Always declare keys as
 * {@code public static final} constants to ensure a single authoritative instance per key.
 *
 * <pre>{@code
 * public static final ConfigKey<Duration> CONNECT_TIMEOUT = ConfigKey.of("connectTimeout");
 * }</pre>
 *
 * @param <T> the type of the value associated with this key
 */
@SuppressWarnings("unused") // T is a phantom type: used only for compile-time type safety at call sites
public final class ConfigKey<T> {

  private final String name;

  private ConfigKey(String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  /**
   * Creates a new key with the given descriptive name.
   *
   * <p><strong>Important:</strong> each call produces a distinct key — store the result as a
   * {@code static final} constant and never call this more than once per logical setting.
   *
   * @param name a descriptive name used in {@link #toString()} and diagnostics
   * @param <T> the value type
   * @return a new {@link ConfigKey}
   */
  public static <T> ConfigKey<T> of(String name) {
    return new ConfigKey<>(name);
  }

  /** Returns the descriptive name provided at creation time. */
  public String name() {
    return name;
  }

  /** Returns a human-readable representation: {@code ConfigKey[name]}. */
  @Override
  public String toString() {
    return "ConfigKey[" + name + "]";
  }

}


