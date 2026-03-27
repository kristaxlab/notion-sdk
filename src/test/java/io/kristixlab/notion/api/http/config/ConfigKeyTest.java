package io.kristixlab.notion.api.http.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfigKeyTest {

  @Test
  void of_returnsKeyWithGivenName() {
    ConfigKey<String> key = ConfigKey.of("myKey");
    assertEquals("myKey", key.name());
  }

  @Test
  void toString_containsName() {
    ConfigKey<Integer> key = ConfigKey.of("timeout");
    assertEquals("ConfigKey[timeout]", key.toString());
  }

  @Test
  void twoCallsWithSameName_produceDifferentKeys() {
    ConfigKey<String> a = ConfigKey.of("same");
    ConfigKey<String> b = ConfigKey.of("same");

    assertNotSame(a, b);
    assertNotEquals(a, b);
  }

  @Test
  void sameInstance_isEqualToItself() {
    ConfigKey<String> key = ConfigKey.of("k");
    assertSame(key, key); // identity equality: a key is always the same object as itself
  }

  @Test
  void of_nullName_throws() {
    assertThrows(NullPointerException.class, () -> ConfigKey.of(null));
  }
}


