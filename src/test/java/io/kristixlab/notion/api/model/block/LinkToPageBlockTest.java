package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LinkToPageBlockTest {

  @Test
  void constructor_setsTypeAndInitializes() {
    LinkToPageBlock block = new LinkToPageBlock();

    assertEquals("link_to_page", block.getType());
    assertNotNull(block.getLinkToPage());
  }

  @Test
  void pageLink_setsTypeAndPageId() {
    LinkToPageBlock block = LinkToPageBlock.pageLink("page-abc-123");

    assertEquals("link_to_page", block.getType());
    assertEquals("page_id", block.getLinkToPage().getType());
    assertEquals("page-abc-123", block.getLinkToPage().getPageId());
    assertNull(block.getLinkToPage().getDatabaseId());
    assertNull(block.getLinkToPage().getCommentId());
  }

  @Test
  void databaseLink_setsTypeAndDatabaseId() {
    LinkToPageBlock block = LinkToPageBlock.databaseLink("db-xyz-456");

    assertEquals("link_to_page", block.getType());
    assertEquals("database_id", block.getLinkToPage().getType());
    assertEquals("db-xyz-456", block.getLinkToPage().getDatabaseId());
    assertNull(block.getLinkToPage().getPageId());
    assertNull(block.getLinkToPage().getCommentId());
  }

  @Test
  void commentLink_setsTypeAndCommentId() {
    LinkToPageBlock block = LinkToPageBlock.commentLink("comment-789");

    assertEquals("link_to_page", block.getType());
    assertEquals("comment_id", block.getLinkToPage().getType());
    assertEquals("comment-789", block.getLinkToPage().getCommentId());
    assertNull(block.getLinkToPage().getPageId());
    assertNull(block.getLinkToPage().getDatabaseId());
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
