package testkit.util;

import io.kristaxlab.notion.config.ConfigurationLookup;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConfigurationLookup {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestConfigurationLookup.class);

  public static String lookupRequired(String key, ExtensionContext context) {
    Optional<String> value = lookup(key, context);
    LOGGER.debug("{}: {}", key, value.orElse(null));

    if (value.isEmpty()) {
      throw new IllegalStateException("Required property " + key + " is missing");
    }
    return value.get();
  }

  public static String lookupOptional(String key, ExtensionContext context) {
    Optional<String> value = lookup(key, context);
    LOGGER.debug("{}: {}", key, value.orElse(null));
    return value.orElse(null);
  }

  public static boolean lookupBoolean(String key, ExtensionContext context) {
    String value = lookup(key, context).orElse(null);
    LOGGER.debug("{}: {}", key, value);
    return value == null || value.trim().isEmpty() ? false : (Boolean.parseBoolean(value));
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
