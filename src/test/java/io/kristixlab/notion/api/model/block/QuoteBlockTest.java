package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import org.junit.jupiter.api.Test;

class QuoteBlockTest {

  @Test
  void constructor_setsTypeAndInitializesQuote() {
    QuoteBlock block = new QuoteBlock();

    assertEquals("quote", block.getType());
    assertNotNull(block.getQuote());
  }

  @Test
  void of_setsRichText() {
    QuoteBlock block = QuoteBlock.of("To be or not to be");

    assertEquals("quote", block.getType());
    assertEquals(1, block.getQuote().getRichText().size());
    assertEquals("To be or not to be", block.getQuote().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withText() {
    QuoteBlock block = QuoteBlock.builder().text("A wise quote").build();

    assertEquals("A wise quote", block.getQuote().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withColor() {
    QuoteBlock block = QuoteBlock.builder().text("Quote").color(Color.GRAY).build();

    assertEquals("gray", block.getQuote().getColor());
  }

  @Test
  void builder_withChildren() {
    QuoteBlock block =
        QuoteBlock.builder().text("Quote").children(c -> c.paragraph("Attribution")).build();

    assertNotNull(block.getQuote().getChildren());
    assertEquals(1, block.getQuote().getChildren().size());
  }

  @Test
  void builder_withAnnotations() {
    QuoteBlock block = QuoteBlock.builder().text("Fancy").italic().code().build();

    assertTrue(block.getQuote().getRichText().get(0).getAnnotations().getItalic());
    assertTrue(block.getQuote().getRichText().get(0).getAnnotations().getCode());
  }
}
