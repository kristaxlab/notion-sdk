package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PdfBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes pdf")
  void constructor_setsTypeAndInitializesPdf() {
    PdfBlock block = new PdfBlock();

    assertEquals("pdf", block.getType());
    assertNotNull(block.getPdf());
  }
}
