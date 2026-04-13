package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

// TODO split into separate test classes for each block type if needed, but for now they are similar
// enough to keep together
class MediaBlockTest {

  // ImageBlock

  @Test
  void image_constructor_setsTypeAndInitializes() {
    ImageBlock block = new ImageBlock();

    assertEquals("image", block.getType());
    assertNotNull(block.getImage());
  }

  // VideoBlock

  @Test
  void video_constructor_setsTypeAndInitializes() {
    VideoBlock block = new VideoBlock();

    assertEquals("video", block.getType());
    assertNotNull(block.getVideo());
  }

  // AudioBlock

  @Test
  void audio_constructor_setsTypeAndInitializes() {
    AudioBlock block = new AudioBlock();

    assertEquals("audio", block.getType());
    assertNotNull(block.getAudio());
  }

  // PdfBlock

  @Test
  void pdf_constructor_setsTypeAndInitializes() {
    PdfBlock block = new PdfBlock();

    assertEquals("pdf", block.getType());
    assertNotNull(block.getPdf());
  }

  // FileBlock

  @Test
  void file_constructor_setsTypeAndInitializes() {
    FileBlock block = new FileBlock();

    assertEquals("file", block.getType());
    assertNotNull(block.getFile());
  }
}
