package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Test;

class BookmarkBlockTest {

  @Test
  void constructor_setsTypeAndInitializesBookmark() {
    BookmarkBlock block = new BookmarkBlock();

    assertEquals("bookmark", block.getType());
    assertNotNull(block.getBookmark());
  }

  @Test
  void of_setsUrl() {
    BookmarkBlock block = BookmarkBlock.of("https://notion.so");

    assertEquals("bookmark", block.getType());
    assertEquals("https://notion.so", block.getBookmark().getUrl());
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
  void builder_withCaptionRichTextList() {
    List<RichText> caption = RichText.of("Rich caption");

    BookmarkBlock block =
        BookmarkBlock.builder().url("https://example.com").caption(caption).build();

    assertSame(caption, block.getBookmark().getCaption());
  }

  @Test
  void builder_noCaptionSet_captionIsNull() {
    BookmarkBlock block = BookmarkBlock.builder().url("https://example.com").build();

    assertNull(block.getBookmark().getCaption());
  }

  @Test
  void bookmark_getterSetter() {
    BookmarkBlock.Bookmark bookmark = new BookmarkBlock.Bookmark();

    assertNull(bookmark.getUrl());
    assertNull(bookmark.getCaption());

    bookmark.setUrl("https://test.com");
    bookmark.setCaption(RichText.of("Test"));

    assertEquals("https://test.com", bookmark.getUrl());
    assertEquals(1, bookmark.getCaption().size());
  }
}
