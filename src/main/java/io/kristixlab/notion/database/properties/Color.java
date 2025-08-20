package io.kristixlab.notion.database.properties;

public enum Color {
  DEFAULT("default"),
  GRAY("gray"),
  BROWN("brown"),
  ORANGE("orange"),
  YELLOW("yellow"),
  GREEN("green"),
  BLUE("blue"),
  PURPLE("purple"),
  PINK("pink"),
  RED("red");

  private final String value;

  Color(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public Color fromValue(String value) {
    for (Color color : Color.values()) {
      if (color.value.equals(value)) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color: " + value);
  }
}
