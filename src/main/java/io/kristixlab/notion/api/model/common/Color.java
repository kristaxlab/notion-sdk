package io.kristixlab.notion.api.model.common;

/**
 * Enumeration representing the available colors in the Notion API.
 *
 * <p>This enum defines all the color options that can be used for various Notion elements such as
 * text formatting, backgrounds, and other UI components. Each color constant is associated with its
 * corresponding string value as used in the Notion API.
 *
 * @author KristaxLab
 * @since 1.0
 */
public enum Color {
  /** Default color (no specific color applied) */
  DEFAULT("default"),

  /** Gray color */
  GRAY("gray"),

  /** Brown color */
  BROWN("brown"),

  /** Orange color */
  ORANGE("orange"),

  /** Yellow color */
  YELLOW("yellow"),

  /** Green color */
  GREEN("green"),

  /** Blue color */
  BLUE("blue"),

  /** Purple color */
  PURPLE("purple"),

  /** Pink color */
  PINK("pink"),

  /** Red color */
  RED("red");

  /** The string value representing this color in the Notion API */
  private final String value;

  /**
   * Constructs a Color enum constant with the specified string value.
   *
   * @param value the string representation of the color as used in the Notion API
   */
  Color(String value) {
    this.value = value;
  }

  /**
   * Returns the string value of this color as used in the Notion API.
   *
   * @return the string representation of this color
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns the Color enum constant corresponding to the given string value.
   *
   * <p>This method performs a case-sensitive search through all enum constants to find the one with
   * a matching value.
   *
   * @param value the string value to look up (case-sensitive)
   * @return the Color enum constant with the matching value
   * @throws IllegalArgumentException if no Color constant is found with the given value
   */
  public Color fromValue(String value) {
    for (Color color : Color.values()) {
      if (color.value.equals(value)) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color: " + value);
  }
}
