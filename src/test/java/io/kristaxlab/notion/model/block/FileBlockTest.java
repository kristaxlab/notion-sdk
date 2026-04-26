package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes file")
  void constructor_setsTypeAndInitializesFile() {
    FileBlock block = new FileBlock();

    assertEquals("file", block.getType());
    assertNotNull(block.getFile());
  }
}
