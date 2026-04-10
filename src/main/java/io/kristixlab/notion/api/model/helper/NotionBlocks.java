package io.kristixlab.notion.api.model.helper;

import static io.kristixlab.notion.api.model.helper.NotionText.plainText;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Static factory helpers for constructing common Notion block types.
 *
 * <p>Designed for use with a static import so call sites read as a concise DSL:
 *
 * <pre>{@code
 * import static io.kristixlab.notion.api.model.helper.Blocks.*;
 *
 * Block heading = heading1("Overview");
 * List<Block> content = blocksBuilder()
 *     .heading1("Overview")
 *     .paragraph("Introduction.")
 *     .todoList("Review design", "Write tests")
 *     .build();
 * }</pre>
 */
public class NotionBlocks {

  // Audio

  /**
   * Creates an audio block from a URL or file upload ID.
   *
   * @param audioRef an external URL or a file upload UUID string
   * @return a new AudioBlock
   */
  public static AudioBlock audio(String audioRef) {
    AudioBlock block = new AudioBlock();
    block.setAudio(resolveFileData(audioRef));
    return block;
  }

  public static AudioBlock audio(FileData fileData) {
    AudioBlock block = new AudioBlock();
    block.setAudio(fileData);
    return block;
  }

  public static AudioBlock audio(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return audio(builder.build());
  }

  public static ParagraphBlock blankLine() {
    return paragraph("");
  }

  // Bookmark

  public static BookmarkBlock bookmark(String url) {
    return BookmarkBlock.builder().url(url).build();
  }

