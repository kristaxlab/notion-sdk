package integration.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.engine.ConfigurationParameters;

class ConfigurationLookupHelperTest {

  private static final String TEST_KEY = "configLookupHelperTestKey";
  private static final String TEST_ENV_KEY = "CONFIG_LOOKUP_HELPER_TEST_KEY";
  private static final String TEST_DOT_KEY = "config.lookup.helper.test.key";

  @AfterEach
  void clearTestSystemProperties() {
    System.clearProperty(TEST_KEY);
    System.clearProperty(TEST_ENV_KEY);
    System.clearProperty(TEST_DOT_KEY);
    System.clearProperty("user.profile.id");
    System.clearProperty("USER_PROFILE_ID");
    System.clearProperty("userProfileId");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("getKeyModifications returns empty list for null or blank keys")
  void getKeyModifications_nullOrBlank_returnsEmpty(String key) {
    assertTrue(ConfigurationLookupHelper.getKeyModifications(key).isEmpty());
  }

  @Test
  @DisplayName("getKeyModifications builds ENV, camelCase, and dot-separated variants")
  void getKeyModifications_buildsAllVariants() {
    List<String> variants = ConfigurationLookupHelper.getKeyModifications("user.profile.id");

    assertEquals(List.of("user.profile.id", "USER_PROFILE_ID", "userProfileId"), variants);
  }

  @Test
  @DisplayName("getKeyModifications keeps original key first and deduplicates variants")
  void getKeyModifications_keepsOriginalAndDeduplicates() {
    List<String> variants = ConfigurationLookupHelper.getKeyModifications("userProfileId");

    assertEquals("userProfileId", variants.get(0));
    assertTrue(variants.contains("USER_PROFILE_ID"));
    assertTrue(variants.contains("user.profile.id"));
    assertEquals(3, variants.size());
  }

  @Test
  @DisplayName("getKeyModifications tokenizes hyphenated keys")
  void getKeyModifications_hyphenatedKey() {
    List<String> variants = ConfigurationLookupHelper.getKeyModifications("user-profile-id");

    assertTrue(variants.contains("user-profile-id"));
    assertTrue(variants.contains("USER_PROFILE_ID"));
    assertTrue(variants.contains("userProfileId"));
    assertTrue(variants.contains("user.profile.id"));
  }

  @Test
  @DisplayName("lookup returns empty when key is absent from env and system properties")
  void lookup_absentKey_returnsEmpty() {
    assertTrue(ConfigurationLookupHelper.lookup("definitely.missing.config.lookup.key").isEmpty());
  }

  @Test
  @DisplayName("lookup finds value via system property using a key variant")
  void lookup_findsSystemPropertyViaVariant() {
    System.setProperty(TEST_ENV_KEY, "from-sysprop");

    Optional<String> value = ConfigurationLookupHelper.lookup(TEST_DOT_KEY);

    assertEquals(Optional.of("from-sysprop"), value);
  }

  @Test
  @DisplayName("lookup skips blank system property values")
  void lookup_blankSystemProperty_returnsEmpty() {
    System.setProperty(TEST_KEY, "   ");

    assertTrue(ConfigurationLookupHelper.lookup(TEST_KEY).isEmpty());
  }

  @Test
  @DisplayName("lookup with default returns default when key is absent")
  void lookup_withDefault_returnsDefaultWhenAbsent() {
    assertEquals(
        "fallback",
        ConfigurationLookupHelper.lookup("definitely.missing.config.lookup.key", "fallback"));
  }

  @Test
  @DisplayName("lookup with default returns found value when present")
  void lookup_withDefault_returnsFoundValue() {
    System.setProperty(TEST_KEY, "present");

    assertEquals("present", ConfigurationLookupHelper.lookup(TEST_KEY, "fallback"));
  }

  @Test
  @DisplayName("lookup with ConfigurationParameters uses params when env/sysprops are empty")
  void lookup_withParams_usesConfigurationParameters() {
    ConfigurationParameters params = mapParams(Map.of("USER_PROFILE_ID", "from-params"));

    Optional<String> value = ConfigurationLookupHelper.lookup("user.profile.id", params);

    assertEquals(Optional.of("from-params"), value);
  }

  @Test
  @DisplayName("lookup with ConfigurationParameters prefers system property over params")
  void lookup_withParams_prefersSystemProperty() {
    System.setProperty("user.profile.id", "from-sysprop");
    ConfigurationParameters params = mapParams(Map.of("user.profile.id", "from-params"));

    Optional<String> value = ConfigurationLookupHelper.lookup("user.profile.id", params);

    assertEquals(Optional.of("from-sysprop"), value);
  }

  @Test
  @DisplayName("lookup with ConfigurationParameters skips blank param values")
  void lookup_withParams_skipsBlankValues() {
    ConfigurationParameters params =
        mapParams(
            Map.of(
                "user.profile.id", "   ",
                "USER_PROFILE_ID", "from-env-like"));

    Optional<String> value = ConfigurationLookupHelper.lookup("user.profile.id", params);

    assertEquals(Optional.of("from-env-like"), value);
  }

  @Test
  @DisplayName("lookupBoolean returns empty when key is absent")
  void lookupBoolean_absentKey_returnsEmpty() {
    assertTrue(
        ConfigurationLookupHelper.lookupBoolean("definitely.missing.config.lookup.key").isEmpty());
  }

  @Test
  @DisplayName("lookupBoolean parses true and false from system properties")
  void lookupBoolean_parsesBooleanValues() {
    System.setProperty(TEST_KEY, "true");
    assertEquals(Optional.of(true), ConfigurationLookupHelper.lookupBoolean(TEST_KEY));

    System.setProperty(TEST_KEY, "false");
    assertEquals(Optional.of(false), ConfigurationLookupHelper.lookupBoolean(TEST_KEY));
  }

  @Test
  @DisplayName("lookupBoolean with default returns default when key is absent")
  void lookupBoolean_withDefault_returnsDefaultWhenAbsent() {
    assertTrue(
        ConfigurationLookupHelper.lookupBoolean("definitely.missing.config.lookup.key", true));
    assertFalse(
        ConfigurationLookupHelper.lookupBoolean("definitely.missing.config.lookup.key", false));
  }

  @Test
  @DisplayName("lookupBoolean with default returns parsed value when present")
  void lookupBoolean_withDefault_returnsParsedValue() {
    System.setProperty(TEST_KEY, "true");

    assertTrue(ConfigurationLookupHelper.lookupBoolean(TEST_KEY, false));
  }

  @Test
  @DisplayName("lookupBoolean with ConfigurationParameters parses param value")
  void lookupBoolean_withParams_parsesValue() {
    ConfigurationParameters params = mapParams(Map.of(TEST_DOT_KEY, "true"));

    assertEquals(Optional.of(true), ConfigurationLookupHelper.lookupBoolean(TEST_DOT_KEY, params));
  }

  @Test
  @DisplayName("lookupBoolean with ConfigurationParameters returns empty for blank value")
  void lookupBoolean_withParams_blankValue_returnsEmpty() {
    ConfigurationParameters params = mapParams(Map.of(TEST_DOT_KEY, "  "));

    assertTrue(ConfigurationLookupHelper.lookupBoolean(TEST_DOT_KEY, params).isEmpty());
  }

  private static ConfigurationParameters mapParams(Map<String, String> values) {
    Map<String, String> copy = new HashMap<>(values);
    return new ConfigurationParameters() {
      @Override
      public Optional<String> get(String key) {
        return Optional.ofNullable(copy.get(key));
      }

      @Override
      public Optional<Boolean> getBoolean(String key) {
        return get(key).map(Boolean::parseBoolean);
      }

      @Override
      public int size() {
        return copy.size();
      }

      @Override
      public Set<String> keySet() {
        return copy.keySet();
      }
    };
  }
}
