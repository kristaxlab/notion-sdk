package io.kristixlab.notion.api.model.helper;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.FileData;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlocksBuilderTest {

  @Test
  void of_createsEmptyBuilder() {
    BlocksBuilder builder = BlocksBuilder.of();
    List<Block> blocks = builder.build();

    assertNotNull(blocks);
    assertTrue(blocks.isEmpty());
  }

  @Test
  void build_returnsDefensiveCopy() {
    BlocksBuilder builder = BlocksBuilder.of().paragraph("A");
    List<Block> first = builder.build();
    List<Block> second = builder.build();

    assertNotSame(first, second);
    assertEquals(first.size(), second.size());
  }

  // add / addAll

  @Test
  void add_appendsSingleBlock() {
    ParagraphBlock block = ParagraphBlock.of("Manual");
    List<Block> blocks = BlocksBuilder.of().add(block).build();

    assertEquals(1, blocks.size());
    assertSame(block, blocks.get(0));
  }

  @Test
  void addAll_list_appendsMultipleBlocks() {
    List<Block> children = List.of(ParagraphBlock.of("A"), ParagraphBlock.of("B"));
    List<Block> blocks = BlocksBuilder.of().addAll(children).build();

    assertEquals(2, blocks.size());
  }

  @Test
  void addAll_varargs_appendsMultipleBlocks() {
    List<Block> blocks =
        BlocksBuilder.of().addAll(ParagraphBlock.of("X"), ParagraphBlock.of("Y")).build();

    assertEquals(2, blocks.size());
  }

  // paragraph

  @Test
  void paragraph_string() {
    List<Block> blocks = BlocksBuilder.of().paragraph("Hello").build();

    assertEquals(1, blocks.size());
    assertInstanceOf(ParagraphBlock.class, blocks.get(0));
    assertEquals(
        "Hello", blocks.get(0).asParagraph().getParagraph().getRichText().get(0).getPlainText());
  }

  @Test
  void paragraph_consumer() {
    List<Block> blocks =
        BlocksBuilder.of().paragraph(p -> p.text("Styled").bold().color(Color.RED)).build();

    assertEquals(1, blocks.size());
    ParagraphBlock p = blocks.get(0).asParagraph();
    assertTrue(p.getParagraph().getRichText().get(0).getAnnotations().getBold());
    assertEquals("red", p.getParagraph().getColor());
  }

  // heading1

  @Test
  void heading1_string() {
    List<Block> blocks = BlocksBuilder.of().heading1("Title").build();

    assertEquals(1, blocks.size());
    assertInstanceOf(HeadingOneBlock.class, blocks.get(0));
  }

  @Test
  void heading1_consumer() {
    List<Block> blocks =
        BlocksBuilder.of().heading1(h -> h.text("Toggleable").toggleable(true)).build();

    HeadingOneBlock h = (HeadingOneBlock) blocks.get(0);
    assertTrue(h.getHeading1().getIsToggleable());
  }

  // heading2

  @Test
  void heading2_string() {
    List<Block> blocks = BlocksBuilder.of().heading2("Subtitle").build();

    assertInstanceOf(HeadingTwoBlock.class, blocks.get(0));
  }

  @Test
  void heading2_consumer() {
    List<Block> blocks = BlocksBuilder.of().heading2(h -> h.text("H2").color(Color.BLUE)).build();

    HeadingTwoBlock h = (HeadingTwoBlock) blocks.get(0);
    assertEquals("blue", h.getHeading2().getColor());
  }

  // heading3

  @Test
  void heading3_string() {
    List<Block> blocks = BlocksBuilder.of().heading3("Section").build();

    assertInstanceOf(HeadingThreeBlock.class, blocks.get(0));
  }

  @Test
  void heading3_consumer() {
    List<Block> blocks = BlocksBuilder.of().heading3(h -> h.text("H3").toggleable(true)).build();

    HeadingThreeBlock h = (HeadingThreeBlock) blocks.get(0);
    assertTrue(h.getHeading3().getIsToggleable());
  }

  // heading4

  @Test
  void heading4_string() {
    List<Block> blocks = BlocksBuilder.of().heading4("Sub-section").build();

    assertInstanceOf(HeadingFourBlock.class, blocks.get(0));
  }

  @Test
  void heading4_consumer() {
    List<Block> blocks = BlocksBuilder.of().heading4(h -> h.text("H4").bold()).build();

    HeadingFourBlock h = (HeadingFourBlock) blocks.get(0);
    assertTrue(h.getHeading4().getRichText().get(0).getAnnotations().getBold());
  }

  // bulletedListItem

  @Test
  void bulletedListItem_string() {
    List<Block> blocks = BlocksBuilder.of().bulletedListItem("Point").build();

    assertInstanceOf(BulletedListItemBlock.class, blocks.get(0));
  }

  @Test
  void bulletedListItem_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .bulletedListItem(b -> b.text("Nested").children(c -> c.paragraph("Child")))
            .build();

    BulletedListItemBlock b = (BulletedListItemBlock) blocks.get(0);
    assertEquals(1, b.getBulletedListItem().getChildren().size());
  }

  // numberedListItem

  @Test
  void numberedListItem_string() {
    List<Block> blocks = BlocksBuilder.of().numberedListItem("Step 1").build();

    assertInstanceOf(NumberedListItemBlock.class, blocks.get(0));
  }

  @Test
  void numberedListItem_consumer() {
    List<Block> blocks =
        BlocksBuilder.of().numberedListItem(n -> n.text("Colored step").color(Color.GREEN)).build();

    NumberedListItemBlock n = (NumberedListItemBlock) blocks.get(0);
    assertEquals("green", n.getNumberedListItem().getColor());
  }

  // todo

  @Test
  void todo_string() {
    List<Block> blocks = BlocksBuilder.of().todo("Task").build();

    assertInstanceOf(ToDoBlock.class, blocks.get(0));
  }

  @Test
  void todo_consumer() {
    List<Block> blocks = BlocksBuilder.of().todo(t -> t.text("Done").checked()).build();

    ToDoBlock t = (ToDoBlock) blocks.get(0);
    assertTrue(t.getToDo().getChecked());
  }

  // toggle

  @Test
  void toggle_string() {
    List<Block> blocks = BlocksBuilder.of().toggle("Expand").build();

    assertInstanceOf(ToggleBlock.class, blocks.get(0));
  }

  @Test
  void toggle_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .toggle(t -> t.text("Toggle").children(c -> c.paragraph("Hidden")))
            .build();

    ToggleBlock t = (ToggleBlock) blocks.get(0);
    assertEquals(1, t.getToggle().getChildren().size());
  }

  // quote

  @Test
  void quote_string() {
    List<Block> blocks = BlocksBuilder.of().quote("Wisdom").build();

    assertInstanceOf(QuoteBlock.class, blocks.get(0));
  }

  @Test
  void quote_consumer() {
    List<Block> blocks = BlocksBuilder.of().quote(q -> q.text("Quote").italic()).build();

    QuoteBlock q = (QuoteBlock) blocks.get(0);
    assertTrue(q.getQuote().getRichText().get(0).getAnnotations().getItalic());
  }

  // callout

  @Test
  void callout_emojiAndText() {
    List<Block> blocks = BlocksBuilder.of().callout("⚠️", "Warning").build();

    CalloutBlock c = (CalloutBlock) blocks.get(0);
    assertEquals("⚠️", c.getCallout().getIcon().getEmoji());
    assertEquals("Warning", c.getCallout().getRichText().get(0).getPlainText());
  }

  @Test
  void callout_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .callout(c -> c.text("Info").emoji("ℹ️").color(Color.BLUE_BACKGROUND))
            .build();

    CalloutBlock c = (CalloutBlock) blocks.get(0);
    assertEquals("ℹ️", c.getCallout().getIcon().getEmoji());
    assertEquals("blue_background", c.getCallout().getColor());
  }

  // code

  @Test
  void code_contentAndLanguage() {
    List<Block> blocks = BlocksBuilder.of().code("x = 1", "python").build();

    CodeBlock c = (CodeBlock) blocks.get(0);
    assertEquals("python", c.getCode().getLanguage());
    assertEquals("x = 1", c.getCode().getRichText().get(0).getPlainText());
  }

  @Test
  void code_consumer() {
    List<Block> blocks = BlocksBuilder.of().code(c -> c.text("fn main()").language("rust")).build();

    CodeBlock c = (CodeBlock) blocks.get(0);
    assertEquals("rust", c.getCode().getLanguage());
  }

  // divider

  @Test
  void divider_addsDividerBlock() {
    List<Block> blocks = BlocksBuilder.of().divider().build();

    assertEquals(1, blocks.size());
    assertInstanceOf(DividerBlock.class, blocks.get(0));
  }

  // emptyLine

  @Test
  void emptyLine_addsEmptyParagraph() {
    List<Block> blocks = BlocksBuilder.of().emptyLine().build();

    assertEquals(1, blocks.size());
    assertInstanceOf(ParagraphBlock.class, blocks.get(0));
    assertEquals(
        "", blocks.get(0).asParagraph().getParagraph().getRichText().get(0).getPlainText());
  }

  // image

  @Test
  void image_fileData() {
    FileData data = FileData.external("https://example.com/img.png");
    List<Block> blocks = BlocksBuilder.of().image(data).build();

    assertInstanceOf(ImageBlock.class, blocks.get(0));
  }

  @Test
  void image_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .image(f -> f.external("https://example.com/img.png").type("external"))
            .build();

    assertInstanceOf(ImageBlock.class, blocks.get(0));
  }

  // video

  @Test
  void video_fileData() {
    FileData data = FileData.external("https://example.com/vid.mp4");
    List<Block> blocks = BlocksBuilder.of().video(data).build();

    assertInstanceOf(VideoBlock.class, blocks.get(0));
  }

  @Test
  void video_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .video(f -> f.external("https://example.com/vid.mp4").type("external"))
            .build();

    assertInstanceOf(VideoBlock.class, blocks.get(0));
  }

  // pdf

  @Test
  void pdf_fileData() {
    FileData data = FileData.external("https://example.com/doc.pdf");
    List<Block> blocks = BlocksBuilder.of().pdf(data).build();

    assertInstanceOf(PdfBlock.class, blocks.get(0));
  }

  @Test
  void pdf_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .pdf(f -> f.external("https://example.com/doc.pdf").type("external"))
            .build();

    assertInstanceOf(PdfBlock.class, blocks.get(0));
  }

  // audio

  @Test
  void audio_fileData() {
    FileData data = FileData.external("https://example.com/song.mp3");
    List<Block> blocks = BlocksBuilder.of().audio(data).build();

    assertInstanceOf(AudioBlock.class, blocks.get(0));
  }

  @Test
  void audio_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .audio(f -> f.external("https://example.com/song.mp3").type("external"))
            .build();

    assertInstanceOf(AudioBlock.class, blocks.get(0));
  }

  // file

  @Test
  void file_fileData() {
    FileData data = FileData.external("https://example.com/file.zip");
    List<Block> blocks = BlocksBuilder.of().file(data).build();

    assertInstanceOf(FileBlock.class, blocks.get(0));
  }

  @Test
  void file_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .file(f -> f.external("https://example.com/file.zip").type("external"))
            .build();

    assertInstanceOf(FileBlock.class, blocks.get(0));
  }

  // embed

  @Test
  void embed_url() {
    List<Block> blocks = BlocksBuilder.of().embed("https://youtube.com").build();

    assertInstanceOf(EmbedBlock.class, blocks.get(0));
    assertEquals("https://youtube.com", blocks.get(0).asEmbed().getEmbed().getUrl());
  }

  @Test
  void embed_consumer() {
    List<Block> blocks =
        BlocksBuilder.of().embed(e -> e.url("https://maps.google.com").caption("Map")).build();

    EmbedBlock e = (EmbedBlock) blocks.get(0);
    assertEquals("https://maps.google.com", e.getEmbed().getUrl());
    assertNotNull(e.getEmbed().getCaption());
  }

  // bookmark

  @Test
  void bookmark_url() {
    List<Block> blocks = BlocksBuilder.of().bookmark("https://notion.so").build();

    assertInstanceOf(BookmarkBlock.class, blocks.get(0));
    assertEquals("https://notion.so", blocks.get(0).asBookmark().getBookmark().getUrl());
  }

  @Test
  void bookmark_consumer() {
    List<Block> blocks =
        BlocksBuilder.of().bookmark(b -> b.url("https://example.com").caption("Site")).build();

    BookmarkBlock b = (BookmarkBlock) blocks.get(0);
    assertEquals("https://example.com", b.getBookmark().getUrl());
    assertNotNull(b.getBookmark().getCaption());
  }

  // breadcrumb

  @Test
  void breadcrumb_addsBreadcrumbBlock() {
    List<Block> blocks = BlocksBuilder.of().breadcrumb().build();

    assertInstanceOf(BreadcrumbBlock.class, blocks.get(0));
  }

  // equation

  @Test
  void equation_addsEquationBlock() {
    List<Block> blocks = BlocksBuilder.of().equation("E = mc^2").build();

    assertInstanceOf(EquationBlock.class, blocks.get(0));
    assertEquals("E = mc^2", blocks.get(0).asEquation().getEquation().getExpression());
  }

  // linkToPage, linkToDatabase, linkToComment

  @Test
  void linkToPage_addsLinkToPageBlock() {
    List<Block> blocks = BlocksBuilder.of().linkToPage("page-id").build();

    assertInstanceOf(LinkToPageBlock.class, blocks.get(0));
    assertEquals("page_id", blocks.get(0).asLinkToPage().getLinkToPage().getType());
  }

  @Test
  void linkToDatabase_addsLinkToDatabaseBlock() {
    List<Block> blocks = BlocksBuilder.of().linkToDatabase("db-id").build();

    assertInstanceOf(LinkToPageBlock.class, blocks.get(0));
    assertEquals("database_id", blocks.get(0).asLinkToPage().getLinkToPage().getType());
  }

  @Test
  void linkToComment_addsLinkToCommentBlock() {
    List<Block> blocks = BlocksBuilder.of().linkToComment("comment-id").build();

    assertInstanceOf(LinkToPageBlock.class, blocks.get(0));
    assertEquals("comment_id", blocks.get(0).asLinkToPage().getLinkToPage().getType());
  }

  @Test
  void linkToPage_consumer() {
    List<Block> blocks =
        BlocksBuilder.of().linkToPage(l -> l.getLinkToPage().setPageId("manual-id")).build();

    assertInstanceOf(LinkToPageBlock.class, blocks.get(0));
    assertEquals("manual-id", blocks.get(0).asLinkToPage().getLinkToPage().getPageId());
  }

  // tableOfContents

  @Test
  void tableOfContents_addsBlock() {
    List<Block> blocks = BlocksBuilder.of().tableOfContents().build();

    assertInstanceOf(TableOfContentsBlock.class, blocks.get(0));
  }

  @Test
  void tableOfContents_withColor() {
    List<Block> blocks = BlocksBuilder.of().tableOfContents(Color.GRAY).build();

    TableOfContentsBlock toc = (TableOfContentsBlock) blocks.get(0);
    assertEquals("gray", toc.getTableOfContents().getColor());
  }

  @Test
  void tableOfContents_withStringColor() {
    List<Block> blocks = BlocksBuilder.of().tableOfContents("blue").build();

    assertEquals(1, blocks.size());
    TableOfContentsBlock toc = (TableOfContentsBlock) blocks.get(0);
    assertEquals("blue", toc.getTableOfContents().getColor());
  }

  // table

  @Test
  void table_withDimensions() {
    List<Block> blocks = BlocksBuilder.of().table(3, true, false).build();

    assertInstanceOf(TableBlock.class, blocks.get(0));
    TableBlock t = (TableBlock) blocks.get(0);
    assertEquals(3, t.getTable().getTableWidth());
    assertTrue(t.getTable().isHasColumnHeader());
    assertFalse(t.getTable().isHasRowHeader());
  }

  @Test
  void table_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .table(t -> t.tableWidth(2).children(rows -> rows.row().cell("A").cell("B")))
            .build();

    assertInstanceOf(TableBlock.class, blocks.get(0));
    TableBlock t = (TableBlock) blocks.get(0);
    assertEquals(2, t.getTable().getTableWidth());
    assertEquals(1, t.getTable().getChildren().size());
  }

  // columns

  @Test
  void columns_withCount() {
    List<Block> blocks = BlocksBuilder.of().columns(3).build();

    assertInstanceOf(ColumnListBlock.class, blocks.get(0));
    ColumnListBlock cl = (ColumnListBlock) blocks.get(0);
    assertEquals(3, cl.getColumnList().getChildren().size());
  }

  @Test
  void columns_withCountLessThan2_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> BlocksBuilder.of().columns(1));
  }

  @Test
  void columns_consumer() {
    List<Block> blocks =
        BlocksBuilder.of()
            .columns(
                c -> c.column(col -> col.paragraph("Left")).column(col -> col.paragraph("Right")))
            .build();

    assertInstanceOf(ColumnListBlock.class, blocks.get(0));
    ColumnListBlock cl = (ColumnListBlock) blocks.get(0);
    assertEquals(2, cl.getColumnList().getChildren().size());
  }

  // Complex composition

  @Test
  void complexComposition_multipleBlockTypes() {
    List<Block> blocks =
        BlocksBuilder.of()
            .heading1("Project Plan")
            .paragraph("Introduction text.")
            .divider()
            .toggle(
                t ->
                    t.text("Details")
                        .children(
                            children ->
                                children
                                    .paragraph("Hidden content")
                                    .bulletedListItem("Point 1")
                                    .bulletedListItem("Point 2")))
            .callout("⚠️", "Don't forget the deadline!")
            .todo("Review PR")
            .build();

    assertEquals(6, blocks.size());
    assertInstanceOf(HeadingOneBlock.class, blocks.get(0));
    assertInstanceOf(ParagraphBlock.class, blocks.get(1));
    assertInstanceOf(DividerBlock.class, blocks.get(2));
    assertInstanceOf(ToggleBlock.class, blocks.get(3));
    assertInstanceOf(CalloutBlock.class, blocks.get(4));
    assertInstanceOf(ToDoBlock.class, blocks.get(5));

    ToggleBlock toggle = (ToggleBlock) blocks.get(3);
    assertEquals(3, toggle.getToggle().getChildren().size());
  }

  // Chaining returns same builder instance

  @Test
  void chainedMethods_returnSameInstance() {
    BlocksBuilder builder = BlocksBuilder.of();
    BlocksBuilder returned = builder.paragraph("Test");

    assertSame(builder, returned);
  }
}
