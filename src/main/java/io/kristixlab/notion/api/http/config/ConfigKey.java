package io.kristixlab.notion.api.http.config;

import java.util.Objects;

/**
 * Type-safe, identity-based key for {@link ApiClientConfig}. Always declare as a {@code static
 * final} constant — two calls to {@code ConfigKey.of("x")} produce distinct, non-equal keys.
 *
 * @param <T> the value type associated with this key
 */
@SuppressWarnings("unused") // T is a phantom type: used only for compile-time type safety at call sites
public final class ConfigKey<T> {

  private final String name;

  private ConfigKey(String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  /** Creates a new key. Each invocation produces a distinct instance — store as a constant. */
  public static <T> ConfigKey<T> of(String name) {
    return new ConfigKey<>(name);
  }

  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return "ConfigKey[" + name + "]";
  }

}