  public static BookmarkBlock bookmark(Consumer<BookmarkBlock.Builder> consumer) {
    BookmarkBlock.Builder builder = BookmarkBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Breadcrumb

  public static BreadcrumbBlock breadcrumb() {
    return new BreadcrumbBlock();
  }

  // Bulleted list

  public static BulletedListItemBlock bullet(String text) {
    return bullet(plainText(text));
  }

  public static BulletedListItemBlock bullet(RichText... texts) {
    return bullet(Arrays.asList(texts));
  }

  public static BulletedListItemBlock bullet(List<RichText> texts) {
    return BulletedListItemBlock.builder().text(texts).build();
  }

  public static BulletedListItemBlock bullet(Consumer<BulletedListItemBlock.Builder> consumer) {
    BulletedListItemBlock.Builder builder = BulletedListItemBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static List<Block> bulletedList(String... items) {
    return bulletedList(Arrays.asList(items));
  }

  public static List<Block> bulletedList(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(bullet(item));
    }
    return blocks;
  }

  // Callout

  public static CalloutBlock callout(String text) {
    return callout(plainText(text));
  }

  public static CalloutBlock callout(RichText... texts) {
    return callout(Arrays.asList(texts));
  }

  public static CalloutBlock callout(List<RichText> texts) {
    return CalloutBlock.builder().text(texts).build();
  }

  public static CalloutBlock callout(Consumer<CalloutBlock.Builder> consumer) {
    CalloutBlock.Builder builder = CalloutBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static CalloutBlock callout(String emoji, String text) {
    return callout(Icon.emoji(emoji), text);
  }

  public static CalloutBlock callout(Icon emoji, String text) {
    return callout(emoji, plainText(text));
  }

  public static CalloutBlock callout(Icon emoji, RichText... texts) {
    return callout(emoji, Arrays.asList(texts));
  }

  public static CalloutBlock callout(Icon emoji, List<RichText> texts) {
    return CalloutBlock.builder().emoji(emoji).text(texts).build();
  }

  // Code

  public static CodeBlock code(String language, String code) {
    return CodeBlock.builder().language(language).code(code).build();
  }

  public static CodeBlock code(Consumer<CodeBlock.Builder> consumer) {
    CodeBlock.Builder builder = CodeBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Columns (layout)

  /** should always be wrapped in a {@link ColumnListBlock}, @see columns() */
  public static ColumnBlock column(List<Block> content) {
    return column(null, content);
  }

  /** should always be wrapped in a {@link ColumnListBlock}, @see columns() */
  public static ColumnBlock column(Double widthRatio, Block... content) {
    return column(widthRatio, Arrays.asList(content));
  }

  /** should always be wrapped in a {@link ColumnListBlock}, @see columns() */
  public static ColumnBlock column(Double widthRatio, List<Block> content) {
    ColumnBlock col = new ColumnBlock();
    col.getColumn().setChildren(new ArrayList<>(content));
    col.getColumn().setWidthRatio(widthRatio);
    return col;
  }

  /** should always be wrapped in a {@link ColumnListBlock}, @see columns() */
  public static ColumnBlock column(Consumer<NotionBlocksBuilder> content) {
    return column(null, content);
  }

  /** should always be wrapped in a {@link ColumnListBlock}, @see columns() */
  public static ColumnBlock column(Double widthRatio, Consumer<NotionBlocksBuilder> content) {
    NotionBlocksBuilder builder = blocksBuilder();
    content.accept(builder);
    ColumnBlock col = new ColumnBlock();
    col.getColumn().setChildren(builder.build());
    col.getColumn().setWidthRatio(widthRatio);
    return col;
  }

  public static ColumnListBlock columns(ColumnBlock... cols) {
    if (cols == null || cols.length < 2) {
      throw new IllegalArgumentException("At least two columns are required");
    }
    return columns(Arrays.asList(cols));
  }

  public static ColumnListBlock columns(List<Block>... columnContents) {
    if (columnContents == null || columnContents.length < 2) {
      throw new IllegalArgumentException("At least two columns are required");
    }
    List<ColumnBlock> columns = new ArrayList<>();
    for (List<Block> columnBlocks : columnContents) {
      ColumnBlock column = column(columnBlocks);
      columns.add(column);
    }
    return columns(columns);
  }

  public static ColumnListBlock columns(List<ColumnBlock> columns) {
    if (columns == null || columns.size() < 2) {
      throw new IllegalArgumentException("At least two columns are required");
    }
    ColumnListBlock block = new ColumnListBlock();
    block.getColumnList().setChildren(columns);
    return block;
  }

  public static ColumnListBlock columns(Consumer<NotionBlocksBuilder>... consumers) {
    if (consumers == null || consumers.length < 2) {
      throw new IllegalArgumentException("At least two columns are required");
    }
    List<Block>[] columnContents = new List[consumers.length];
    for (int i = 0; i < consumers.length; i++) {
      NotionBlocksBuilder contentBuilder = blocksBuilder();
      if (consumers[i] != null) {
        consumers[i].accept(contentBuilder);
      }
      columnContents[i] = contentBuilder.build();
    }
    return columns(columnContents);
  }

  // Divider

  public static DividerBlock divider() {
    return new DividerBlock();
  }

  // Embed

  public static EmbedBlock embed(String url) {
    return EmbedBlock.builder().url(url).build();
  }

  public static EmbedBlock embed(Consumer<EmbedBlock.Builder> consumer) {
    EmbedBlock.Builder builder = EmbedBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Equation

  public static EquationBlock equation(String expression) {
    EquationBlock block = new EquationBlock();
    block.getEquation().setExpression(expression);
    return block;
  }

  // File

  /**
   * Creates a file block from a URL or file upload ID.
   *
   * @param fileRef an external URL or a file upload UUID string
   * @return a new FileBlock
   */
  public static FileBlock file(String fileRef) {
    FileBlock block = new FileBlock();
    block.setFile(resolveFileData(fileRef));
    return block;
  }

  public static FileBlock file(FileData fileData) {
    FileBlock block = new FileBlock();
    block.setFile(fileData);
    return block;
  }

  public static FileBlock file(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return file(builder.build());
  }

  // Headings

  public static HeadingOneBlock heading1(String text) {
    return heading1(plainText(text));
  }

  public static HeadingOneBlock heading1(RichText... texts) {
    return heading1(Arrays.asList(texts));
  }

  public static HeadingOneBlock heading1(List<RichText> texts) {
    return HeadingOneBlock.builder().text(texts).build();
  }

  public static HeadingOneBlock heading1(Consumer<HeadingOneBlock.Builder> consumer) {
    HeadingOneBlock.Builder builder = HeadingOneBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static HeadingTwoBlock heading2(String text) {
    return heading2(plainText(text));
  }

  public static HeadingTwoBlock heading2(RichText... texts) {
    return heading2(Arrays.asList(texts));
  }

  public static HeadingTwoBlock heading2(List<RichText> texts) {
    return HeadingTwoBlock.builder().text(texts).build();
  }

  public static HeadingTwoBlock heading2(Consumer<HeadingTwoBlock.Builder> consumer) {
    HeadingTwoBlock.Builder builder = HeadingTwoBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static HeadingThreeBlock heading3(String text) {
    return heading3(plainText(text));
  }

  public static HeadingThreeBlock heading3(RichText... texts) {
    return heading3(Arrays.asList(texts));
  }

  public static HeadingThreeBlock heading3(List<RichText> texts) {
    return HeadingThreeBlock.builder().text(texts).build();
  }

  public static HeadingThreeBlock heading3(Consumer<HeadingThreeBlock.Builder> consumer) {
    HeadingThreeBlock.Builder builder = HeadingThreeBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static HeadingFourBlock heading4(String text) {
    return heading4(plainText(text));
  }

  public static HeadingFourBlock heading4(RichText... texts) {
    return heading4(Arrays.asList(texts));
  }

  public static HeadingFourBlock heading4(List<RichText> texts) {
    return HeadingFourBlock.builder().text(texts).build();
  }

  public static HeadingFourBlock heading4(Consumer<HeadingFourBlock.Builder> consumer) {
    HeadingFourBlock.Builder builder = HeadingFourBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Image

  /**
   * @param imageRef either external URL or file upload ID (UUID string)
   * @return
   */
  public static ImageBlock image(String imageRef) {
    ImageBlock block = new ImageBlock();
    block.setImage(resolveFileData(imageRef));
    return block;
  }

  public static ImageBlock image(FileData fileData) {
    ImageBlock block = new ImageBlock();
    block.setImage(fileData);
    return block;
  }

  public static ImageBlock image(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return image(builder.build());
  }

  // Links to blocks

  public static LinkToPageBlock linkToPage(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("page_id");
    block.getLinkToPage().setPageId(id);
    return block;
  }

  public static LinkToPageBlock linkToDatabase(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("database_id");
    block.getLinkToPage().setDatabaseId(id);
    return block;
  }

  public static LinkToPageBlock linkToComment(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("comment_id");
    block.getLinkToPage().setCommentId(id);
    return block;
  }

  // Numbered list

  public static NumberedListItemBlock numbered(String text) {
    return numbered(plainText(text));
  }

  public static NumberedListItemBlock numbered(RichText... texts) {
    return numbered(Arrays.asList(texts));
  }

  public static NumberedListItemBlock numbered(List<RichText> texts) {
    return NumberedListItemBlock.builder().text(texts).build();
  }

  public static NumberedListItemBlock numbered(Consumer<NumberedListItemBlock.Builder> consumer) {
    NumberedListItemBlock.Builder builder = NumberedListItemBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static List<Block> numberedList(String... items) {
    return numberedList(Arrays.asList(items));
  }

  public static List<Block> numberedList(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(numbered(item));
    }
    return blocks;
  }

  // Paragraph

  public static ParagraphBlock paragraph(String text) {
    return paragraph(plainText(text));
  }

  public static ParagraphBlock paragraph(RichText... texts) {
    return paragraph(Arrays.asList(texts));
  }

  public static ParagraphBlock paragraph(List<RichText> texts) {
    return ParagraphBlock.builder().text(texts).build();
  }

  public static ParagraphBlock paragraph(Consumer<ParagraphBlock.Builder> consumer) {
    ParagraphBlock.Builder builder = ParagraphBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static List<Block> paragraphList(String... items) {
    return paragraphList(Arrays.asList(items));
  }

  public static List<Block> paragraphList(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(paragraph(item));
    }
    return blocks;
  }

  // PDF

  /**
   * Creates a PDF block from a URL or file upload ID.
   *
   * @param pdfRef an external URL or a file upload UUID string
   * @return a new PdfBlock
   */
  public static PdfBlock pdf(String pdfRef) {
    PdfBlock block = new PdfBlock();
    block.setPdf(resolveFileData(pdfRef));
    return block;
  }

  public static PdfBlock pdf(FileData fileData) {
    PdfBlock block = new PdfBlock();
    block.setPdf(fileData);
    return block;
  }

  public static PdfBlock pdf(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return pdf(builder.build());
  }

  // Quote

  public static QuoteBlock quote(String text) {
    return quote(plainText(text));
  }

  public static QuoteBlock quote(RichText... texts) {
    return quote(Arrays.asList(texts));
  }

  public static QuoteBlock quote(List<RichText> texts) {
    return QuoteBlock.builder().text(texts).build();
  }

  public static QuoteBlock quote(Consumer<QuoteBlock.Builder> consumer) {
    QuoteBlock.Builder builder = QuoteBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Synced block

  public static SyncedBlock synced(Block... children) {
    return synced(Arrays.asList(children));
  }

  public static SyncedBlock synced(Consumer<NotionBlocksBuilder> consumer) {
    NotionBlocksBuilder builder = blocksBuilder();
    consumer.accept(builder);
    return synced(builder.build());
  }

  public static SyncedBlock synced(List<Block> children) {
    SyncedBlock block = new SyncedBlock();
    block.getSyncedBlock().setChildren(children);
    return block;
  }

  public static SyncedBlock syncedFrom(String blockId) {
    SyncedBlock block = new SyncedBlock();
    SyncedBlock.SyncedFrom syncedFrom = new SyncedBlock.SyncedFrom();
    syncedFrom.setBlockId(blockId);
    block.getSyncedBlock().setSyncedFrom(syncedFrom);
    return block;
  }

  // Plain table

  public static TableBlock table(Consumer<TableBlock.Builder> consumer) {
    TableBlock.Builder builder = TableBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static TableBlock table(TableRowBlock... rows) {
    return table(Arrays.asList(rows));
  }

  public static TableBlock table(List<TableRowBlock> rows) {
    TableBlock block = new TableBlock();
    block.setTable(new TableBlock.Table());
    int tableWidth = rows.isEmpty() ? 0 : rows.get(0).getTableRow().getCells().size();
    for (TableRowBlock row : rows) {
      if (row.getTableRow().getCells().size() != tableWidth) {
        throw new IllegalArgumentException(
            "All rows must have the same number of cells. Expected: "
                + tableWidth
                + ", but got: "
                + row.getTableRow().getCells().size());
      }
    }

    block.getTable().setTableWidth(tableWidth);
    block.getTable().setChildren(new ArrayList<>(rows));
    return block;
  }

  public static TableRowBlock tableRow(String... cellTexts) {
    return tableRow(Arrays.asList(cellTexts));
  }

  public static TableRowBlock tableRow(List<String> cellTexts) {
    TableRowBlock.Builder builder = TableRowBlock.builder().row();
    for (String cellText : cellTexts) {
      builder.cell(cellText);
    }
    return builder.build();
  }

  /**
   * Creates a {@link TableRowBlock} where each argument becomes one cell containing a single
   * formatted text run.
   *
   * <p>Use this overload to apply per-cell rich-text styling:
   *
   * <pre>{@code
   * tableRow(plainText("Mon").bold(), plainText("Tue"), plainText("Wed"))
   * }</pre>
   *
   * @param cells one {@link RichText} per cell
   * @return a new TableRowBlock
   */
  public static TableRowBlock tableRow(RichText... cells) {
    TableRowBlock row = new TableRowBlock();
    for (RichText cell : cells) {
      List<RichText> cellContent = new ArrayList<>();
      cellContent.add(cell);
      row.getTableRow().getCells().add(cellContent);
    }
    return row;
  }

  // Table of contents

  public static TableOfContentsBlock tableOfContents() {
    return new TableOfContentsBlock();
  }

  public static TableOfContentsBlock tableOfContents(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }
    return tableOfContents(color.getValue());
  }

  public static TableOfContentsBlock tableOfContents(String color) {
    TableOfContentsBlock block = new TableOfContentsBlock();
    block.getTableOfContents().setColor(color);
    return block;
  }

  // To-do

  public static ToDoBlock todo(String text) {
    return todo(plainText(text));
  }

  public static ToDoBlock todo(RichText... texts) {
    return todo(Arrays.asList(texts));
  }

  public static ToDoBlock todo(List<RichText> texts) {
    return ToDoBlock.builder().text(texts).build();
  }

  public static ToDoBlock todo(Consumer<ToDoBlock.Builder> consumer) {
    ToDoBlock.Builder builder = ToDoBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static List<Block> todoList(String... items) {
    return todoList(Arrays.asList(items));
  }

  public static List<Block> todoList(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(todo(item));
    }
    return blocks;
  }

  // Toggle

  public static ToggleBlock toggle(String text) {
    return toggle(plainText(text));
  }

  public static ToggleBlock toggle(RichText... texts) {
    return toggle(Arrays.asList(texts));
  }

  public static ToggleBlock toggle(List<RichText> texts) {
    return ToggleBlock.builder().text(texts).build();
  }

  public static ToggleBlock toggle(Consumer<ToggleBlock.Builder> consumer) {
    ToggleBlock.Builder builder = ToggleBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static List<Block> toggleList(String... items) {
    return toggleList(Arrays.asList(items));
  }

  public static List<Block> toggleList(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(toggle(item));
    }
    return blocks;
  }

  // Video

  /**
   * Creates a video block from a URL or file upload ID.
   *
   * @param videoRef an external URL or a file upload UUID string
   * @return a new VideoBlock
   */
  public static VideoBlock video(String videoRef) {
    VideoBlock block = new VideoBlock();
    block.setVideo(resolveFileData(videoRef));
    return block;
  }

  public static VideoBlock video(FileData fileData) {
    VideoBlock block = new VideoBlock();
    block.setVideo(fileData);
    return block;
  }

  public static VideoBlock video(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return video(builder.build());
  }

  public static NotionBlocksBuilder blocksBuilder() {
    return new NotionBlocksBuilder();
  }

  private static FileData resolveFileData(String ref) {
    try {
      UUID.fromString(ref);
      return FileData.builder().fileUpload(ref).build();
    } catch (IllegalArgumentException e) {
      return FileData.builder().externalUrl(ref).build();
    }
  }
}
