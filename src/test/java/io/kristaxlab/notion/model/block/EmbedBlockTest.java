package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmbedBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes embed")
  void constructor_setsTypeAndInitializesEmbed() {
    EmbedBlock block = new EmbedBlock();

    assertEquals("embed", block.getType());
    assertNotNull(block.getEmbed());
  }

  @Test
  @DisplayName("builder with url")
  void builder_withUrl() {
    EmbedBlock block = EmbedBlock.builder().url("https://maps.google.com").build();

    assertEquals("https://maps.google.com", block.getEmbed().getUrl());
  }

  @Test
  @DisplayName("builder with url and caption string")
  void builder_withUrlAndCaptionString() {
    EmbedBlock block = EmbedBlock.builder().url("https://youtube.com").caption("Video").build();

    assertEquals("https://youtube.com", block.getEmbed().getUrl());
    assertEquals(1, block.getEmbed().getCaption().size());
    assertEquals("Video", block.getEmbed().getCaption().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder no caption set caption is null")
  void builder_noCaptionSet_captionIsNull() {
    EmbedBlock block = EmbedBlock.builder().url("https://example.com").build();

    assertNull(block.getEmbed().getCaption());
  }
}
