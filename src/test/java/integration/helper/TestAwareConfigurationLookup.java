package integration.helper;

import io.kristaxlab.notion.config.ConfigurationLookup;
import java.util.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.engine.ConfigurationParameters;

public class TestAwareConfigurationLookup {

  public static Optional<Boolean> lookupBoolean(String key, ConfigurationParameters params) {
    String value = lookup(key, params).orElse(null);
    return value == null || value.trim().isEmpty()
        ? Optional.empty()
        : Optional.of(Boolean.parseBoolean(value));
  }

  public static Optional<String> lookup(String key, ConfigurationParameters params) {
    Optional<String> value = ConfigurationLookup.lookup(key);
    if (value.isPresent() && !value.get().trim().isEmpty()) {
      return value;
    }

    List<String> keyModifications = ConfigurationLookup.getKeyModifications(key);
    for (String modifiedKey : keyModifications) {
      value = params.get(modifiedKey);
      if (value.isPresent() && !value.get().trim().isEmpty()) {
        return value;
      }
    }

    return Optional.empty();
  }

  public static Optional<Boolean> lookupBoolean(String key, ExtensionContext testExtensionContext) {
    String value = lookup(key, testExtensionContext).orElse(null);
    return value == null || value.trim().isEmpty()
        ? Optional.empty()
        : Optional.of(Boolean.parseBoolean(value));
  }

  public static Optional<String> lookup(String key, ExtensionContext testExtensionContext) {
    Optional<String> value = ConfigurationLookup.lookup(key);
    if (value.isPresent() && !value.get().trim().isEmpty()) {
      return value;
    }

    List<String> keyModifications = ConfigurationLookup.getKeyModifications(key);
    for (String modifiedKey : keyModifications) {
      value = testExtensionContext.getConfigurationParameter(modifiedKey);
      if (value.isPresent() && !value.get().trim().isEmpty()) {
        return value;
      }
    }

    return Optional.empty();
  }
}
