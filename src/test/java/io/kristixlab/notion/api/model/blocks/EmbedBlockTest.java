package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmbedBlockTest {

  @Test
  void constructor_setsTypeAndInitializesEmbed() {
    EmbedBlock block = new EmbedBlock();

    assertEquals("embed", block.getType());
    assertNotNull(block.getEmbed());
  }

  @Test
  void of_setsUrl() {
    EmbedBlock block = EmbedBlock.of("https://youtube.com/embed/abc");

    assertEquals("embed", block.getType());
    assertEquals("https://youtube.com/embed/abc", block.getEmbed().getUrl());
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
  void builder_withCaptionRichTextList() {
    List<RichText> caption = RichText.of("Embedded content");

    EmbedBlock block = EmbedBlock.builder().url("https://example.com").caption(caption).build();

    assertSame(caption, block.getEmbed().getCaption());
  }

  @Test
  void builder_noCaptionSet_captionIsNull() {
    EmbedBlock block = EmbedBlock.builder().url("https://example.com").build();

    assertNull(block.getEmbed().getCaption());
  }

  @Test
  void embed_getterSetter() {
    EmbedBlock.Embed embed = new EmbedBlock.Embed();

    assertNull(embed.getUrl());
    assertNull(embed.getCaption());

    embed.setUrl("https://test.com");
    embed.setCaption(RichText.of("Test"));

    assertEquals("https://test.com", embed.getUrl());
    assertEquals(1, embed.getCaption().size());
  }
}
