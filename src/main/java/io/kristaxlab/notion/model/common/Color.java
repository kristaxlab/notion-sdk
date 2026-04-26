package io.kristaxlab.notion.model.common;

/** Enumerates Notion text and background color tokens used by rich text and blocks. */
public enum Color {
  /** Default color (no specific color applied) */
  DEFAULT("default"),
  /** Default background color. */
  DEFAULT_BACKGROUND("default_background"),

  /** Gray color */
  GRAY("gray"),
  /** Gray background color. */
  GRAY_BACKGROUND("gray_background"),

  /** Brown color */
  BROWN("brown"),
  /** Brown background color. */
  BROWN_BACKGROUND("brown_background"),

  /** Orange color */
  ORANGE("orange"),
  /** Orange background color. */
  ORANGE_BACKGROUND("orange_background"),

  /** Yellow color */
  YELLOW("yellow"),
  /** Yellow background color. */
  YELLOW_BACKGROUND("yellow_background"),

  /** Green color */
  GREEN("green"),
  /** Green background color. */
  GREEN_BACKGROUND("green_background"),

  /** Blue color */
  BLUE("blue"),
  /** Blue background color. */
  BLUE_BACKGROUND("blue_background"),

  /** Purple color */
  PURPLE("purple"),
  /** Purple background color. */
  PURPLE_BACKGROUND("purple_background"),

  /** Pink color */
  PINK("pink"),
  /** Pink background color. */
  PINK_BACKGROUND("pink_background"),

  /** Red color */
  RED("red"),
  /** Red background color. */
  RED_BACKGROUND("red_background");

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
   * @param type the string value to look up (case-sensitive)
   * @return the Color enum constant with the matching value
   * @throws IllegalArgumentException if no Color constant is found with the given value
   */
  public static Color fromValue(String type) {
    for (Color color : Color.values()) {
      if (color.value.equals(type)) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color: " + type);
  }
}
