package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.fluent.NotionBlocksBuilder;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.Icon;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParagraphBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes paragraph")
  void constructor_setsTypeAndInitializesParagraph() {
    ParagraphBlock block = new ParagraphBlock();

    assertEquals("paragraph", block.getType());
    assertNotNull(block.getParagraph());
  }

  @Test
  @DisplayName("of string sets rich text from plain text")
  void ofString_setsRichTextFromPlainText() {
    ParagraphBlock block = NotionBlocks.paragraph("Hello world");

    assertEquals("paragraph", block.getType());
    assertNotNull(block.getParagraph().getRichText());
    assertEquals(1, block.getParagraph().getRichText().size());
    assertEquals("Hello world", block.getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder with text")
  void builder_withText() {
    ParagraphBlock block = ParagraphBlock.builder().text("Builder text").build();

    assertEquals("paragraph", block.getType());
    assertEquals(1, block.getParagraph().getRichText().size());
    assertEquals("Builder text", block.getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder with color")
  void builder_withColor() {
    ParagraphBlock block = ParagraphBlock.builder().text("Colored").blockColor(Color.GREEN).build();

    assertEquals("green", block.getParagraph().getColor());
  }

  @Test
  @DisplayName("builder with string color")
  void builder_withStringColor() {
    ParagraphBlock block =
        ParagraphBlock.builder().text("Custom color").blockColor("purple_background").build();

    assertEquals("purple_background", block.getParagraph().getColor());
  }

  @Test
  @DisplayName("builder color null throws illegal argument")
  void builder_colorNull_throwsIllegalArgument() {
    ParagraphBlock.Builder builder = ParagraphBlock.builder().text("text");

    assertThrows(IllegalArgumentException.class, () -> builder.blockColor((Color) null));
  }

  @Test
  @DisplayName("builder with children")
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
  @DisplayName("builder with children list")
  void builder_withChildrenList() {
    List<Block> children = List.of(ParagraphBlock.builder().text("Child").build());

    ParagraphBlock block = ParagraphBlock.builder().text("Parent").children(children).build();

    assertEquals(1, block.getParagraph().getChildren().size());
  }

  @Test
  @DisplayName("builder with null children consumer no children set")
  void builder_withNullChildrenConsumer_noChildrenSet() {
    ParagraphBlock block =
        ParagraphBlock.builder()
            .text("Text")
            .children((Consumer<NotionBlocksBuilder>) null)
            .build();

    assertNull(block.getParagraph().getChildren());
  }

  @Test
  @DisplayName("builder with rich text consumer")
  void builder_withRichTextConsumer() {
    ParagraphBlock block =
        ParagraphBlock.builder().text(rt -> rt.plainText("Bold text").bold()).build();

    assertEquals(1, block.getParagraph().getRichText().size());
    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getBold());
  }

  @Test
  @DisplayName("builder with icon")
  void builder_withIcon() {
    Icon icon = Icon.emoji("📝");

    ParagraphBlock block = ParagraphBlock.builder().text("With icon").icon(icon).build();

    assertNotNull(block.getParagraph().getIcon());
    assertEquals("📝", block.getParagraph().getIcon().getEmoji());
  }

  @Test
  @DisplayName("builder no text provided defaults to empty rich text")
  void builder_noTextProvided_defaultsToEmptyRichText() {
    ParagraphBlock block = ParagraphBlock.builder().build();

    assertNotNull(block.getParagraph().getRichText());
    assertFalse(block.getParagraph().getRichText().isEmpty());
    assertEquals("", block.getParagraph().getRichText().get(0).getPlainText());
  }
}
