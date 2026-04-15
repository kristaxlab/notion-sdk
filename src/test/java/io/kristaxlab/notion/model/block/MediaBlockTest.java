package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// TODO split into separate test classes for each block type if needed, but for now they are similar
// enough to keep together
class MediaBlockTest {

  // ImageBlock

  @Test
  @DisplayName("image constructor sets type and initializes")
  void image_constructor_setsTypeAndInitializes() {
    ImageBlock block = new ImageBlock();

    assertEquals("image", block.getType());
    assertNotNull(block.getImage());
  }

  // VideoBlock

  @Test
  @DisplayName("video constructor sets type and initializes")
  void video_constructor_setsTypeAndInitializes() {
    VideoBlock block = new VideoBlock();

    assertEquals("video", block.getType());
    assertNotNull(block.getVideo());
  }

  // AudioBlock

  @Test
  @DisplayName("audio constructor sets type and initializes")
  void audio_constructor_setsTypeAndInitializes() {
    AudioBlock block = new AudioBlock();

    assertEquals("audio", block.getType());
    assertNotNull(block.getAudio());
  }

  // PdfBlock

  @Test
  @DisplayName("pdf constructor sets type and initializes")
  void pdf_constructor_setsTypeAndInitializes() {
    PdfBlock block = new PdfBlock();

    assertEquals("pdf", block.getType());
    assertNotNull(block.getPdf());
  }

  // FileBlock

  @Test
  @DisplayName("file constructor sets type and initializes")
  void file_constructor_setsTypeAndInitializes() {
    FileBlock block = new FileBlock();

    assertEquals("file", block.getType());
    assertNotNull(block.getFile());
  }
}
