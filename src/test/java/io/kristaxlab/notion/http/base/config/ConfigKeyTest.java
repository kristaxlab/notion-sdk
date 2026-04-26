package io.kristaxlab.notion.http.base.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigKeyTest {

  @Test
  @DisplayName("of() returns key with given name")
  void of_returnsKeyWithGivenName() {
    ConfigKey<String> key = ConfigKey.of("myKey");
    assertEquals("myKey", key.name());
  }

  @Test
  @DisplayName("toString() contains name")
  void toString_containsName() {
    ConfigKey<Integer> key = ConfigKey.of("timeout");
    assertEquals("ConfigKey[timeout]", key.toString());
  }

  @Test
  @DisplayName("two calls with same name produce different keys")
  void twoCallsWithSameName_produceDifferentKeys() {
    ConfigKey<String> a = ConfigKey.of("same");
    ConfigKey<String> b = ConfigKey.of("same");

    assertNotSame(a, b);
    assertNotEquals(a, b);
  }

  @Test
  @DisplayName("of() with null name throws")
  void of_nullName_throws() {
    assertThrows(NullPointerException.class, () -> ConfigKey.of(null));
  }
}
