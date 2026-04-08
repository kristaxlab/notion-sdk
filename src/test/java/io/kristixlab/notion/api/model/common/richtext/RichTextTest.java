package io.kristixlab.notion.api.model.common.richtext;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class RichTextTest {

  // of(String) factory

  @Test
  void of_createsListWithSingleEntry() {
    List<RichText> list = RichText.of("Hello");

    assertNotNull(list);
    assertEquals(1, list.size());
  }

  @Test
  void of_setsPlainTextAndType() {
    List<RichText> list = RichText.of("Hello");
    RichText rt = list.get(0);

    assertEquals("Hello", rt.getPlainText());
    assertEquals("text", rt.getType());
  }

  @Test
  void of_setsTextContent() {
    List<RichText> list = RichText.of("World");
    RichText rt = list.get(0);

    assertNotNull(rt.getText());
    assertEquals("World", rt.getText().getContent());
  }

  @Test
  void of_emptyString() {
    List<RichText> list = RichText.of("");
    RichText rt = list.get(0);

    assertEquals("", rt.getPlainText());
    assertEquals("", rt.getText().getContent());
  }

  // Constructor

  @Test
  void constructor_initializesAnnotations() {
    RichText rt = new RichText();

    assertNotNull(rt.getAnnotations());
  }

  @Test
  void constructor_fieldsNullExceptAnnotations() {
    RichText rt = new RichText();

    assertNull(rt.getType());
    assertNull(rt.getPlainText());
    assertNull(rt.getHref());
    assertNull(rt.getText());
    assertNull(rt.getMention());
    assertNull(rt.getEquation());
    assertNotNull(rt.getAnnotations());
  }

  // Builder - text

  @Test
  void builder_text_createsSingleEntry() {
    RichText rt = RichText.builder().text("Simple").build();

    assertEquals("text", rt.getType());
    assertEquals("Simple", rt.getPlainText());
    assertNotNull(rt.getText());
    assertEquals("Simple", rt.getText().getContent());
  }

  @Test
  void builder_text_buildList() {
    List<RichText> list = RichText.builder().text("Item").buildList();

    assertEquals(1, list.size());
    assertEquals("Item", list.get(0).getPlainText());
  }

  @Test
  void builder_multipleTexts_buildList() {
    List<RichText> list = RichText.builder().text("First").text("Second").text("Third").buildList();

    assertEquals(3, list.size());
    assertEquals("First", list.get(0).getPlainText());
    assertEquals("Second", list.get(1).getPlainText());
    assertEquals("Third", list.get(2).getPlainText());
  }

  @Test
  void builder_multipleTexts_build_throwsException() {
    RichText.Builder builder = RichText.builder().text("A").text("B");

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("more than one"));
  }

  @Test
  void builder_empty_build_throwsException() {
    RichText.Builder builder = RichText.builder();

    assertThrows(IllegalStateException.class, builder::build);
  }

  @Test
  void builder_empty_buildList_returnsEmpty() {
    List<RichText> list = RichText.builder().buildList();

    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  // Builder - annotations

  @Test
  void builder_bold() {
    RichText rt = RichText.builder().text("Bold").bold().build();

    assertTrue(rt.getAnnotations().getBold());
  }

  @Test
  void builder_boldFalse() {
    RichText rt = RichText.builder().text("Not bold").bold(false).build();

    assertFalse(rt.getAnnotations().getBold());
  }

  @Test
  void builder_italic() {
    RichText rt = RichText.builder().text("Italic").italic().build();

    assertTrue(rt.getAnnotations().getItalic());
  }

  @Test
  void builder_italicFalse() {
    RichText rt = RichText.builder().text("Not italic").italic(false).build();

    assertFalse(rt.getAnnotations().getItalic());
  }

  @Test
  void builder_strikethrough() {
    RichText rt = RichText.builder().text("Struck").strikethrough().build();

    assertTrue(rt.getAnnotations().getStrikethrough());
  }

  @Test
  void builder_underline() {
    RichText rt = RichText.builder().text("Under").underline().build();

    assertTrue(rt.getAnnotations().getUnderline());
  }

  @Test
  void builder_code() {
    RichText rt = RichText.builder().text("Code").code().build();

    assertTrue(rt.getAnnotations().getCode());
  }

  @Test
  void builder_color_enum() {
    RichText rt = RichText.builder().text("Red").color(Color.RED).build();

    assertEquals("red", rt.getAnnotations().getColor());
  }

  @Test
  void builder_color_string() {
    RichText rt = RichText.builder().text("Custom").color("purple_background").build();

    assertEquals("purple_background", rt.getAnnotations().getColor());
  }

  @Test
  void builder_color_nullEnum_throwsIllegalArgument() {
    RichText.Builder builder = RichText.builder().text("text");

    assertThrows(IllegalArgumentException.class, () -> builder.color((Color) null));
  }

  @Test
  void builder_multipleAnnotations() {
    RichText rt = RichText.builder().text("Styled").bold().italic().underline().code().build();

    assertTrue(rt.getAnnotations().getBold());
    assertTrue(rt.getAnnotations().getItalic());
    assertTrue(rt.getAnnotations().getUnderline());
    assertTrue(rt.getAnnotations().getCode());
  }

  // Builder - annotation without text throws

  @Test
  void builder_boldWithoutText_throwsException() {
    RichText.Builder builder = RichText.builder();

    assertThrows(IllegalStateException.class, builder::bold);
  }

  // Builder - url

  @Test
  void builder_url() {
    RichText rt = RichText.builder().url("https://example.com").build();

    assertEquals("text", rt.getType());
    assertEquals("https://example.com", rt.getPlainText());
    assertEquals("https://example.com", rt.getHref());
    assertNotNull(rt.getText());
    assertNotNull(rt.getText().getLink());
    assertEquals("https://example.com", rt.getText().getLink().getUrl());
  }

  // Builder - expression

  @Test
  void builder_expression() {
    RichText rt = RichText.builder().expression("E=mc^2").build();

    assertEquals("equation", rt.getType());
    assertEquals("E=mc^2", rt.getPlainText());
    assertNotNull(rt.getEquation());
  }

  // Builder - mentions

  @Test
  void builder_dateMention_singleDate() {
    RichText rt = RichText.builder().dateMention("2026-01-01").build();

    assertEquals("mention", rt.getType());
    assertNotNull(rt.getMention());
    assertEquals("date", rt.getMention().getType());
    assertEquals("2026-01-01", rt.getMention().getDate().getStart());
  }

  @Test
  void builder_dateMention_dateRange() {
    RichText rt = RichText.builder().dateMention("2026-01-01", "2026-12-31").build();

    assertNotNull(rt.getMention().getDate());
    assertEquals("2026-01-01", rt.getMention().getDate().getStart());
    assertEquals("2026-12-31", rt.getMention().getDate().getEnd());
  }

  @Test
  void builder_dateMention_withTimezone() {
    RichText rt =
        RichText.builder().dateMention("2026-01-01", "2026-12-31", "America/New_York").build();

    assertEquals("America/New_York", rt.getMention().getDate().getTimeZone());
  }

  @Test
  void builder_userMention() {
    RichText rt = RichText.builder().userMention("user-abc").build();

    assertEquals("mention", rt.getType());
    assertNotNull(rt.getMention());
    assertEquals("user", rt.getMention().getType());
    assertNotNull(rt.getMention().getUser());
    assertEquals("user-abc", rt.getMention().getUser().getId());
  }

  @Test
  void builder_blockMention() {
    RichText rt = RichText.builder().blockMention("block-id").build();

    assertEquals("mention", rt.getType());
    assertNotNull(rt.getMention());
    assertNotNull(rt.getMention().getPage());
    assertEquals("block-id", rt.getMention().getPage().getId());
  }

  @Test
  void builder_customEmoji() {
    RichText rt = RichText.builder().customEmoji("emoji-id").build();

    assertEquals("mention", rt.getType());
    assertNotNull(rt.getMention());
    assertEquals("custom_emoji", rt.getMention().getType());
    assertNotNull(rt.getMention().getCustomEmoji());
    assertEquals("emoji-id", rt.getMention().getCustomEmoji().getId());
  }

  // Builder - mixed content

  @Test
  void builder_mixedContent() {
    List<RichText> list =
        RichText.builder()
            .text("Hello ")
            .text("World")
            .bold()
            .url("https://example.com")
            .buildList();

    assertEquals(3, list.size());
    assertEquals("Hello ", list.get(0).getPlainText());
    assertEquals("World", list.get(1).getPlainText());
    assertTrue(list.get(1).getAnnotations().getBold());
    assertEquals("https://example.com", list.get(2).getHref());
  }

  // Annotations inner class

  @Test
  void annotations_allFieldsNullByDefault() {
    RichText.Annotations ann = new RichText.Annotations();

    assertNull(ann.getBold());
    assertNull(ann.getItalic());
    assertNull(ann.getStrikethrough());
    assertNull(ann.getUnderline());
    assertNull(ann.getCode());
    assertNull(ann.getColor());
  }

  @Test
  void annotations_getterSetter() {
    RichText.Annotations ann = new RichText.Annotations();

    ann.setBold(true);
    ann.setItalic(true);
    ann.setStrikethrough(true);
    ann.setUnderline(true);
    ann.setCode(true);
    ann.setColor("red");

    assertTrue(ann.getBold());
    assertTrue(ann.getItalic());
    assertTrue(ann.getStrikethrough());
    assertTrue(ann.getUnderline());
    assertTrue(ann.getCode());
    assertEquals("red", ann.getColor());
  }
}
