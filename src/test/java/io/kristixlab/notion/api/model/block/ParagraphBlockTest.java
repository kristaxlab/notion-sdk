package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.helper.NotionBlocks;
import io.kristixlab.notion.api.model.helper.NotionBlocksBuilder;
import java.util.List;
import java.util.function.Consumer;
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
    ParagraphBlock block = NotionBlocks.paragraph("Hello world");

    assertEquals("paragraph", block.getType());
    assertNotNull(block.getParagraph().getRichText());
    assertEquals(1, block.getParagraph().getRichText().size());
    assertEquals("Hello world", block.getParagraph().getRichText().get(0).getPlainText());
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
    ParagraphBlock block = ParagraphBlock.builder().text("Colored").blockColor(Color.GREEN).build();

    assertEquals("green", block.getParagraph().getColor());
  }

  @Test
  void builder_withStringColor() {
    ParagraphBlock block =
        ParagraphBlock.builder().text("Custom color").blockColor("purple_background").build();

    assertEquals("purple_background", block.getParagraph().getColor());
  }

  @Test
  void builder_colorNull_throwsIllegalArgument() {
    ParagraphBlock.Builder builder = ParagraphBlock.builder().text("text");

    assertThrows(IllegalArgumentException.class, () -> builder.blockColor((Color) null));
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
    List<Block> children = List.of(ParagraphBlock.builder().text("Child").build());

    ParagraphBlock block = ParagraphBlock.builder().text("Parent").children(children).build();

    assertEquals(1, block.getParagraph().getChildren().size());
  }

  @Test
  void builder_withNullChildrenConsumer_noChildrenSet() {
    ParagraphBlock block =
        ParagraphBlock.builder()
            .text("Text")
            .children((Consumer<NotionBlocksBuilder>) null)
            .build();

    assertNull(block.getParagraph().getChildren());
  }

  @Test
  void builder_withRichTextConsumer() {
    ParagraphBlock block =
        ParagraphBlock.builder().text(rt -> rt.plainText("Bold text").bold()).build();

    assertEquals(1, block.getParagraph().getRichText().size());
    assertTrue(block.getParagraph().getRichText().get(0).getAnnotations().getBold());
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
}
