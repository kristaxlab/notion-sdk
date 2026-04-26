package io.kristaxlab.notion.endpoints.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidatorTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("check not null or empty should throw exception when value is null")
  void checkNotNullOrEmpty_shouldThrowException_whenValueIsNull(String value) {
    String valueName = "testValue";

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Validator.checkNotNullOrEmpty(value, valueName);
        });
  }

  @Test
  @DisplayName("check not null or empty should not throw exception when value is valid")
  void checkNotNullOrEmpty_shouldNotThrowException_whenValueIsValid() {
    String value = "valid";
    String valueName = "testValue";

    assertDoesNotThrow(
        () -> {
          Validator.checkNotNullOrEmpty(value, valueName);
        });
  }

  @Test
  @DisplayName("check not null should throw exception when value is null")
  void checkNotNull_shouldThrowException_whenValueIsNull() {
    String valueName = "testValue";

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Validator.checkNotNull(null, valueName);
            });

    assertEquals("testValue cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("check not null should not throw exception when value is valid")
  void checkNotNull_shouldNotThrowException_whenValueIsValid() {
    Object value = new Object();
    String valueName = "testValue";

    assertDoesNotThrow(
        () -> {
          Validator.checkNotNull(value, valueName);
        });
  }
}
