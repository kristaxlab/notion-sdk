package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BookmarkBlockTest {

  @Test
  void constructor_setsTypeAndInitializesBookmark() {
    BookmarkBlock block = new BookmarkBlock();

    assertEquals("bookmark", block.getType());
    assertNotNull(block.getBookmark());
  }

  @Test
  void builder_withUrl() {
    BookmarkBlock block = BookmarkBlock.builder().url("https://example.com").build();

    assertEquals("https://example.com", block.getBookmark().getUrl());
  }

  @Test
  void builder_withUrlAndCaptionString() {
    BookmarkBlock block =
        BookmarkBlock.builder().url("https://example.com").caption("Example Site").build();

    assertEquals("https://example.com", block.getBookmark().getUrl());
    assertNotNull(block.getBookmark().getCaption());
    assertEquals(1, block.getBookmark().getCaption().size());
    assertEquals("Example Site", block.getBookmark().getCaption().get(0).getPlainText());
  }

  @Test
  void builder_noCaptionSet_captionIsNull() {
    BookmarkBlock block = BookmarkBlock.builder().url("https://example.com").build();

    assertNull(block.getBookmark().getCaption());
  }
}
