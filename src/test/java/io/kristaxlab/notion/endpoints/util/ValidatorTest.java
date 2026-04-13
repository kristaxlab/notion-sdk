package io.kristaxlab.notion.endpoints.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidatorTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void checkNotNullOrEmpty_shouldThrowException_whenValueIsNull(String value) {
    String valueName = "testValue";

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Validator.checkNotNullOrEmpty(value, valueName);
        });
  }

  @Test
  void checkNotNullOrEmpty_shouldNotThrowException_whenValueIsValid() {
    String value = "valid";
    String valueName = "testValue";

    assertDoesNotThrow(
        () -> {
          Validator.checkNotNullOrEmpty(value, valueName);
        });
  }

  @Test
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
  void checkNotNull_shouldNotThrowException_whenValueIsValid() {
    Object value = new Object();
    String valueName = "testValue";

    assertDoesNotThrow(
        () -> {
          Validator.checkNotNull(value, valueName);
        });
  }
}
