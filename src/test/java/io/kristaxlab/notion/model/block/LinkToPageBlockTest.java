package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LinkToPageBlockTest {

  @Test
  void constructor_setsTypeAndInitializes() {
    LinkToPageBlock block = new LinkToPageBlock();

    assertEquals("link_to_page", block.getType());
    assertNotNull(block.getLinkToPage());
  }

  // LinkToPage inner class

  @Test
  void linkToPage_getterSetter() {
    LinkToPageBlock.LinkToPage ltp = new LinkToPageBlock.LinkToPage();

    assertNull(ltp.getType());
    assertNull(ltp.getPageId());
    assertNull(ltp.getDatabaseId());
    assertNull(ltp.getCommentId());

    ltp.setType("page_id");
    ltp.setPageId("p1");
    ltp.setDatabaseId("d1");
    ltp.setCommentId("c1");

    assertEquals("page_id", ltp.getType());
    assertEquals("p1", ltp.getPageId());
    assertEquals("d1", ltp.getDatabaseId());
    assertEquals("c1", ltp.getCommentId());
  }
}
