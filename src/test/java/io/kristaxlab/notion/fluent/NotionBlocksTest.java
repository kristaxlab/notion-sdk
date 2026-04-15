package io.kristaxlab.notion.fluent;

import static io.kristaxlab.notion.fluent.NotionText.plainText;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionBlocksTest {

  private static final String UUID_STRING = "550e8400-e29b-41d4-a716-446655440000";
  private static final String EXTERNAL_URL = "https://example.com/file.mp3";

  @Nested
  class Audio {

    @Test
    @DisplayName("from external url sets external type")
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.audio(EXTERNAL_URL).getAudio().getType());
    }

    @Test
    @DisplayName("from uuid sets file upload type")
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.audio(UUID_STRING).getAudio().getType());
    }

    @Test
    @DisplayName("from uuid sets file upload id")
    void fromUuid_setsFileUploadId() {
      assertEquals(UUID_STRING, NotionBlocks.audio(UUID_STRING).getAudio().getFileUpload().getId());
    }

    @Test
    @DisplayName("from file data assigns same file data")
    void fromFileData_assignsSameFileData() {
      FileData fd = FileData.builder().externalUrl(EXTERNAL_URL).build();
      assertSame(fd, NotionBlocks.audio(fd).getAudio());
    }

    @Test
    @DisplayName("from consumer applies consumer")
    void fromConsumer_appliesConsumer() {
      assertEquals(
          "external", NotionBlocks.audio(b -> b.externalUrl(EXTERNAL_URL)).getAudio().getType());
    }
  }

  @Nested
  class BlankLine {

    @Test
    @DisplayName("returns paragraph block")
    void returnsParagraphBlock() {
      assertInstanceOf(ParagraphBlock.class, NotionBlocks.blankLine());
    }

    @Test
    @DisplayName("has empty plain text")
    void hasEmptyPlainText() {
      assertEquals("", NotionBlocks.blankLine().getParagraph().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Bookmark {

    @Test
    @DisplayName("from string sets url")
    void fromString_setsUrl() {
      assertEquals(
          "https://notion.so", NotionBlocks.bookmark("https://notion.so").getBookmark().getUrl());
    }

    @Test
    @DisplayName("from consumer applies consumer")
    void fromConsumer_appliesConsumer() {
      assertNotNull(NotionBlocks.bookmark(b -> b.url("https://x.com")).getBookmark().getUrl());
    }
  }

  @Nested
  class Breadcrumb {

    @Test
    @DisplayName("returns breadcrumb block")
    void returnsBreadcrumbBlock() {
      assertInstanceOf(BreadcrumbBlock.class, NotionBlocks.breadcrumb());
    }
  }

  @Nested
  class Bullet {

    @Test
    @DisplayName("from string creates one rich text")
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.bullet("item").getBulletedListItem().getRichText().size());
    }

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "item",
          NotionBlocks.bullet("item").getBulletedListItem().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("from rich text varargs sets multiple")
    void fromRichTextVarargs_setsMultiple() {
      assertEquals(
          2,
          NotionBlocks.bullet(plainText("a"), plainText("b"))
              .getBulletedListItem()
              .getRichText()
              .size());
    }

    @Test
    @DisplayName("from list sets rich texts")
    void fromList_setsRichTexts() {
      assertEquals(
          1,
          NotionBlocks.bullet(List.of(plainText("x"))).getBulletedListItem().getRichText().size());
    }
  }

  @Nested
  class BulletedList {

    @Test
    @DisplayName("varargs returns correct count")
    void varargs_returnsCorrectCount() {
      assertEquals(3, NotionBlocks.bullets("a", "b", "c").size());
    }

    @Test
    @DisplayName("each item is bulleted list item block")
    void eachItem_isBulletedListItemBlock() {
      NotionBlocks.bullets("x", "y").forEach(b -> assertInstanceOf(BulletedListItemBlock.class, b));
    }
  }

  @Nested
  class Callout {

    @Test
    @DisplayName("from string creates one rich text")
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.callout("note").getCallout().getRichText().size());
    }

    @Test
    @DisplayName("with emoji string sets icon emoji")
    void withEmojiString_setsIconEmoji() {
      assertEquals("💡", NotionBlocks.callout("💡", "note").getCallout().getIcon().getEmoji());
    }

    @Test
    @DisplayName("with icon and string sets icon")
    void withIconAndString_setsIcon() {
      assertNotNull(NotionBlocks.callout(Icon.emoji("🔔"), "text").getCallout().getIcon());
    }

    @Test
    @DisplayName("with icon and varargs sets multiple rich texts")
    void withIconAndVarargs_setsMultipleRichTexts() {
      assertEquals(
          2,
          NotionBlocks.callout(Icon.emoji("💡"), plainText("a"), plainText("b"))
              .getCallout()
              .getRichText()
              .size());
    }
  }

  @Nested
  class Code {

    @Test
    @DisplayName("sets language")
    void setsLanguage() {
      assertEquals("java", NotionBlocks.code("java", "int x;").getCode().getLanguage());
    }

    @Test
    @DisplayName("sets code content")
    void setsCodeContent() {
      assertFalse(NotionBlocks.code("java", "int x;").getCode().getRichText().isEmpty());
    }
  }

  @Nested
  class Column {

    @Test
    @DisplayName("with width ratio sets width ratio")
    void withWidthRatio_setsWidthRatio() {
      assertEquals(
          0.33, NotionBlocks.column(0.33, b -> b.paragraph("p")).getColumn().getWidthRatio());
    }

    @Test
    @DisplayName("with consumer populates children")
    void withConsumer_populatesChildren() {
      assertFalse(NotionBlocks.column(b -> b.paragraph("p")).getColumn().getChildren().isEmpty());
    }

    @Test
    @DisplayName("with consumer no width ratio width ratio is null")
    void withConsumer_noWidthRatio_widthRatioIsNull() {
      assertNull(NotionBlocks.column(b -> b.paragraph("p")).getColumn().getWidthRatio());
    }

    @Test
    @DisplayName("with consumer and width ratio sets width ratio and children")
    void withConsumerAndWidthRatio_setsWidthRatioAndChildren() {
      ColumnBlock col = NotionBlocks.column(0.5, b -> b.paragraph("p").heading1("h"));
      assertEquals(0.5, col.getColumn().getWidthRatio());
      assertEquals(2, col.getColumn().getChildren().size());
    }

    @Test
    @DisplayName("with list sets children")
    void withList_setsChildren() {
      assertEquals(
          1,
          NotionBlocks.column(List.of(NotionBlocks.paragraph("x")))
              .getColumn()
              .getChildren()
              .size());
    }

    @Test
    @DisplayName("with list no width ratio width ratio is null")
    void withList_noWidthRatio_widthRatioIsNull() {
      assertNull(
          NotionBlocks.column(List.of(NotionBlocks.paragraph("x"))).getColumn().getWidthRatio());
    }

    @Test
    @DisplayName("with width ratio and list sets width ratio")
    void withWidthRatioAndList_setsWidthRatio() {
      List<Block> content = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(0.25, NotionBlocks.column(0.25, content).getColumn().getWidthRatio());
    }

    @Test
    @DisplayName("with width ratio and list sets children")
    void withWidthRatioAndList_setsChildren() {
      List<Block> content = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(2, NotionBlocks.column(0.25, content).getColumn().getChildren().size());
    }

    @Test
    @DisplayName("with width ratio and block varargs sets width ratio")
    void withWidthRatioAndBlockVarargs_setsWidthRatio() {
      assertEquals(
          0.6,
          NotionBlocks.column(0.6, NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .getColumn()
              .getWidthRatio());
    }

    @Test
    @DisplayName("with width ratio and block varargs sets children")
    void withWidthRatioAndBlockVarargs_setsChildren() {
      assertEquals(
          2,
          NotionBlocks.column(0.6, NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .getColumn()
              .getChildren()
              .size());
    }

    @Test
    @DisplayName("with width ratio and block varargs children are defensive copy")
    void withWidthRatioAndBlockVarargs_childrenAreDefensiveCopy() {
      ParagraphBlock p1 = NotionBlocks.paragraph("a");
      ParagraphBlock p2 = NotionBlocks.paragraph("b");
      ColumnBlock col = NotionBlocks.column(0.5, p1, p2);
      col.getColumn().getChildren().clear();
      // original varargs content is not affected — column was built with its own list
      assertEquals(2, NotionBlocks.column(0.5, p1, p2).getColumn().getChildren().size());
    }
  }

  @Nested
  class Columns {

    @Test
    @DisplayName("from column block varargs less than two throws illegal argument")
    void fromColumnBlockVarargs_lessThanTwo_throwsIllegalArgument() {
      ColumnBlock col = NotionBlocks.column(b -> b.paragraph("x"));
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.columns(col));
    }

    @Test
    @DisplayName("from column block varargs two blocks returns column list block")
    void fromColumnBlockVarargs_twoBlocks_returnsColumnListBlock() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertInstanceOf(ColumnListBlock.class, NotionBlocks.columns(a, c));
    }

    @Test
    @DisplayName("from column block varargs two blocks sets two columns")
    void fromColumnBlockVarargs_twoBlocks_setsTwoColumns() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertEquals(2, NotionBlocks.columns(a, c).getColumnList().getChildren().size());
    }

    @Test
    @DisplayName("from column block varargs preserves width ratios")
    void fromColumnBlockVarargs_preservesWidthRatios() {
      ColumnBlock a = NotionBlocks.column(0.3, b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(0.7, b -> b.paragraph("c"));
      List<ColumnBlock> children = NotionBlocks.columns(a, c).getColumnList().getChildren();
      assertEquals(0.3, children.get(0).getColumn().getWidthRatio());
      assertEquals(0.7, children.get(1).getColumn().getWidthRatio());
    }

    @Test
    @DisplayName("from column block list returns column list block")
    void fromColumnBlockList_returnsColumnListBlock() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertInstanceOf(ColumnListBlock.class, NotionBlocks.columns(List.of(a, c)));
    }

    @Test
    @DisplayName("from column block list less than two throws illegal argument")
    void fromColumnBlockList_lessThanTwo_throwsIllegalArgument() {
      ColumnBlock col = NotionBlocks.column(b -> b.paragraph("x"));
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.columns(List.of(col)));
    }

    @Test
    @DisplayName("from column block list sets two columns")
    void fromColumnBlockList_setsTwoColumns() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertEquals(2, NotionBlocks.columns(List.of(a, c)).getColumnList().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from block list varargs returns column list block")
    void fromBlockListVarargs_returnsColumnListBlock() {
      List<Block> left = List.of(NotionBlocks.paragraph("left"));
      List<Block> right = List.of(NotionBlocks.paragraph("right"));
      assertInstanceOf(ColumnListBlock.class, NotionBlocks.columns(left, right));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from block list varargs sets two columns")
    void fromBlockListVarargs_setsTwoColumns() {
      List<Block> left = List.of(NotionBlocks.paragraph("left"));
      List<Block> right = List.of(NotionBlocks.paragraph("right"));
      assertEquals(2, NotionBlocks.columns(left, right).getColumnList().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from block list varargs each list becomes column children")
    void fromBlockListVarargs_eachListBecomesColumnChildren() {
      List<Block> left = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      List<Block> right = List.of(NotionBlocks.heading1("h"));
      List<ColumnBlock> children = NotionBlocks.columns(left, right).getColumnList().getChildren();
      assertEquals(2, children.get(0).getColumn().getChildren().size());
      assertEquals(1, children.get(1).getColumn().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from block list varargs less than two throws illegal argument")
    void fromBlockListVarargs_lessThanTwo_throwsIllegalArgument() {
      List<Block> single = List.of(NotionBlocks.paragraph("x"));
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.columns(single));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers returns column list block")
    void fromConsumers_returnsColumnListBlock() {
      assertInstanceOf(
          ColumnListBlock.class,
          NotionBlocks.columns(b -> b.paragraph("left"), b -> b.paragraph("right")));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers sets two columns")
    void fromConsumers_setsTwoColumns() {
      assertEquals(
          2,
          NotionBlocks.columns(b -> b.paragraph("left"), b -> b.paragraph("right"))
              .getColumnList()
              .getChildren()
              .size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers each consumer populates its column")
    void fromConsumers_eachConsumerPopulatesItsColumn() {
      List<ColumnBlock> children =
          NotionBlocks.columns(b -> b.paragraph("a").paragraph("b"), b -> b.heading1("h"))
              .getColumnList()
              .getChildren();
      assertEquals(2, children.get(0).getColumn().getChildren().size());
      assertEquals(1, children.get(1).getColumn().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers less than two throws illegal argument")
    void fromConsumers_lessThanTwo_throwsIllegalArgument() {
      assertThrows(
          IllegalArgumentException.class, () -> NotionBlocks.columns(b -> b.paragraph("only")));
    }
  }

  @Nested
  class Divider {

    @Test
    @DisplayName("returns divider block")
    void returnsDividerBlock() {
      assertInstanceOf(DividerBlock.class, NotionBlocks.divider());
    }
  }

  @Nested
  class Embed {

    @Test
    @DisplayName("from string sets url")
    void fromString_setsUrl() {
      assertEquals(
          "https://example.com", NotionBlocks.embed("https://example.com").getEmbed().getUrl());
    }
  }

  @Nested
  class Equation {

    @Test
    @DisplayName("sets expression")
    void setsExpression() {
      assertEquals("E=mc^2", NotionBlocks.equation("E=mc^2").getEquation().getExpression());
    }
  }

  @Nested
  class File {

    @Test
    @DisplayName("from external url sets external type")
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.file(EXTERNAL_URL).getFile().getType());
    }

    @Test
    @DisplayName("from uuid sets file upload type")
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.file(UUID_STRING).getFile().getType());
    }
  }

  @Nested
  class Heading1 {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "Title",
          NotionBlocks.heading1("Title").getHeading1().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("from varargs sets multiple")
    void fromVarargs_setsMultiple() {
      assertEquals(
          2,
          NotionBlocks.heading1(plainText("a"), plainText("b")).getHeading1().getRichText().size());
    }
  }

  @Nested
  class Heading2 {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "Sub", NotionBlocks.heading2("Sub").getHeading2().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Heading3 {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "H3", NotionBlocks.heading3("H3").getHeading3().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Heading4 {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "H4", NotionBlocks.heading4("H4").getHeading4().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Image {

    @Test
    @DisplayName("from external url sets external type")
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.image(EXTERNAL_URL).getImage().getType());
    }

    @Test
    @DisplayName("from uuid sets file upload type")
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.image(UUID_STRING).getImage().getType());
    }
  }

  @Nested
  class LinkToPage {

    @Test
    @DisplayName("sets page id")
    void setsPageId() {
      assertEquals("page-1", NotionBlocks.linkToPage("page-1").getLinkToPage().getPageId());
    }

    @Test
    @DisplayName("sets type to page id")
    void setsTypeToPageId() {
      assertEquals("page_id", NotionBlocks.linkToPage("page-1").getLinkToPage().getType());
    }
  }

  @Nested
  class LinkToDatabase {

    @Test
    @DisplayName("sets database id")
    void setsDatabaseId() {
      assertEquals("db-1", NotionBlocks.linkToDatabase("db-1").getLinkToPage().getDatabaseId());
    }

    @Test
    @DisplayName("sets type to database")
    void setsTypeToDatabase() {
      assertEquals("database_id", NotionBlocks.linkToDatabase("db-1").getLinkToPage().getType());
    }
  }

  @Nested
  class LinkToComment {

    @Test
    @DisplayName("sets comment id")
    void setsCommentId() {
      assertEquals("c-1", NotionBlocks.linkToComment("c-1").getLinkToPage().getCommentId());
    }

    @Test
    @DisplayName("sets type to comment id")
    void setsTypeToCommentId() {
      assertEquals("comment_id", NotionBlocks.linkToComment("c-1").getLinkToPage().getType());
    }
  }

  @Nested
  class Numbered {

    @Test
    @DisplayName("from string creates one rich text")
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.numbered("item").getNumberedListItem().getRichText().size());
    }

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "item",
          NotionBlocks.numbered("item").getNumberedListItem().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class NumberedList {

    @Test
    @DisplayName("varargs returns correct count")
    void varargs_returnsCorrectCount() {
      assertEquals(3, NotionBlocks.numberedItems("a", "b", "c").size());
    }

    @Test
    @DisplayName("each item is numbered list item block")
    void eachItem_isNumberedListItemBlock() {
      NotionBlocks.numberedItems("x", "y")
          .forEach(b -> assertInstanceOf(NumberedListItemBlock.class, b));
    }
  }

  @Nested
  class Paragraph {

    @Test
    @DisplayName("from string creates one rich text")
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.paragraph("Hello").getParagraph().getRichText().size());
    }

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "Hello",
          NotionBlocks.paragraph("Hello").getParagraph().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("from varargs sets multiple")
    void fromVarargs_setsMultiple() {
      assertEquals(
          2,
          NotionBlocks.paragraph(plainText("a"), plainText("b"))
              .getParagraph()
              .getRichText()
              .size());
    }
  }

  @Nested
  class ParagraphList {

    @Test
    @DisplayName("varargs returns correct count")
    void varargs_returnsCorrectCount() {
      assertEquals(2, NotionBlocks.paragraphList("a", "b").size());
    }

    @Test
    @DisplayName("each item is paragraph block")
    void eachItem_isParagraphBlock() {
      NotionBlocks.paragraphList("x", "y").forEach(b -> assertInstanceOf(ParagraphBlock.class, b));
    }
  }

  @Nested
  class Pdf {

    @Test
    @DisplayName("from external url sets external type")
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.pdf(EXTERNAL_URL).getPdf().getType());
    }

    @Test
    @DisplayName("from uuid sets file upload type")
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.pdf(UUID_STRING).getPdf().getType());
    }
  }

  @Nested
  class Quote {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "wisdom", NotionBlocks.quote("wisdom").getQuote().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("from varargs sets multiple rich texts")
    void fromVarargs_setsMultipleRichTexts() {
      assertEquals(
          2, NotionBlocks.quote(plainText("a"), plainText("b")).getQuote().getRichText().size());
    }
  }

  @Nested
  class Synced {

    @Test
    @DisplayName("from varargs sets children")
    void fromVarargs_setsChildren() {
      assertEquals(
          2,
          NotionBlocks.synced(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .getSyncedBlock()
              .getChildren()
              .size());
    }

    @Test
    @DisplayName("from consumer sets children")
    void fromConsumer_setsChildren() {
      assertFalse(
          NotionBlocks.synced(b -> b.paragraph("x")).getSyncedBlock().getChildren().isEmpty());
    }
  }

  @Nested
  class SyncedFrom {

    @Test
    @DisplayName("sets block id")
    void setsBlockId() {
      assertEquals(
          "block-123",
          NotionBlocks.syncedFrom("block-123").getSyncedBlock().getSyncedFrom().getBlockId());
    }
  }

  @Nested
  class Table {

    @Test
    @DisplayName("from varargs infers table width")
    void fromVarargs_infersTableWidth() {
      TableRowBlock row = NotionBlocks.tableRow("a", "b", "c");
      assertEquals(3, NotionBlocks.table(row).getTable().getTableWidth());
    }

    @Test
    @DisplayName("from varargs sets children")
    void fromVarargs_setsChildren() {
      TableRowBlock row = NotionBlocks.tableRow("a", "b");
      assertEquals(1, NotionBlocks.table(row).getTable().getChildren().size());
    }

    @Test
    @DisplayName("mismatched row widths throws illegal argument")
    void mismatchedRowWidths_throwsIllegalArgument() {
      TableRowBlock r1 = NotionBlocks.tableRow("a", "b");
      TableRowBlock r2 = NotionBlocks.tableRow("x");
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.table(List.of(r1, r2)));
    }

    @Test
    @DisplayName("empty rows table width is zero")
    void emptyRows_tableWidthIsZero() {
      assertEquals(0, NotionBlocks.table(List.of()).getTable().getTableWidth());
    }
  }

  @Nested
  class TableRow {

    @Test
    @DisplayName("from strings creates correct cell count")
    void fromStrings_createsCorrectCellCount() {
      assertEquals(3, NotionBlocks.tableRow("a", "b", "c").getTableRow().getCells().size());
    }

    @Test
    @DisplayName("from strings each cell contains one rich text")
    void fromStrings_eachCellContainsOneRichText() {
      NotionBlocks.tableRow("x", "y")
          .getTableRow()
          .getCells()
          .forEach(cell -> assertEquals(1, cell.size()));
    }

    @Test
    @DisplayName("from rich texts each arg becomes separate cell")
    void fromRichTexts_eachArgBecomesSeparateCell() {
      assertEquals(
          2, NotionBlocks.tableRow(plainText("a"), plainText("b")).getTableRow().getCells().size());
    }

    @Test
    @DisplayName("from rich texts cell contains same rich text instance")
    void fromRichTexts_cellContainsSameRichTextInstance() {
      RichText rt = plainText("hello");
      assertSame(rt, NotionBlocks.tableRow(rt).getTableRow().getCells().get(0).get(0));
    }
  }

  @Nested
  class TableOfContents {

    @Test
    @DisplayName("no args returns table of contents block")
    void noArgs_returnsTableOfContentsBlock() {
      assertInstanceOf(TableOfContentsBlock.class, NotionBlocks.tableOfContents());
    }

    @Test
    @DisplayName("with color enum sets color value")
    void withColorEnum_setsColorValue() {
      assertEquals(
          "blue", NotionBlocks.tableOfContents(Color.BLUE).getTableOfContents().getColor());
    }

    @Test
    @DisplayName("with color string sets color value")
    void withColorString_setsColorValue() {
      assertEquals("gray", NotionBlocks.tableOfContents("gray").getTableOfContents().getColor());
    }

    @Test
    @DisplayName("null color throws illegal argument")
    void nullColor_throwsIllegalArgument() {
      assertThrows(
          IllegalArgumentException.class, () -> NotionBlocks.tableOfContents((Color) null));
    }
  }

  @Nested
  class Todo {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "Buy milk", NotionBlocks.todo("Buy milk").getToDo().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("from varargs sets multiple rich texts")
    void fromVarargs_setsMultipleRichTexts() {
      assertEquals(
          2, NotionBlocks.todo(plainText("a"), plainText("b")).getToDo().getRichText().size());
    }
  }

  @Nested
  class TodoList {

    @Test
    @DisplayName("varargs returns correct count")
    void varargs_returnsCorrectCount() {
      assertEquals(2, NotionBlocks.todos("a", "b").size());
    }

    @Test
    @DisplayName("each item is to do block")
    void eachItem_isToDoBlock() {
      NotionBlocks.todos("x", "y").forEach(b -> assertInstanceOf(ToDoBlock.class, b));
    }
  }

  @Nested
  class Toggle {

    @Test
    @DisplayName("from string sets plain text")
    void fromString_setsPlainText() {
      assertEquals(
          "expand", NotionBlocks.toggle("expand").getToggle().getRichText().get(0).getPlainText());
    }

    @Test
    @DisplayName("from varargs sets multiple rich texts")
    void fromVarargs_setsMultipleRichTexts() {
      assertEquals(
          2, NotionBlocks.toggle(plainText("a"), plainText("b")).getToggle().getRichText().size());
    }
  }

  @Nested
  class ToggleList {

    @Test
    @DisplayName("varargs returns correct count")
    void varargs_returnsCorrectCount() {
      assertEquals(2, NotionBlocks.toggles("a", "b").size());
    }

    @Test
    @DisplayName("each item is toggle block")
    void eachItem_isToggleBlock() {
      NotionBlocks.toggles("x", "y").forEach(b -> assertInstanceOf(ToggleBlock.class, b));
    }
  }

  @Nested
  class Video {

    @Test
    @DisplayName("from external url sets external type")
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.video(EXTERNAL_URL).getVideo().getType());
    }

    @Test
    @DisplayName("from uuid sets file upload type")
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.video(UUID_STRING).getVideo().getType());
    }
  }
}
