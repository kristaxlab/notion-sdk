package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlockCastTest {

  @Test
  @DisplayName("as paragraph success")
  void asParagraph_success() {
    Block block = NotionBlocks.paragraph("text");
    ParagraphBlock result = block.asParagraph();
    assertNotNull(result);
  }

  @Test
  @DisplayName("as paragraph wrong type throws class cast")
  void asParagraph_wrongType_throwsClassCast() {
    Block block = new DividerBlock();
    assertThrows(ClassCastException.class, block::asParagraph);
  }

  @Test
  @DisplayName("as breadcrumb success")
  void asBreadcrumb_success() {
    Block block = new BreadcrumbBlock();
    assertNotNull(block.asBreadcrumb());
  }

  @Test
  @DisplayName("as heading one success")
  void asHeadingOne_success() {
    Block block = NotionBlocks.heading1("H1");
    assertNotNull(block.asHeadingOne());
  }

  @Test
  @DisplayName("as heading two success")
  void asHeadingTwo_success() {
    Block block = NotionBlocks.heading2("H2");
    assertNotNull(block.asHeadingTwo());
  }

  @Test
  @DisplayName("as heading three success")
  void asHeadingThree_success() {
    Block block = NotionBlocks.heading3("H3");
    assertNotNull(block.asHeadingThree());
  }

  @Test
  @DisplayName("as to do success")
  void asToDo_success() {
    Block block = NotionBlocks.todo("Task");
    assertNotNull(block.asToDo());
  }

  @Test
  @DisplayName("as bulleted list item success")
  void asBulletedListItem_success() {
    Block block = NotionBlocks.bullet("Item");
    assertNotNull(block.asBulletedListItem());
  }

  @Test
  @DisplayName("as numbered list item success")
  void asNumberedListItem_success() {
    Block block = NotionBlocks.numbered("Step");
    assertNotNull(block.asNumberedListItem());
  }

  @Test
  @DisplayName("as quote success")
  void asQuote_success() {
    Block block = NotionBlocks.quote("Quote");
    assertNotNull(block.asQuote());
  }

  @Test
  @DisplayName("as callout success")
  void asCallout_success() {
    Block block = NotionBlocks.callout("⚠️", "Warning");
    assertNotNull(block.asCallout());
  }

  @Test
  @DisplayName("as code success")
  void asCode_success() {
    Block block = NotionBlocks.code("java", "System.out.println(\"Hello\");");
    assertNotNull(block.asCode());
  }

  @Test
  @DisplayName("as toggle success")
  void asToggle_success() {
    Block block = NotionBlocks.toggle("Toggle");
    assertNotNull(block.asToggle());
  }

  @Test
  @DisplayName("as divider success")
  void asDivider_success() {
    Block block = new DividerBlock();
    assertNotNull(block.asDivider());
  }

  @Test
  @DisplayName("as column success")
  void asColumn_success() {
    Block block = new ColumnBlock();
    assertNotNull(block.asColumn());
  }

  @Test
  @DisplayName("as column list success")
  void asColumnList_success() {
    Block block = new ColumnListBlock();
    assertNotNull(block.asColumnList());
  }

  @Test
  @DisplayName("as bookmark success")
  void asBookmark_success() {
    Block block = NotionBlocks.bookmark("https://example.com");
    assertNotNull(block.asBookmark());
  }

  @Test
  @DisplayName("as embed success")
  void asEmbed_success() {
    Block block = NotionBlocks.embed("https://youtube.com");
    assertNotNull(block.asEmbed());
  }

  @Test
  @DisplayName("as table success")
  void asTable_success() {
    Block block = new TableBlock();
    assertNotNull(block.asTable());
  }

  @Test
  @DisplayName("as table row success")
  void asTableRow_success() {
    Block block = new TableRowBlock();
    assertNotNull(block.asTableRow());
  }

  @Test
  @DisplayName("as table of contents success")
  void asTableOfContents_success() {
    Block block = new TableOfContentsBlock();
    assertNotNull(block.asTableOfContents());
  }

  @Test
  @DisplayName("as link to page success")
  void asLinkToPage_success() {
    Block block = NotionBlocks.linkToPage("id");
    assertNotNull(block.asLinkToPage());
  }

  @Test
  @DisplayName("as synced success")
  void asSynced_success() {
    Block block = new SyncedBlock();
    assertNotNull(block.asSynced());
  }

  @Test
  @DisplayName("as equation success")
  void asEquation_success() {
    Block block = NotionBlocks.equation("x^2");
    assertNotNull(block.asEquation());
  }

  @Test
  @DisplayName("as unknown success")
  void asUnknown_success() {
    Block block = new UnknownBlock();
    assertNotNull(block.asUnknown());
  }

  @Test
  @DisplayName("as unsupported success")
  void asUnsupported_success() {
    Block block = new UnsupportedBlock();
    assertNotNull(block.asUnsupported());
  }

  @Test
  @DisplayName("block type getter setter")
  void block_typeGetterSetter() {
    Block block = new Block();
    assertNull(block.getType());

    block.setType("test_type");
    assertEquals("test_type", block.getType());
  }

  @Test
  @DisplayName("block has children getter setter")
  void block_hasChildrenGetterSetter() {
    Block block = new Block();
    assertNull(block.getHasChildren());

    block.setHasChildren(true);
    assertTrue(block.getHasChildren());
  }
}
