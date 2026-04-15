package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookmarkBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes bookmark")
  void constructor_setsTypeAndInitializesBookmark() {
    BookmarkBlock block = new BookmarkBlock();

    assertEquals("bookmark", block.getType());
    assertNotNull(block.getBookmark());
  }

  @Test
  @DisplayName("builder with url")
  void builder_withUrl() {
    BookmarkBlock block = BookmarkBlock.builder().url("https://example.com").build();

    assertEquals("https://example.com", block.getBookmark().getUrl());
  }

  @Test
  @DisplayName("builder with url and caption string")
  void builder_withUrlAndCaptionString() {
    BookmarkBlock block =
        BookmarkBlock.builder().url("https://example.com").caption("Example Site").build();

    assertEquals("https://example.com", block.getBookmark().getUrl());
    assertNotNull(block.getBookmark().getCaption());
    assertEquals(1, block.getBookmark().getCaption().size());
    assertEquals("Example Site", block.getBookmark().getCaption().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder with url and caption consumer")
  void builder_withUrlAndCaptionConsumer() {
    BookmarkBlock block =
        BookmarkBlock.builder()
            .url("https://example.com")
            .caption(c -> c.plainText("Example Site"))
            .build();

    assertEquals("https://example.com", block.getBookmark().getUrl());
    assertNotNull(block.getBookmark().getCaption());
    assertEquals(1, block.getBookmark().getCaption().size());
    assertEquals("Example Site", block.getBookmark().getCaption().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder no caption set caption is null")
  void builder_noCaptionSet_captionIsNull() {
    BookmarkBlock block = BookmarkBlock.builder().url("https://example.com").build();

    assertNull(block.getBookmark().getCaption());
  }
}
