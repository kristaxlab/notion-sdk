package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes image")
  void constructor_setsTypeAndInitializesImage() {
    ImageBlock block = new ImageBlock();

    assertEquals("image", block.getType());
    assertNotNull(block.getImage());
  }
}
