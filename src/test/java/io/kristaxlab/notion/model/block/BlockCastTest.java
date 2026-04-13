package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.helper.NotionBlocks;
import org.junit.jupiter.api.Test;

class BlockCastTest {

  @Test
  void asParagraph_success() {
    Block block = NotionBlocks.paragraph("text");
    ParagraphBlock result = block.asParagraph();
    assertNotNull(result);
  }

  @Test
  void asParagraph_wrongType_throwsClassCast() {
    Block block = new DividerBlock();
    assertThrows(ClassCastException.class, block::asParagraph);
  }

  @Test
  void asBreadcrumb_success() {
    Block block = new BreadcrumbBlock();
    assertNotNull(block.asBreadcrumb());
  }

  @Test
  void asHeadingOne_success() {
    Block block = NotionBlocks.heading1("H1");
    assertNotNull(block.asHeadingOne());
  }

  @Test
  void asHeadingTwo_success() {
    Block block = NotionBlocks.heading2("H2");
    assertNotNull(block.asHeadingTwo());
  }

  @Test
  void asHeadingThree_success() {
    Block block = NotionBlocks.heading3("H3");
    assertNotNull(block.asHeadingThree());
  }

  @Test
  void asToDo_success() {
    Block block = NotionBlocks.todo("Task");
    assertNotNull(block.asToDo());
  }

  @Test
  void asBulletedListItem_success() {
    Block block = NotionBlocks.bullet("Item");
    assertNotNull(block.asBulletedListItem());
  }

  @Test
  void asNumberedListItem_success() {
    Block block = NotionBlocks.numbered("Step");
    assertNotNull(block.asNumberedListItem());
  }

  @Test
  void asQuote_success() {
    Block block = NotionBlocks.quote("Quote");
    assertNotNull(block.asQuote());
  }

  @Test
  void asCallout_success() {
    Block block = NotionBlocks.callout("⚠️", "Warning");
    assertNotNull(block.asCallout());
  }

  @Test
  void asCode_success() {
    Block block = NotionBlocks.code("java", "System.out.println(\"Hello\");");
    assertNotNull(block.asCode());
  }

  @Test
  void asToggle_success() {
    Block block = NotionBlocks.toggle("Toggle");
    assertNotNull(block.asToggle());
  }

  @Test
  void asDivider_success() {
    Block block = new DividerBlock();
    assertNotNull(block.asDivider());
  }

  @Test
  void asColumn_success() {
    Block block = new ColumnBlock();
    assertNotNull(block.asColumn());
  }

  @Test
  void asColumnList_success() {
    Block block = new ColumnListBlock();
    assertNotNull(block.asColumnList());
  }

  @Test
  void asBookmark_success() {
    Block block = NotionBlocks.bookmark("https://example.com");
    assertNotNull(block.asBookmark());
  }

  @Test
  void asEmbed_success() {
    Block block = NotionBlocks.embed("https://youtube.com");
    assertNotNull(block.asEmbed());
  }

  @Test
  void asTable_success() {
    Block block = new TableBlock();
    assertNotNull(block.asTable());
  }

  @Test
  void asTableRow_success() {
    Block block = new TableRowBlock();
    assertNotNull(block.asTableRow());
  }

  @Test
  void asTableOfContents_success() {
    Block block = new TableOfContentsBlock();
    assertNotNull(block.asTableOfContents());
  }

  @Test
  void asLinkToPage_success() {
    Block block = NotionBlocks.linkToPage("id");
    assertNotNull(block.asLinkToPage());
  }

  @Test
  void asSynced_success() {
    Block block = new SyncedBlock();
    assertNotNull(block.asSynced());
  }

  @Test
  void asEquation_success() {
    Block block = NotionBlocks.equation("x^2");
    assertNotNull(block.asEquation());
  }

  @Test
  void asUnknown_success() {
    Block block = new UnknownBlock();
    assertNotNull(block.asUnknown());
  }

  @Test
  void asUnsupported_success() {
    Block block = new UnsupportedBlock();
    assertNotNull(block.asUnsupported());
  }

  @Test
  void block_typeGetterSetter() {
    Block block = new Block();
    assertNull(block.getType());

    block.setType("test_type");
    assertEquals("test_type", block.getType());
  }

  @Test
  void block_hasChildrenGetterSetter() {
    Block block = new Block();
    assertNull(block.getHasChildren());

    block.setHasChildren(true);
    assertTrue(block.getHasChildren());
  }
}
