package io.kristaxlab.notion.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ConfigurationLookupTest {

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
    assertTrue(ConfigurationLookup.getKeyModifications(key).isEmpty());
  }

  @Test
  @DisplayName("getKeyModifications builds ENV, camelCase, and dot-separated variants")
  void getKeyModifications_buildsAllVariants() {
    List<String> variants = ConfigurationLookup.getKeyModifications("user.profile.id");

    assertEquals(List.of("user.profile.id", "USER_PROFILE_ID", "userProfileId"), variants);
  }

  @Test
  @DisplayName("getKeyModifications keeps original key first and deduplicates variants")
  void getKeyModifications_keepsOriginalAndDeduplicates() {
    List<String> variants = ConfigurationLookup.getKeyModifications("userProfileId");

    assertEquals("userProfileId", variants.get(0));
    assertTrue(variants.contains("USER_PROFILE_ID"));
    assertTrue(variants.contains("user.profile.id"));
    assertEquals(3, variants.size());
  }

  @Test
  @DisplayName("getKeyModifications tokenizes hyphenated keys")
  void getKeyModifications_hyphenatedKey() {
    List<String> variants = ConfigurationLookup.getKeyModifications("user-profile-id");

    assertTrue(variants.contains("user-profile-id"));
    assertTrue(variants.contains("USER_PROFILE_ID"));
    assertTrue(variants.contains("userProfileId"));
    assertTrue(variants.contains("user.profile.id"));
  }

  @Test
  @DisplayName("lookup returns empty when key is absent from env and system properties")
  void lookup_absentKey_returnsEmpty() {
    assertTrue(ConfigurationLookup.lookup("definitely.missing.config.lookup.key").isEmpty());
  }

  @Test
  @DisplayName("lookup finds value via system property using a key variant")
  void lookup_findsSystemPropertyViaVariant() {
    System.setProperty(TEST_ENV_KEY, "from-sysprop");

    Optional<String> value = ConfigurationLookup.lookup(TEST_DOT_KEY);

    assertEquals(Optional.of("from-sysprop"), value);
  }

  @Test
  @DisplayName("lookup skips blank system property values")
  void lookup_blankSystemProperty_returnsEmpty() {
    System.setProperty(TEST_KEY, "   ");

    assertTrue(ConfigurationLookup.lookup(TEST_KEY).isEmpty());
  }

  @Test
  @DisplayName("lookup with default returns default when key is absent")
  void lookup_withDefault_returnsDefaultWhenAbsent() {
    assertEquals(
        "fallback", ConfigurationLookup.lookup("definitely.missing.config.lookup.key", "fallback"));
  }

  @Test
  @DisplayName("lookup with default returns found value when present")
  void lookup_withDefault_returnsFoundValue() {
    System.setProperty(TEST_KEY, "present");

    assertEquals("present", ConfigurationLookup.lookup(TEST_KEY, "fallback"));
  }

  @Test
  @DisplayName("lookupBoolean returns empty when key is absent")
  void lookupBoolean_absentKey_returnsEmpty() {
    assertTrue(ConfigurationLookup.lookupBoolean("definitely.missing.config.lookup.key").isEmpty());
  }

  @Test
  @DisplayName("lookupBoolean parses true and false from system properties")
  void lookupBoolean_parsesBooleanValues() {
    System.setProperty(TEST_KEY, "true");
    assertEquals(Optional.of(true), ConfigurationLookup.lookupBoolean(TEST_KEY));

    System.setProperty(TEST_KEY, "false");
    assertEquals(Optional.of(false), ConfigurationLookup.lookupBoolean(TEST_KEY));
  }

  @Test
  @DisplayName("lookupBoolean with default returns default when key is absent")
  void lookupBoolean_withDefault_returnsDefaultWhenAbsent() {
    assertTrue(ConfigurationLookup.lookupBoolean("definitely.missing.config.lookup.key", true));
    assertFalse(ConfigurationLookup.lookupBoolean("definitely.missing.config.lookup.key", false));
  }

  @Test
  @DisplayName("lookupBoolean with default returns parsed value when present")
  void lookupBoolean_withDefault_returnsParsedValue() {
    System.setProperty(TEST_KEY, "true");

    assertTrue(ConfigurationLookup.lookupBoolean(TEST_KEY, false));
  }
}
