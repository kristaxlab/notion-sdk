package io.kristaxlab.notion.endpoints.util;

public class Validator {

  private Validator() {}

  /**
   * Validates that a string value is present and contains non-whitespace characters.
   *
   * @param value value to validate
   * @param valueName value name used in error messages
   * @throws IllegalArgumentException if {@code value} is {@code null}, empty, or blank
   */
  public static void checkNotNullOrEmpty(String value, String valueName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(valueName + " cannot be null, empty, or blank");
    }
  }

  /**
   * Validates that an object reference is not {@code null}.
   *
   * @param object object to validate
   * @param objectName object name used in error messages
   * @throws IllegalArgumentException if {@code object} is {@code null}
   */
  public static void checkNotNull(Object object, String objectName) {
    if (object == null) {
      throw new IllegalArgumentException(objectName + " cannot be null");
    }
  }
}
