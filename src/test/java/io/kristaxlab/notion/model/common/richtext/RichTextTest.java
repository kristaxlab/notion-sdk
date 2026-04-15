package io.kristaxlab.notion.model.common.richtext;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.Color;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RichTextTest {

  @Test
  @DisplayName("constructor initializes annotations")
  void constructor_initializesAnnotations() {
    RichText richText = new RichText();

    assertNotNull(richText.getAnnotations());
  }

  @Test
  @DisplayName("as list wraps current instance")
  void asList_wrapsCurrentInstance() {
    RichText richText = new RichText();

    List<RichText> list = richText.asList();

    assertEquals(1, list.size());
    assertSame(richText, list.get(0));
  }

  @Test
  @DisplayName("as list returns new list each call")
  void asList_returnsNewListEachCall() {
    RichText richText = new RichText();

    List<RichText> first = richText.asList();
    List<RichText> second = richText.asList();

    assertNotSame(first, second);
    first.add(new RichText());
    assertEquals(1, second.size());
  }

  @Test
  @DisplayName("bold without argument sets bold true and returns same instance")
  void bold_withoutArgument_setsBoldTrueAndReturnsSameInstance() {
    RichText richText = new RichText();

    RichText result = richText.bold();

    assertSame(richText, result);
    assertEquals(true, richText.getAnnotations().getBold());
  }

  @Test
  @DisplayName("bold with false sets bold false")
  void bold_withFalse_setsBoldFalse() {
    RichText richText = new RichText();

    richText.bold(false);

    assertEquals(false, richText.getAnnotations().getBold());
  }

  @Test
  @DisplayName("style methods recreate annotations when null")
  void styleMethods_recreateAnnotationsWhenNull() {
    RichText richText = new RichText();
    richText.setAnnotations(null);

    richText.italic().strikethrough().underline().code();

    assertNotNull(richText.getAnnotations());
    assertEquals(true, richText.getAnnotations().getItalic());
    assertEquals(true, richText.getAnnotations().getStrikethrough());
    assertEquals(true, richText.getAnnotations().getUnderline());
    assertEquals(true, richText.getAnnotations().getCode());
  }

  @Test
  @DisplayName("style boolean overloads set expected values")
  void styleBooleanOverloads_setExpectedValues() {
    RichText richText = new RichText();

    richText.italic(false).strikethrough(false).underline(false).code(false);

    assertEquals(false, richText.getAnnotations().getItalic());
    assertEquals(false, richText.getAnnotations().getStrikethrough());
    assertEquals(false, richText.getAnnotations().getUnderline());
    assertEquals(false, richText.getAnnotations().getCode());
  }

  @Test
  @DisplayName("color sets annotation color and returns same instance")
  void color_setsAnnotationColorAndReturnsSameInstance() {
    RichText richText = new RichText();

    RichText result = richText.color(Color.BLUE);

    assertSame(richText, result);
    assertEquals("blue", richText.getAnnotations().getColor());
  }

  @Test
  @DisplayName("color with null throws exception")
  void color_withNull_throwsException() {
    RichText richText = new RichText();

    assertThrows(NullPointerException.class, () -> richText.color(null));
  }

  @Test
  @DisplayName("color helpers set expected color values")
  void colorHelpers_setExpectedColorValues() {
    RichText richText = new RichText();

    assertEquals("default", richText.defaultColor().getAnnotations().getColor());
    assertEquals("gray", richText.gray().getAnnotations().getColor());
    assertEquals("gray_background", richText.grayBackground().getAnnotations().getColor());
    assertEquals("brown", richText.brown().getAnnotations().getColor());
    assertEquals("brown_background", richText.brownBackground().getAnnotations().getColor());
    assertEquals("orange", richText.orange().getAnnotations().getColor());
    assertEquals("orange_background", richText.orangeBackground().getAnnotations().getColor());
    assertEquals("yellow", richText.yellow().getAnnotations().getColor());
    assertEquals("yellow_background", richText.yellowBackground().getAnnotations().getColor());
    assertEquals("green", richText.green().getAnnotations().getColor());
    assertEquals("green_background", richText.greenBackground().getAnnotations().getColor());
    assertEquals("blue", richText.blue().getAnnotations().getColor());
    assertEquals("blue_background", richText.blueBackground().getAnnotations().getColor());
    assertEquals("purple", richText.purple().getAnnotations().getColor());
    assertEquals("purple_background", richText.purpleBackground().getAnnotations().getColor());
    assertEquals("pink", richText.pink().getAnnotations().getColor());
    assertEquals("pink_background", richText.pinkBackground().getAnnotations().getColor());
    assertEquals("red", richText.red().getAnnotations().getColor());
    assertEquals("red_background", richText.redBackground().getAnnotations().getColor());
  }
}
