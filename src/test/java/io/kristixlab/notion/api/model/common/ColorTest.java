package io.kristixlab.notion.api.model.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ColorTest {

  @Test
  void getValue_returnsApiString() {
    assertEquals("default", Color.DEFAULT.getValue());
    assertEquals("gray", Color.GRAY.getValue());
    assertEquals("gray_background", Color.GRAY_BACKGROUND.getValue());
    assertEquals("red", Color.RED.getValue());
    assertEquals("red_background", Color.RED_BACKGROUND.getValue());
    assertEquals("blue", Color.BLUE.getValue());
    assertEquals("blue_background", Color.BLUE_BACKGROUND.getValue());
    assertEquals("green", Color.GREEN.getValue());
    assertEquals("purple", Color.PURPLE.getValue());
    assertEquals("pink", Color.PINK.getValue());
    assertEquals("yellow", Color.YELLOW.getValue());
    assertEquals("orange", Color.ORANGE.getValue());
    assertEquals("brown", Color.BROWN.getValue());
  }

  @Test
  void fromValue_knownColors() {
    assertEquals(Color.DEFAULT, Color.fromValue("default"));
    assertEquals(Color.GRAY, Color.fromValue("gray"));
    assertEquals(Color.GRAY_BACKGROUND, Color.fromValue("gray_background"));
    assertEquals(Color.RED, Color.fromValue("red"));
    assertEquals(Color.RED_BACKGROUND, Color.fromValue("red_background"));
    assertEquals(Color.BLUE, Color.fromValue("blue"));
    assertEquals(Color.GREEN, Color.fromValue("green"));
    assertEquals(Color.PURPLE, Color.fromValue("purple"));
    assertEquals(Color.PINK, Color.fromValue("pink"));
    assertEquals(Color.YELLOW, Color.fromValue("yellow"));
    assertEquals(Color.ORANGE, Color.fromValue("orange"));
    assertEquals(Color.BROWN, Color.fromValue("brown"));
  }

  @Test
  void fromValue_backgroundColors() {
    assertEquals(Color.DEFAULT_BACKGROUND, Color.fromValue("default_background"));
    assertEquals(Color.BROWN_BACKGROUND, Color.fromValue("brown_background"));
    assertEquals(Color.ORANGE_BACKGROUND, Color.fromValue("orange_background"));
    assertEquals(Color.YELLOW_BACKGROUND, Color.fromValue("yellow_background"));
    assertEquals(Color.GREEN_BACKGROUND, Color.fromValue("green_background"));
    assertEquals(Color.BLUE_BACKGROUND, Color.fromValue("blue_background"));
    assertEquals(Color.PURPLE_BACKGROUND, Color.fromValue("purple_background"));
    assertEquals(Color.PINK_BACKGROUND, Color.fromValue("pink_background"));
  }

  @Test
  void fromValue_unknownColor_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Color.fromValue("neon_green"));
  }

  @Test
  void fromValue_null_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> Color.fromValue(null));
  }

  @Test
  void fromValue_caseSensitive() {
    assertThrows(IllegalArgumentException.class, () -> Color.fromValue("RED"));
    assertThrows(IllegalArgumentException.class, () -> Color.fromValue("Gray"));
  }

  @Test
  void allEnumValues_haveNonNullValue() {
    for (Color color : Color.values()) {
      assertNotNull(color.getValue());
      assertFalse(color.getValue().isEmpty());
    }
  }

  @Test
  void roundTrip_allColors() {
    for (Color color : Color.values()) {
      assertEquals(color, Color.fromValue(color.getValue()));
    }
  }
}
