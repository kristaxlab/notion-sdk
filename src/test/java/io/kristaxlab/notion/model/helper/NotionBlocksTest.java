package io.kristaxlab.notion.model.helper;

import static io.kristaxlab.notion.model.helper.NotionText.plainText;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionBlocksTest {

  private static final String UUID_STRING = "550e8400-e29b-41d4-a716-446655440000";
  private static final String EXTERNAL_URL = "https://example.com/file.mp3";

  @Nested
  class Audio {

    @Test
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.audio(EXTERNAL_URL).getAudio().getType());
    }

    @Test
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.audio(UUID_STRING).getAudio().getType());
    }

    @Test
    void fromUuid_setsFileUploadId() {
      assertEquals(UUID_STRING, NotionBlocks.audio(UUID_STRING).getAudio().getFileUpload().getId());
    }

    @Test
    void fromFileData_assignsSameFileData() {
      FileData fd = FileData.builder().externalUrl(EXTERNAL_URL).build();
      assertSame(fd, NotionBlocks.audio(fd).getAudio());
    }

    @Test
    void fromConsumer_appliesConsumer() {
      assertEquals(
          "external", NotionBlocks.audio(b -> b.externalUrl(EXTERNAL_URL)).getAudio().getType());
    }
  }

  @Nested
  class BlankLine {

    @Test
    void returnsParagraphBlock() {
      assertInstanceOf(ParagraphBlock.class, NotionBlocks.blankLine());
    }

    @Test
    void hasEmptyPlainText() {
      assertEquals("", NotionBlocks.blankLine().getParagraph().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Bookmark {

    @Test
    void fromString_setsUrl() {
      assertEquals(
          "https://notion.so", NotionBlocks.bookmark("https://notion.so").getBookmark().getUrl());
    }

    @Test
    void fromConsumer_appliesConsumer() {
      assertNotNull(NotionBlocks.bookmark(b -> b.url("https://x.com")).getBookmark().getUrl());
    }
  }

  @Nested
  class Breadcrumb {

    @Test
    void returnsBreadcrumbBlock() {
      assertInstanceOf(BreadcrumbBlock.class, NotionBlocks.breadcrumb());
    }
  }

  @Nested
  class Bullet {

    @Test
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.bullet("item").getBulletedListItem().getRichText().size());
    }

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "item",
          NotionBlocks.bullet("item").getBulletedListItem().getRichText().get(0).getPlainText());
    }

    @Test
    void fromRichTextVarargs_setsMultiple() {
      assertEquals(
          2,
          NotionBlocks.bullet(plainText("a"), plainText("b"))
              .getBulletedListItem()
              .getRichText()
              .size());
    }

    @Test
    void fromList_setsRichTexts() {
      assertEquals(
          1,
          NotionBlocks.bullet(List.of(plainText("x"))).getBulletedListItem().getRichText().size());
    }
  }

  @Nested
  class BulletedList {

    @Test
    void varargs_returnsCorrectCount() {
      assertEquals(3, NotionBlocks.bullets("a", "b", "c").size());
    }

    @Test
    void eachItem_isBulletedListItemBlock() {
      NotionBlocks.bullets("x", "y").forEach(b -> assertInstanceOf(BulletedListItemBlock.class, b));
    }
  }

  @Nested
  class Callout {

    @Test
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.callout("note").getCallout().getRichText().size());
    }

    @Test
    void withEmojiString_setsIconEmoji() {
      assertEquals("💡", NotionBlocks.callout("💡", "note").getCallout().getIcon().getEmoji());
    }

    @Test
    void withIconAndString_setsIcon() {
      assertNotNull(NotionBlocks.callout(Icon.emoji("🔔"), "text").getCallout().getIcon());
    }

    @Test
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
    void setsLanguage() {
      assertEquals("java", NotionBlocks.code("java", "int x;").getCode().getLanguage());
    }

    @Test
    void setsCodeContent() {
      assertFalse(NotionBlocks.code("java", "int x;").getCode().getRichText().isEmpty());
    }
  }

  @Nested
  class Column {

    @Test
    void withWidthRatio_setsWidthRatio() {
      assertEquals(
          0.33, NotionBlocks.column(0.33, b -> b.paragraph("p")).getColumn().getWidthRatio());
    }

    @Test
    void withConsumer_populatesChildren() {
      assertFalse(NotionBlocks.column(b -> b.paragraph("p")).getColumn().getChildren().isEmpty());
    }

    @Test
    void withConsumer_noWidthRatio_widthRatioIsNull() {
      assertNull(NotionBlocks.column(b -> b.paragraph("p")).getColumn().getWidthRatio());
    }

    @Test
    void withConsumerAndWidthRatio_setsWidthRatioAndChildren() {
      ColumnBlock col = NotionBlocks.column(0.5, b -> b.paragraph("p").heading1("h"));
      assertEquals(0.5, col.getColumn().getWidthRatio());
      assertEquals(2, col.getColumn().getChildren().size());
    }

    @Test
    void withList_setsChildren() {
      assertEquals(
          1,
          NotionBlocks.column(List.of(NotionBlocks.paragraph("x")))
              .getColumn()
              .getChildren()
              .size());
    }

    @Test
    void withList_noWidthRatio_widthRatioIsNull() {
      assertNull(
          NotionBlocks.column(List.of(NotionBlocks.paragraph("x"))).getColumn().getWidthRatio());
    }

    @Test
    void withWidthRatioAndList_setsWidthRatio() {
      List<Block> content = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(0.25, NotionBlocks.column(0.25, content).getColumn().getWidthRatio());
    }

    @Test
    void withWidthRatioAndList_setsChildren() {
      List<Block> content = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(2, NotionBlocks.column(0.25, content).getColumn().getChildren().size());
    }

    @Test
    void withWidthRatioAndBlockVarargs_setsWidthRatio() {
      assertEquals(
          0.6,
          NotionBlocks.column(0.6, NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .getColumn()
              .getWidthRatio());
    }

    @Test
    void withWidthRatioAndBlockVarargs_setsChildren() {
      assertEquals(
          2,
          NotionBlocks.column(0.6, NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .getColumn()
              .getChildren()
              .size());
    }

    @Test
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
    void fromColumnBlockVarargs_lessThanTwo_throwsIllegalArgument() {
      ColumnBlock col = NotionBlocks.column(b -> b.paragraph("x"));
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.columns(col));
    }

    @Test
    void fromColumnBlockVarargs_twoBlocks_returnsColumnListBlock() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertInstanceOf(ColumnListBlock.class, NotionBlocks.columns(a, c));
    }

    @Test
    void fromColumnBlockVarargs_twoBlocks_setsTwoColumns() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertEquals(2, NotionBlocks.columns(a, c).getColumnList().getChildren().size());
    }

    @Test
    void fromColumnBlockVarargs_preservesWidthRatios() {
      ColumnBlock a = NotionBlocks.column(0.3, b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(0.7, b -> b.paragraph("c"));
      List<ColumnBlock> children = NotionBlocks.columns(a, c).getColumnList().getChildren();
      assertEquals(0.3, children.get(0).getColumn().getWidthRatio());
      assertEquals(0.7, children.get(1).getColumn().getWidthRatio());
    }

    @Test
    void fromColumnBlockList_returnsColumnListBlock() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertInstanceOf(ColumnListBlock.class, NotionBlocks.columns(List.of(a, c)));
    }

    @Test
    void fromColumnBlockList_lessThanTwo_throwsIllegalArgument() {
      ColumnBlock col = NotionBlocks.column(b -> b.paragraph("x"));
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.columns(List.of(col)));
    }

    @Test
    void fromColumnBlockList_setsTwoColumns() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertEquals(2, NotionBlocks.columns(List.of(a, c)).getColumnList().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromBlockListVarargs_returnsColumnListBlock() {
      List<Block> left = List.of(NotionBlocks.paragraph("left"));
      List<Block> right = List.of(NotionBlocks.paragraph("right"));
      assertInstanceOf(ColumnListBlock.class, NotionBlocks.columns(left, right));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromBlockListVarargs_setsTwoColumns() {
      List<Block> left = List.of(NotionBlocks.paragraph("left"));
      List<Block> right = List.of(NotionBlocks.paragraph("right"));
      assertEquals(2, NotionBlocks.columns(left, right).getColumnList().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromBlockListVarargs_eachListBecomesColumnChildren() {
      List<Block> left = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      List<Block> right = List.of(NotionBlocks.heading1("h"));
      List<ColumnBlock> children = NotionBlocks.columns(left, right).getColumnList().getChildren();
      assertEquals(2, children.get(0).getColumn().getChildren().size());
      assertEquals(1, children.get(1).getColumn().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromBlockListVarargs_lessThanTwo_throwsIllegalArgument() {
      List<Block> single = List.of(NotionBlocks.paragraph("x"));
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.columns(single));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromConsumers_returnsColumnListBlock() {
      assertInstanceOf(
          ColumnListBlock.class,
          NotionBlocks.columns(b -> b.paragraph("left"), b -> b.paragraph("right")));
    }

    @SuppressWarnings("unchecked")
    @Test
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
    void fromConsumers_lessThanTwo_throwsIllegalArgument() {
      assertThrows(
          IllegalArgumentException.class, () -> NotionBlocks.columns(b -> b.paragraph("only")));
    }
  }

  @Nested
  class Divider {

    @Test
    void returnsDividerBlock() {
      assertInstanceOf(DividerBlock.class, NotionBlocks.divider());
    }
  }

  @Nested
  class Embed {

    @Test
    void fromString_setsUrl() {
      assertEquals(
          "https://example.com", NotionBlocks.embed("https://example.com").getEmbed().getUrl());
    }
  }

  @Nested
  class Equation {

    @Test
    void setsExpression() {
      assertEquals("E=mc^2", NotionBlocks.equation("E=mc^2").getEquation().getExpression());
    }
  }

  @Nested
  class File {

    @Test
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.file(EXTERNAL_URL).getFile().getType());
    }

    @Test
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.file(UUID_STRING).getFile().getType());
    }
  }

  @Nested
  class Heading1 {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "Title",
          NotionBlocks.heading1("Title").getHeading1().getRichText().get(0).getPlainText());
    }

    @Test
    void fromVarargs_setsMultiple() {
      assertEquals(
          2,
          NotionBlocks.heading1(plainText("a"), plainText("b")).getHeading1().getRichText().size());
    }
  }

  @Nested
  class Heading2 {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "Sub", NotionBlocks.heading2("Sub").getHeading2().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Heading3 {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "H3", NotionBlocks.heading3("H3").getHeading3().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Heading4 {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "H4", NotionBlocks.heading4("H4").getHeading4().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class Image {

    @Test
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.image(EXTERNAL_URL).getImage().getType());
    }

    @Test
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.image(UUID_STRING).getImage().getType());
    }
  }

  @Nested
  class LinkToPage {

    @Test
    void setsPageId() {
      assertEquals("page-1", NotionBlocks.linkToPage("page-1").getLinkToPage().getPageId());
    }

    @Test
    void setsTypeToPageId() {
      assertEquals("page_id", NotionBlocks.linkToPage("page-1").getLinkToPage().getType());
    }
  }

  @Nested
  class LinkToDatabase {

    @Test
    void setsDatabaseId() {
      assertEquals("db-1", NotionBlocks.linkToDatabase("db-1").getLinkToPage().getDatabaseId());
    }

    @Test
    void setsTypeToDatabase() {
      assertEquals("database_id", NotionBlocks.linkToDatabase("db-1").getLinkToPage().getType());
    }
  }

  @Nested
  class LinkToComment {

    @Test
    void setsCommentId() {
      assertEquals("c-1", NotionBlocks.linkToComment("c-1").getLinkToPage().getCommentId());
    }

    @Test
    void setsTypeToCommentId() {
      assertEquals("comment_id", NotionBlocks.linkToComment("c-1").getLinkToPage().getType());
    }
  }

  @Nested
  class Numbered {

    @Test
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.numbered("item").getNumberedListItem().getRichText().size());
    }

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "item",
          NotionBlocks.numbered("item").getNumberedListItem().getRichText().get(0).getPlainText());
    }
  }

  @Nested
  class NumberedList {

    @Test
    void varargs_returnsCorrectCount() {
      assertEquals(3, NotionBlocks.numberedItems("a", "b", "c").size());
    }

    @Test
    void eachItem_isNumberedListItemBlock() {
      NotionBlocks.numberedItems("x", "y")
          .forEach(b -> assertInstanceOf(NumberedListItemBlock.class, b));
    }
  }

  @Nested
  class Paragraph {

    @Test
    void fromString_createsOneRichText() {
      assertEquals(1, NotionBlocks.paragraph("Hello").getParagraph().getRichText().size());
    }

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "Hello",
          NotionBlocks.paragraph("Hello").getParagraph().getRichText().get(0).getPlainText());
    }

    @Test
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
    void varargs_returnsCorrectCount() {
      assertEquals(2, NotionBlocks.paragraphList("a", "b").size());
    }

    @Test
    void eachItem_isParagraphBlock() {
      NotionBlocks.paragraphList("x", "y").forEach(b -> assertInstanceOf(ParagraphBlock.class, b));
    }
  }

  @Nested
  class Pdf {

    @Test
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.pdf(EXTERNAL_URL).getPdf().getType());
    }

    @Test
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.pdf(UUID_STRING).getPdf().getType());
    }
  }

  @Nested
  class Quote {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "wisdom", NotionBlocks.quote("wisdom").getQuote().getRichText().get(0).getPlainText());
    }

    @Test
    void fromVarargs_setsMultipleRichTexts() {
      assertEquals(
          2, NotionBlocks.quote(plainText("a"), plainText("b")).getQuote().getRichText().size());
    }
  }

  @Nested
  class Synced {

    @Test
    void fromVarargs_setsChildren() {
      assertEquals(
          2,
          NotionBlocks.synced(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .getSyncedBlock()
              .getChildren()
              .size());
    }

    @Test
    void fromConsumer_setsChildren() {
      assertFalse(
          NotionBlocks.synced(b -> b.paragraph("x")).getSyncedBlock().getChildren().isEmpty());
    }
  }

  @Nested
  class SyncedFrom {

    @Test
    void setsBlockId() {
      assertEquals(
          "block-123",
          NotionBlocks.syncedFrom("block-123").getSyncedBlock().getSyncedFrom().getBlockId());
    }
  }

  @Nested
  class Table {

    @Test
    void fromVarargs_infersTableWidth() {
      TableRowBlock row = NotionBlocks.tableRow("a", "b", "c");
      assertEquals(3, NotionBlocks.table(row).getTable().getTableWidth());
    }

    @Test
    void fromVarargs_setsChildren() {
      TableRowBlock row = NotionBlocks.tableRow("a", "b");
      assertEquals(1, NotionBlocks.table(row).getTable().getChildren().size());
    }

    @Test
    void mismatchedRowWidths_throwsIllegalArgument() {
      TableRowBlock r1 = NotionBlocks.tableRow("a", "b");
      TableRowBlock r2 = NotionBlocks.tableRow("x");
      assertThrows(IllegalArgumentException.class, () -> NotionBlocks.table(List.of(r1, r2)));
    }

    @Test
    void emptyRows_tableWidthIsZero() {
      assertEquals(0, NotionBlocks.table(List.of()).getTable().getTableWidth());
    }
  }

  @Nested
  class TableRow {

    @Test
    void fromStrings_createsCorrectCellCount() {
      assertEquals(3, NotionBlocks.tableRow("a", "b", "c").getTableRow().getCells().size());
    }

    @Test
    void fromStrings_eachCellContainsOneRichText() {
      NotionBlocks.tableRow("x", "y")
          .getTableRow()
          .getCells()
          .forEach(cell -> assertEquals(1, cell.size()));
    }

    @Test
    void fromRichTexts_eachArgBecomesSeparateCell() {
      assertEquals(
          2, NotionBlocks.tableRow(plainText("a"), plainText("b")).getTableRow().getCells().size());
    }

    @Test
    void fromRichTexts_cellContainsSameRichTextInstance() {
      RichText rt = plainText("hello");
      assertSame(rt, NotionBlocks.tableRow(rt).getTableRow().getCells().get(0).get(0));
    }
  }

  @Nested
  class TableOfContents {

    @Test
    void noArgs_returnsTableOfContentsBlock() {
      assertInstanceOf(TableOfContentsBlock.class, NotionBlocks.tableOfContents());
    }

    @Test
    void withColorEnum_setsColorValue() {
      assertEquals(
          "blue", NotionBlocks.tableOfContents(Color.BLUE).getTableOfContents().getColor());
    }

    @Test
    void withColorString_setsColorValue() {
      assertEquals("gray", NotionBlocks.tableOfContents("gray").getTableOfContents().getColor());
    }

    @Test
    void nullColor_throwsIllegalArgument() {
      assertThrows(
          IllegalArgumentException.class, () -> NotionBlocks.tableOfContents((Color) null));
    }
  }

  @Nested
  class Todo {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "Buy milk", NotionBlocks.todo("Buy milk").getToDo().getRichText().get(0).getPlainText());
    }

    @Test
    void fromVarargs_setsMultipleRichTexts() {
      assertEquals(
          2, NotionBlocks.todo(plainText("a"), plainText("b")).getToDo().getRichText().size());
    }
  }

  @Nested
  class TodoList {

    @Test
    void varargs_returnsCorrectCount() {
      assertEquals(2, NotionBlocks.todos("a", "b").size());
    }

    @Test
    void eachItem_isToDoBlock() {
      NotionBlocks.todos("x", "y").forEach(b -> assertInstanceOf(ToDoBlock.class, b));
    }
  }

  @Nested
  class Toggle {

    @Test
    void fromString_setsPlainText() {
      assertEquals(
          "expand", NotionBlocks.toggle("expand").getToggle().getRichText().get(0).getPlainText());
    }

    @Test
    void fromVarargs_setsMultipleRichTexts() {
      assertEquals(
          2, NotionBlocks.toggle(plainText("a"), plainText("b")).getToggle().getRichText().size());
    }
  }

  @Nested
  class ToggleList {

    @Test
    void varargs_returnsCorrectCount() {
      assertEquals(2, NotionBlocks.toggles("a", "b").size());
    }

    @Test
    void eachItem_isToggleBlock() {
      NotionBlocks.toggles("x", "y").forEach(b -> assertInstanceOf(ToggleBlock.class, b));
    }
  }

  @Nested
  class Video {

    @Test
    void fromExternalUrl_setsExternalType() {
      assertEquals("external", NotionBlocks.video(EXTERNAL_URL).getVideo().getType());
    }

    @Test
    void fromUuid_setsFileUploadType() {
      assertEquals("file_upload", NotionBlocks.video(UUID_STRING).getVideo().getType());
    }
  }
}
