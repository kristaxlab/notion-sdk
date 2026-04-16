package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AudioBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes audio")
  void constructor_setsTypeAndInitializesAudio() {
    AudioBlock block = new AudioBlock();

    assertEquals("audio", block.getType());
    assertNotNull(block.getAudio());
  }
}

