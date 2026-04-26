package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VideoBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes video")
  void constructor_setsTypeAndInitializesVideo() {
    VideoBlock block = new VideoBlock();

    assertEquals("video", block.getType());
    assertNotNull(block.getVideo());
  }
}
