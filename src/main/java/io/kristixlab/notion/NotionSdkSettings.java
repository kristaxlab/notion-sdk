package io.kristixlab.notion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

/**
 * Loads and validates settings for integration tests (Singleton pattern).
 *
 * <p>Loading order: 1. Environment variables 2. src/test/resources/integration-tests.yml
 */
@Slf4j
public final class NotionSdkSettings {

  private static final String SETTINGS_FILE = "notion-sdk-settings.yml";
  private static final String SETTINGS_FILE_DFLT = "notion-sdk-settings-default.yml";

  private static NotionSdkSettings instance;
  private final Map<String, Object> settings;
  private final Map<String, Object> defaultSettings;

  private NotionSdkSettings() {
    this.defaultSettings = loadSettings(SETTINGS_FILE_DFLT);
    this.settings = loadSettings(SETTINGS_FILE);
  }

  /**
   * Gets the singleton instance of IntegrationTestsSettings.
   *
   * @return the singleton instance
   */
  public static synchronized NotionSdkSettings getInstance() {
    if (instance == null) {
      instance = new NotionSdkSettings();
    }
    return instance;
  }

  /**
   * Gets a string value from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path (e.g., "workspace.id")
   * @return the string value, or null if not found
   */
  public String getString(String key) {
    // First try environment variable
    String envValue = getFromEnvironment(key);
    if (envValue != null) {
      return envValue;
    }

    // Then try YAML file
    return (String) getValue(key);
  }

  /**
   * Gets an integer value from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path
   * @return the integer value, or null if not found or not a valid integer
   */
  public Integer getInteger(String key) {
    // First try environment variable
    String envValue = getFromEnvironment(key);
    if (envValue != null) {
      try {
        return Integer.parseInt(envValue);
      } catch (NumberFormatException e) {
        log.warn("Environment variable {} is not a valid integer: {}", key, envValue);
      }
    }

    // Then try YAML file
    return (Integer) getValue(key);
  }

  /**
   * Gets a long value from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path
   * @return the long value, or null if not found or not a valid long
   */
  public Long getLong(String key) {
    // First try environment variable
    String envValue = getFromEnvironment(key);
    if (envValue != null) {
      try {
        return Long.parseLong(envValue);
      } catch (NumberFormatException e) {
        log.warn("Environment variable {} is not a valid long: {}", key, envValue);
      }
    }

    // Then try YAML file
    return (Long) getValue(key);
  }

  /**
   * Gets a double value from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path
   * @return the double value, or null if not found or not a valid double
   */
  public Double getDouble(String key) {
    // First try environment variable
    String envValue = getFromEnvironment(key);
    if (envValue != null) {
      try {
        return Double.parseDouble(envValue);
      } catch (NumberFormatException e) {
        log.warn("Environment variable {} is not a valid double: {}", key, envValue);
      }
    }

    // Then try YAML file
    return (Double) getValue(key);
  }

  /**
   * Gets a boolean value from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path
   * @return the boolean value, or null if not found or not a valid boolean
   */
  public Boolean getBoolean(String key) {
    // First try environment variable
    String envValue = getFromEnvironment(key);
    if (envValue != null) {
      return Boolean.parseBoolean(envValue);
    }

    // Then try YAML file
    return (Boolean) getValue(key);
  }

  /**
   * Gets a list of strings from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path
   * @return the list of strings, or empty list if not found or not a valid array
   */
  @SuppressWarnings("unchecked")
  public List<String> getList(String key) {
    // Environment variables don't support lists directly

    // Try YAML file
    return (List<String>) getValue(key);
  }

  /**
   * Gets a map of key-value pairs from the settings using a dot-separated key path.
   *
   * @param key the dot-separated key path
   * @return the map of string keys to string values, or empty map if not found or not a valid
   *     object
   */
  @SuppressWarnings("unchecked")
  public Map<String, String> getMap(String key) {
    // Environment variables don't support maps directly

    // Try YAML file
    return (Map<String, String>) getValue(key);
  }

  /**
   * Checks if a setting exists at the given key path.
   *
   * @param key the dot-separated key path
   * @return true if the setting exists, false otherwise
   */
  public boolean exists(String key) {
    return getFromEnvironment(key) != null || getValue(key) != null;
  }

  private String getFromEnvironment(String key) {
    // Convert dot notation to environment variable format
    String envKey = key.toUpperCase().replace('.', '_');
    return System.getenv(envKey);
  }

  public Object getValue(String key) {
    if (settings == null && defaultSettings == null) {
      return null;
    }
    Object value = getValue(settings, new ArrayList<>(List.of(key.split("\\."))), 0);
    if (value == null) {
      value = getValue(defaultSettings, new ArrayList<>(List.of(key.split("\\."))), 0);
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  private Object getValue(Map<String, Object> settingsMap, List<String> segments, int startFrom) {
    if (startFrom == segments.size() || settings == null) {
      return null;
    }

    Object nested = null;
    String key = "";
    while (startFrom < segments.size()) {
      key += (!key.isEmpty() ? "." : "") + segments.get(startFrom);
      nested = settingsMap.get(key);
      if (nested != null) {
        if (startFrom != segments.size() - 1) {
          if (nested instanceof Map) {
            return getValue((Map<String, Object>) nested, segments, startFrom + 1);
          } else {
            return null;
          }
        } else {
          return nested;
        }
      }
      startFrom++;
    }

    return nested;
  }

  private Map<String, Object> loadSettings(String resourceName) {
    Yaml yaml = new Yaml();

    // First try to load from classpath
    try (InputStream resourceStream =
        getClass().getClassLoader().getResourceAsStream(resourceName)) {
      if (resourceStream != null) {
        log.info("Loading integration test settings from classpath: {}", resourceName);
        return yaml.load(resourceStream);
      }
    } catch (IOException e) {
      log.error("Failed to load integration test settings from classpath", e);
    }

    log.warn("Integration test settings file not found. Using empty settings.");
    return new HashMap<>();
  }
}
