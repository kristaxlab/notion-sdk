package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmbedBlockTest {

  @Test
  void constructor_setsTypeAndInitializesEmbed() {
    EmbedBlock block = new EmbedBlock();

    assertEquals("embed", block.getType());
    assertNotNull(block.getEmbed());
  }

  @Test
  void builder_withUrl() {
    EmbedBlock block = EmbedBlock.builder().url("https://maps.google.com").build();

    assertEquals("https://maps.google.com", block.getEmbed().getUrl());
  }

  @Test
  void builder_withUrlAndCaptionString() {
    EmbedBlock block = EmbedBlock.builder().url("https://youtube.com").caption("Video").build();

    assertEquals("https://youtube.com", block.getEmbed().getUrl());
    assertEquals(1, block.getEmbed().getCaption().size());
    assertEquals("Video", block.getEmbed().getCaption().get(0).getPlainText());
  }

  @Test
  void builder_noCaptionSet_captionIsNull() {
    EmbedBlock block = EmbedBlock.builder().url("https://example.com").build();

    assertNull(block.getEmbed().getCaption());
  }
}
