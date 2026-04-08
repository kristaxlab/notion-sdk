package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Test;

class ParagraphBlockTest {

  @Test
  void constructor_setsTypeAndInitializesParagraph() {
    ParagraphBlock block = new ParagraphBlock();

    assertEquals("paragraph", block.getType());
    assertNotNull(block.getParagraph());
  }

  @Test
  void ofString_setsRichTextFromPlainText() {
    ParagraphBlock block = ParagraphBlock.of("Hello world");

    assertEquals("paragraph", block.getType());
    assertNotNull(block.getParagraph().getRichText());
    assertEquals(1, block.getParagraph().getRichText().size());
    assertEquals("Hello world", block.getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  void ofStringColor_setsTextAndColor() {
    ParagraphBlock block = ParagraphBlock.of("Red text", Color.RED);

    assertEquals(1, block.getParagraph().getRichText().size());
    assertEquals("Red text", block.getParagraph().getRichText().get(0).getPlainText());
    assertEquals("red", block.getParagraph().getRichText().get(0).getAnnotations().getColor());
  }

  @Test
  void ofRichTextList_setsRichTextDirectly() {
    List<RichText> richTexts = RichText.of("Direct text");

    ParagraphBlock block = ParagraphBlock.of(richTexts);

    assertSame(richTexts, block.getParagraph().getRichText());
    assertNull(block.getParagraph().getColor());
  }

  @Test
  void ofRichTextListWithColor_setsRichTextAndBlockColor() {
    List<RichText> richTexts = RichText.of("Colored paragraph");

    ParagraphBlock block = ParagraphBlock.of(richTexts, Color.BLUE);

    assertSame(richTexts, block.getParagraph().getRichText());
    assertEquals("blue", block.getParagraph().getColor());
  }

  @Test
  void ofRichTextListWithNullColor_noBlockColorSet() {
    List<RichText> richTexts = RichText.of("No color");

    ParagraphBlock block = ParagraphBlock.of(richTexts, null);

    assertSame(richTexts, block.getParagraph().getRichText());
    assertNull(block.getParagraph().getColor());
  }

  @Test
  void builder_withText() {
    ParagraphBlock block = ParagraphBlock.builder().text("Builder text").build();

    assertEquals("paragraph", block.getType());
    assertEquals(1, block.getParagraph().getRichText().size());
    assertEquals("Builder text", block.getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withColor() {
    ParagraphBlock block = ParagraphBlock.builder().text("Colored").color(Color.GREEN).build();

    assertEquals("green", block.getParagraph().getColor());
  }

  @Test
  void builder_withStringColor() {
    ParagraphBlock block =
        ParagraphBlock.builder().text("Custom color").color("purple_background").build();

    assertEquals("purple_background", block.getParagraph().getColor());
  }

  @Test
  void builder_colorNull_throwsIllegalArgument() {
    ParagraphBlock.Builder builder = ParagraphBlock.builder().text("text");

    assertThrows(IllegalArgumentException.class, () -> builder.color((Color) null));
  }

  @Test
  void builder_withChildren() {
    ParagraphBlock block =
        ParagraphBlock.builder()
            .text("Parent")
            .children(c -> c.paragraph("Child 1").paragraph("Child 2"))
            .build();

    assertNotNull(block.getParagraph().getChildren());
    assertEquals(2, block.getParagraph().getChildren().size());
    assertInstanceOf(ParagraphBlock.class, block.getParagraph().getChildren().get(0));
  }

  @Test
  void builder_withChildrenList() {
    List<Block> children = List.of(ParagraphBlock.of("Child"));

    ParagraphBlock block = ParagraphBlock.builder().text("Parent").children(children).build();

    assertEquals(1, block.getParagraph().getChildren().size());
  }

  @Test
  void builder_withNullChildrenConsumer_noChildrenSet() {
    ParagraphBlock block =
        ParagraphBlock.builder().text("Text").children((java.util.function.Consumer) null).build();

    assertNull(block.getParagraph().getChildren());
  }

  @Test
  void builder_withRichTextConsumer() {
    ParagraphBlock block =
        ParagraphBlock.builder().richText(rt -> rt.text("Bold text").bold()).build();

    assertEquals(1, block.getParagraph().getRichText().size());
    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getBold());
  }

  @Test
  void builder_withNullRichTextConsumer_defaultsToEmptyRichText() {
    ParagraphBlock block =
        ParagraphBlock.builder()
            .richText((java.util.function.Consumer<RichText.Builder>) null)
            .build();

    assertNotNull(block.getParagraph().getRichText());
  }

  @Test
  void builder_withRichTextList() {
    List<RichText> richTexts = RichText.of("Direct");

    ParagraphBlock block = ParagraphBlock.builder().richText(richTexts).build();

    assertEquals("Direct", block.getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withIcon() {
    Icon icon = Icon.emoji("📝");

    ParagraphBlock block = ParagraphBlock.builder().text("With icon").icon(icon).build();

    assertNotNull(block.getParagraph().getIcon());
    assertEquals("📝", block.getParagraph().getIcon().getEmoji());
  }

  @Test
  void builder_noTextProvided_defaultsToEmptyRichText() {
    ParagraphBlock block = ParagraphBlock.builder().build();

    assertNotNull(block.getParagraph().getRichText());
    assertFalse(block.getParagraph().getRichText().isEmpty());
    assertEquals("", block.getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_annotations_bold() {
    ParagraphBlock block = ParagraphBlock.builder().text("Bold").bold().build();

    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getBold());
  }

  @Test
  void builder_annotations_italic() {
    ParagraphBlock block = ParagraphBlock.builder().text("Italic").italic().build();

    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getItalic());
  }

  @Test
  void builder_annotations_strikethrough() {
    ParagraphBlock block = ParagraphBlock.builder().text("Struck").strikethrough().build();

    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getStrikethrough());
  }

  @Test
  void builder_annotations_underline() {
    ParagraphBlock block = ParagraphBlock.builder().text("Underlined").underline().build();

    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getUnderline());
  }

  @Test
  void builder_annotations_code() {
    ParagraphBlock block = ParagraphBlock.builder().text("Code").code().build();

    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getCode());
  }

  @Test
  void builder_annotations_boldFalse() {
    ParagraphBlock block = ParagraphBlock.builder().text("Not bold").bold(false).build();

    assertFalse(block.getParagraph().getRichText().get(0).getAnnotations().getBold());
  }

  @Test
  void builder_annotations_italicFalse() {
    ParagraphBlock block = ParagraphBlock.builder().text("Not italic").italic(false).build();

    assertFalse(block.getParagraph().getRichText().get(0).getAnnotations().getItalic());
  }

  @Test
  void builder_annotations_multiple() {
    ParagraphBlock block =
        ParagraphBlock.builder().text("Formatted").bold().italic().underline().build();

    RichText.Annotations ann = block.getParagraph().getRichText().get(0).getAnnotations();
    assertTrue(ann.getBold());
    assertTrue(ann.getItalic());
    assertTrue(ann.getUnderline());
  }
}
