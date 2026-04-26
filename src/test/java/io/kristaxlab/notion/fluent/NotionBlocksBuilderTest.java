package io.kristaxlab.notion.fluent;

import static io.kristaxlab.notion.fluent.NotionText.plainText;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.Icon;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionBlocksBuilderTest {

  private static final String EXTERNAL_URL = "https://example.com/file.mp3";
  private static final String UUID_STRING = "550e8400-e29b-41d4-a716-446655440000";

  private NotionBlocksBuilder builder() {
    return NotionBlocks.blocksBuilder();
  }

  @Nested
  class Build {

    @Test
    @DisplayName("empty builder returns empty list")
    void emptyBuilder_returnsEmptyList() {
      assertTrue(builder().build().isEmpty());
    }

    @Test
    @DisplayName("build returns new list instance each call")
    void build_returnsNewListInstanceEachCall() {
      NotionBlocksBuilder b = builder().paragraph("x");
      assertNotSame(b.build(), b.build());
    }

    @Test
    @DisplayName("build contains all added blocks")
    void build_containsAllAddedBlocks() {
      assertEquals(3, builder().paragraph("a").paragraph("b").paragraph("c").build().size());
    }

    @Test
    @DisplayName("mutating returned list does not affect builder")
    void mutatingReturnedList_doesNotAffectBuilder() {
      NotionBlocksBuilder b = builder().paragraph("x");
      b.build().clear();
      assertEquals(1, b.build().size());
    }
  }

  @Nested
  class BlockMethod {

    @Test
    @DisplayName("block adds one block")
    void block_addsOneBlock() {
      assertEquals(1, builder().block(NotionBlocks.paragraph("x")).build().size());
    }

    @Test
    @DisplayName("block adds correct instance")
    void block_addsCorrectInstance() {
      ParagraphBlock p = NotionBlocks.paragraph("x");
      assertSame(p, builder().block(p).build().get(0));
    }
  }

  @Nested
  class BlocksVarargs {

    @Test
    @DisplayName("blocks adds all varargs")
    void blocks_addsAllVarargs() {
      assertEquals(
          2,
          builder()
              .blocks(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .build()
              .size());
    }
  }

  @Nested
  class BlocksList {

    @Test
    @DisplayName("blocks adds all from list")
    void blocks_addsAllFromList() {
      List<Block> list = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      assertEquals(2, builder().blocks(list).build().size());
    }
  }

  @Nested
  class BlankLine {

    @Test
    @DisplayName("blank line adds paragraph block")
    void blankLine_addsParagraphBlock() {
      assertInstanceOf(ParagraphBlock.class, builder().blankLine().build().get(0));
    }
  }

  @Nested
  class Audio {

    @Test
    @DisplayName("from string adds audio block")
    void fromString_addsAudioBlock() {
      assertInstanceOf(AudioBlock.class, builder().audio(EXTERNAL_URL).build().get(0));
    }

    @Test
    @DisplayName("from file data adds audio block")
    void fromFileData_addsAudioBlock() {
      FileData fd = FileData.builder().externalUrl(EXTERNAL_URL).build();
      assertInstanceOf(AudioBlock.class, builder().audio(fd).build().get(0));
    }

    @Test
    @DisplayName("from consumer adds audio block")
    void fromConsumer_addsAudioBlock() {
      assertInstanceOf(
          AudioBlock.class, builder().audio(b -> b.externalUrl(EXTERNAL_URL)).build().get(0));
    }
  }

  @Nested
  class Bookmark {

    @Test
    @DisplayName("from string adds bookmark block")
    void fromString_addsBookmarkBlock() {
      assertInstanceOf(BookmarkBlock.class, builder().bookmark("https://notion.so").build().get(0));
    }
  }

  @Nested
  class Breadcrumb {

    @Test
    @DisplayName("adds breadcrumb block")
    void addsBreadcrumbBlock() {
      assertInstanceOf(BreadcrumbBlock.class, builder().breadcrumb().build().get(0));
    }
  }

  @Nested
  class Bullet {

    @Test
    @DisplayName("from string adds bulleted list item")
    void fromString_addsBulletedListItem() {
      assertInstanceOf(BulletedListItemBlock.class, builder().bullet("item").build().get(0));
    }

    @Test
    @DisplayName("from rich text varargs adds bulleted list item")
    void fromRichTextVarargs_addsBulletedListItem() {
      assertInstanceOf(
          BulletedListItemBlock.class,
          builder().bullet(plainText("a"), plainText("b")).build().get(0));
    }

    @Test
    @DisplayName("from list adds bulleted list item")
    void fromList_addsBulletedListItem() {
      assertInstanceOf(
          BulletedListItemBlock.class, builder().bullet(List.of(plainText("x"))).build().get(0));
    }
  }

  @Nested
  class BulletedList {

    @Test
    @DisplayName("varargs adds multiple blocks")
    void varargs_addsMultipleBlocks() {
      assertEquals(3, builder().bullets("a", "b", "c").build().size());
    }

    @Test
    @DisplayName("list adds multiple blocks")
    void list_addsMultipleBlocks() {
      assertEquals(2, builder().bullets(List.of("x", "y")).build().size());
    }
  }

  @Nested
  class Callout {

    @Test
    @DisplayName("from string adds callout block")
    void fromString_addsCalloutBlock() {
      assertInstanceOf(CalloutBlock.class, builder().callout("note").build().get(0));
    }

    @Test
    @DisplayName("with emoji and string sets icon emoji")
    void withEmojiAndString_setsIconEmoji() {
      CalloutBlock b = (CalloutBlock) builder().callout("💡", "note").build().get(0);
      assertEquals("💡", b.getCallout().getIcon().getEmoji());
    }

    @Test
    @DisplayName("with icon and string adds callout block")
    void withIconAndString_addsCalloutBlock() {
      assertInstanceOf(
          CalloutBlock.class, builder().callout(Icon.emoji("🔔"), "text").build().get(0));
    }
  }

  @Nested
  class Code {

    @Test
    @DisplayName("adds code block")
    void addsCodeBlock() {
      assertInstanceOf(CodeBlock.class, builder().code("java", "int x;").build().get(0));
    }

    @Test
    @DisplayName("sets language")
    void setsLanguage() {
      CodeBlock b = (CodeBlock) builder().code("python", "x = 1").build().get(0);
      assertEquals("python", b.getCode().getLanguage());
    }
  }

  @Nested
  class Columns {

    @Test
    @DisplayName("from column blocks adds column list block")
    void fromColumnBlocks_addsColumnListBlock() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertInstanceOf(ColumnListBlock.class, builder().columns(a, c).build().get(0));
    }

    @Test
    @DisplayName("from column block list adds column list block")
    void fromColumnBlockList_addsColumnListBlock() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      assertInstanceOf(ColumnListBlock.class, builder().columns(List.of(a, c)).build().get(0));
    }

    @Test
    @DisplayName("from column block list sets two columns")
    void fromColumnBlockList_setsTwoColumns() {
      ColumnBlock a = NotionBlocks.column(b -> b.paragraph("a"));
      ColumnBlock c = NotionBlocks.column(b -> b.paragraph("c"));
      ColumnListBlock result = (ColumnListBlock) builder().columns(List.of(a, c)).build().get(0);
      assertEquals(2, result.getColumnList().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers adds column list block")
    void fromConsumers_addsColumnListBlock() {
      assertInstanceOf(
          ColumnListBlock.class,
          builder().columns(b -> b.paragraph("left"), b -> b.paragraph("right")).build().get(0));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers sets two columns")
    void fromConsumers_setsTwoColumns() {
      ColumnListBlock result =
          (ColumnListBlock)
              builder().columns(b -> b.paragraph("left"), b -> b.paragraph("right")).build().get(0);
      assertEquals(2, result.getColumnList().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from consumers each consumer populates its column")
    void fromConsumers_eachConsumerPopulatesItsColumn() {
      ColumnListBlock result =
          (ColumnListBlock)
              builder()
                  .columns(b -> b.paragraph("a").paragraph("b"), b -> b.heading1("h"))
                  .build()
                  .get(0);
      List<ColumnBlock> children = result.getColumnList().getChildren();
      assertEquals(2, children.get(0).getColumn().getChildren().size());
      assertEquals(1, children.get(1).getColumn().getChildren().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from block list varargs adds column list block")
    void fromBlockListVarargs_addsColumnListBlock() {
      List<Block> left = List.of(NotionBlocks.paragraph("left"));
      List<Block> right = List.of(NotionBlocks.paragraph("right"));
      assertInstanceOf(ColumnListBlock.class, builder().columns(left, right).build().get(0));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("from block list varargs each list becomes column children")
    void fromBlockListVarargs_eachListBecomesColumnChildren() {
      List<Block> left = List.of(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"));
      List<Block> right = List.of(NotionBlocks.heading1("h"));
      ColumnListBlock result = (ColumnListBlock) builder().columns(left, right).build().get(0);
      List<ColumnBlock> children = result.getColumnList().getChildren();
      assertEquals(2, children.get(0).getColumn().getChildren().size());
      assertEquals(1, children.get(1).getColumn().getChildren().size());
    }
  }

  @Nested
  class Divider {

    @Test
    @DisplayName("adds divider block")
    void addsDividerBlock() {
      assertInstanceOf(DividerBlock.class, builder().divider().build().get(0));
    }
  }

  @Nested
  class Equation {

    @Test
    @DisplayName("adds equation block")
    void addsEquationBlock() {
      assertInstanceOf(EquationBlock.class, builder().equation("E=mc^2").build().get(0));
    }
  }

  @Nested
  class Image {

    @Test
    @DisplayName("from string adds image block")
    void fromString_addsImageBlock() {
      assertInstanceOf(ImageBlock.class, builder().image(EXTERNAL_URL).build().get(0));
    }

    @Test
    @DisplayName("from uuid adds image block with file upload")
    void fromUuid_addsImageBlockWithFileUpload() {
      ImageBlock b = (ImageBlock) builder().image(UUID_STRING).build().get(0);
      assertEquals("file_upload", b.getImage().getType());
    }
  }

  @Nested
  class Video {

    @Test
    @DisplayName("from string adds video block")
    void fromString_addsVideoBlock() {
      assertInstanceOf(VideoBlock.class, builder().video(EXTERNAL_URL).build().get(0));
    }
  }

  @Nested
  class Pdf {

    @Test
    @DisplayName("from string adds pdf block")
    void fromString_addsPdfBlock() {
      assertInstanceOf(PdfBlock.class, builder().pdf(EXTERNAL_URL).build().get(0));
    }
  }

  @Nested
  class File {

    @Test
    @DisplayName("from string adds file block")
    void fromString_addsFileBlock() {
      assertInstanceOf(FileBlock.class, builder().file(EXTERNAL_URL).build().get(0));
    }
  }

  @Nested
  class Embed {

    @Test
    @DisplayName("from string adds embed block")
    void fromString_addsEmbedBlock() {
      assertInstanceOf(EmbedBlock.class, builder().embed("https://example.com").build().get(0));
    }
  }

  @Nested
  class Headings {

    @Test
    @DisplayName("heading1 adds heading one block")
    void heading1_addsHeadingOneBlock() {
      assertInstanceOf(HeadingOneBlock.class, builder().heading1("Title").build().get(0));
    }

    @Test
    @DisplayName("heading2 adds heading two block")
    void heading2_addsHeadingTwoBlock() {
      assertInstanceOf(HeadingTwoBlock.class, builder().heading2("Sub").build().get(0));
    }

    @Test
    @DisplayName("heading3 adds heading three block")
    void heading3_addsHeadingThreeBlock() {
      assertInstanceOf(HeadingThreeBlock.class, builder().heading3("H3").build().get(0));
    }

    @Test
    @DisplayName("heading4 adds heading four block")
    void heading4_addsHeadingFourBlock() {
      assertInstanceOf(HeadingFourBlock.class, builder().heading4("H4").build().get(0));
    }

    @Test
    @DisplayName("heading1 from rich text varargs sets multiple")
    void heading1_fromRichTextVarargs_setsMultiple() {
      HeadingOneBlock h =
          (HeadingOneBlock) builder().heading1(plainText("a"), plainText("b")).build().get(0);
      assertEquals(2, h.getHeading1().getRichText().size());
    }
  }

  @Nested
  class LinkTo {

    @Test
    @DisplayName("link to page adds link to page block")
    void linkToPage_addsLinkToPageBlock() {
      assertInstanceOf(LinkToPageBlock.class, builder().linkToPage("p-1").build().get(0));
    }

    @Test
    @DisplayName("link to database adds link to page block")
    void linkToDatabase_addsLinkToPageBlock() {
      assertInstanceOf(LinkToPageBlock.class, builder().linkToDatabase("db-1").build().get(0));
    }

    @Test
    @DisplayName("link to comment adds link to page block")
    void linkToComment_addsLinkToPageBlock() {
      assertInstanceOf(LinkToPageBlock.class, builder().linkToComment("c-1").build().get(0));
    }
  }

  @Nested
  class Paragraph {

    @Test
    @DisplayName("from string adds paragraph block")
    void fromString_addsParagraphBlock() {
      assertInstanceOf(ParagraphBlock.class, builder().paragraph("Hello").build().get(0));
    }

    @Test
    @DisplayName("from rich text varargs adds paragraph block")
    void fromRichTextVarargs_addsParagraphBlock() {
      assertInstanceOf(
          ParagraphBlock.class, builder().paragraph(plainText("a"), plainText("b")).build().get(0));
    }

    @Test
    @DisplayName("from list adds paragraph block")
    void fromList_addsParagraphBlock() {
      assertInstanceOf(
          ParagraphBlock.class, builder().paragraph(List.of(plainText("x"))).build().get(0));
    }
  }

  @Nested
  class ParagraphList {

    @Test
    @DisplayName("varargs adds multiple paragraphs")
    void varargs_addsMultipleParagraphs() {
      assertEquals(3, builder().paragraphList("a", "b", "c").build().size());
    }

    @Test
    @DisplayName("list adds multiple paragraphs")
    void list_addsMultipleParagraphs() {
      assertEquals(2, builder().paragraphList(List.of("x", "y")).build().size());
    }
  }

  @Nested
  class Numbered {

    @Test
    @DisplayName("from string adds numbered list item block")
    void fromString_addsNumberedListItemBlock() {
      assertInstanceOf(NumberedListItemBlock.class, builder().numbered("item").build().get(0));
    }
  }

  @Nested
  class NumberedList {

    @Test
    @DisplayName("varargs adds correct count")
    void varargs_addsCorrectCount() {
      assertEquals(3, builder().numberedItems("a", "b", "c").build().size());
    }

    @Test
    @DisplayName("varargs each item is numbered list item block")
    void varargs_eachItemIsNumberedListItemBlock() {
      builder()
          .numberedItems("x", "y")
          .build()
          .forEach(b -> assertInstanceOf(NumberedListItemBlock.class, b));
    }

    @Test
    @DisplayName("list adds correct count")
    void list_addsCorrectCount() {
      assertEquals(2, builder().numberedItems(List.of("x", "y")).build().size());
    }
  }

  @Nested
  class Quote {

    @Test
    @DisplayName("from string adds quote block")
    void fromString_addsQuoteBlock() {
      assertInstanceOf(QuoteBlock.class, builder().quote("wisdom").build().get(0));
    }

    @Test
    @DisplayName("from rich text varargs adds quote block")
    void fromRichTextVarargs_addsQuoteBlock() {
      assertInstanceOf(
          QuoteBlock.class, builder().quote(plainText("a"), plainText("b")).build().get(0));
    }
  }

  @Nested
  class Synced {

    @Test
    @DisplayName("from varargs adds synced block")
    void fromVarargs_addsSyncedBlock() {
      assertInstanceOf(
          SyncedBlock.class,
          builder()
              .synced(NotionBlocks.paragraph("a"), NotionBlocks.paragraph("b"))
              .build()
              .get(0));
    }

    @Test
    @DisplayName("from consumer adds synced block")
    void fromConsumer_addsSyncedBlock() {
      assertInstanceOf(SyncedBlock.class, builder().synced(b -> b.paragraph("x")).build().get(0));
    }

    @Test
    @DisplayName("synced from adds synced block")
    void syncedFrom_addsSyncedBlock() {
      assertInstanceOf(SyncedBlock.class, builder().syncedFrom("block-123").build().get(0));
    }
  }

  @Nested
  class Table {

    @Test
    @DisplayName("from varargs adds table block")
    void fromVarargs_addsTableBlock() {
      TableRowBlock row = NotionBlocks.tableRow("a", "b");
      assertInstanceOf(TableBlock.class, builder().table(row).build().get(0));
    }

    @Test
    @DisplayName("from list adds table block")
    void fromList_addsTableBlock() {
      List<TableRowBlock> rows = List.of(NotionBlocks.tableRow("a", "b"));
      assertInstanceOf(TableBlock.class, builder().table(rows).build().get(0));
    }

    @Test
    @DisplayName("from consumer adds table block")
    void fromConsumer_addsTableBlock() {
      assertInstanceOf(
          TableBlock.class,
          builder().table(t -> t.rows(rows -> rows.row("a", "b").row("c", "d"))).build().get(0));
    }
  }

  @Nested
  class TableOfContents {

    @Test
    @DisplayName("no args adds table of contents block")
    void noArgs_addsTableOfContentsBlock() {
      assertInstanceOf(TableOfContentsBlock.class, builder().tableOfContents().build().get(0));
    }

    @Test
    @DisplayName("with color string sets color")
    void withColorString_setsColor() {
      TableOfContentsBlock b =
          (TableOfContentsBlock) builder().tableOfContents("blue").build().get(0);
      assertEquals("blue", b.getTableOfContents().getColor());
    }

    @Test
    @DisplayName("with color enum sets color")
    void withColorEnum_setsColor() {
      TableOfContentsBlock b =
          (TableOfContentsBlock) builder().tableOfContents(Color.RED).build().get(0);
      assertEquals("red", b.getTableOfContents().getColor());
    }
  }

  @Nested
  class Todo {

    @Test
    @DisplayName("from string adds to do block")
    void fromString_addsToDoBlock() {
      assertInstanceOf(ToDoBlock.class, builder().todo("Buy milk").build().get(0));
    }

    @Test
    @DisplayName("from rich text varargs adds to do block")
    void fromRichTextVarargs_addsToDoBlock() {
      assertInstanceOf(
          ToDoBlock.class, builder().todo(plainText("a"), plainText("b")).build().get(0));
    }
  }

  @Nested
  class TodoList {

    @Test
    @DisplayName("varargs adds multiple to do blocks")
    void varargs_addsMultipleToDoBlocks() {
      assertEquals(3, builder().todos("a", "b", "c").build().size());
    }

    @Test
    @DisplayName("list adds multiple to do blocks")
    void list_addsMultipleToDoBlocks() {
      assertEquals(2, builder().todos(List.of("x", "y")).build().size());
    }
  }

  @Nested
  class Toggle {

    @Test
    @DisplayName("from string adds toggle block")
    void fromString_addsToggleBlock() {
      assertInstanceOf(ToggleBlock.class, builder().toggle("expand").build().get(0));
    }

    @Test
    @DisplayName("from rich text varargs adds toggle block")
    void fromRichTextVarargs_addsToggleBlock() {
      assertInstanceOf(
          ToggleBlock.class, builder().toggle(plainText("a"), plainText("b")).build().get(0));
    }
  }

  @Nested
  class ToggleList {

    @Test
    @DisplayName("varargs adds multiple toggle blocks")
    void varargs_addsMultipleToggleBlocks() {
      assertEquals(2, builder().toggles("a", "b").build().size());
    }

    @Test
    @DisplayName("list adds multiple toggle blocks")
    void list_addsMultipleToggleBlocks() {
      assertEquals(2, builder().toggles(List.of("x", "y")).build().size());
    }
  }

  @Nested
  class FluentChaining {

    @Test
    @DisplayName("multiple calls on same builder accumulate blocks")
    void multipleCallsOnSameBuilder_accumulateBlocks() {
      List<Block> result =
          builder()
              .heading1("Title")
              .paragraph("Intro")
              .bullet("Point 1")
              .bullet("Point 2")
              .divider()
              .build();
      assertEquals(5, result.size());
    }
  }
}
