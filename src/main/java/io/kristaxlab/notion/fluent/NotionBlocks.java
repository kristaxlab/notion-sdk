package io.kristaxlab.notion.fluent;

import static io.kristaxlab.notion.fluent.NotionText.plainText;

import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.richtext.RichText;
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
 * import static io.kristaxlab.notion.model.helper.Blocks.*;
 *
 * Block heading = heading1("Overview");
 * List<Block> content = blocksBuilder()
 *     .heading1("Overview")
 *     .paragraph("Introduction.")
 *     .todos("Review design", "Write tests")
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

  /**
   * Creates an audio block from prebuilt file data.
   *
   * @param fileData file payload
   * @return audio block
   */
  public static AudioBlock audio(FileData fileData) {
    AudioBlock block = new AudioBlock();
    block.setAudio(fileData);
    return block;
  }

  /**
   * Creates an audio block by configuring {@link FileData.Builder}.
   *
   * @param consumer callback to configure file data
   * @return audio block
   */
  public static AudioBlock audio(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return audio(builder.build());
  }

  /**
   * Creates an empty paragraph block.
   *
   * @return blank paragraph block
   */
  public static ParagraphBlock blankLine() {
    return paragraph("");
  }

  // Bookmark

  /**
   * Creates a bookmark block for the provided URL.
   *
   * @param url bookmark URL
   * @return bookmark block
   */
  public static BookmarkBlock bookmark(String url) {
    return BookmarkBlock.builder().url(url).build();
  }

  /**
   * Creates a bookmark block by configuring its builder.
   *
   * @param consumer callback to configure bookmark builder
   * @return bookmark block
   */
  public static BookmarkBlock bookmark(Consumer<BookmarkBlock.Builder> consumer) {
    BookmarkBlock.Builder builder = BookmarkBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Breadcrumb

  /**
   * Creates a breadcrumb block.
   *
   * @return breadcrumb block
   */
  public static BreadcrumbBlock breadcrumb() {
    return new BreadcrumbBlock();
  }

  // Bulleted list

  /**
   * Creates a bulleted list item from plain text.
   *
   * @param text list item text
   * @return bulleted list item block
   */
  public static BulletedListItemBlock bullet(String text) {
    return bullet(plainText(text));
  }

  /**
   * Creates a bulleted list item from rich text fragments.
   *
   * @param texts rich text fragments
   * @return bulleted list item block
   */
  public static BulletedListItemBlock bullet(RichText... texts) {
    return bullet(Arrays.asList(texts));
  }

  /**
   * Creates a bulleted list item from rich text fragments.
   *
   * @param texts rich text fragments
   * @return bulleted list item block
   */
  public static BulletedListItemBlock bullet(List<RichText> texts) {
    return BulletedListItemBlock.builder().text(texts).build();
  }

  /**
   * Creates a bulleted list item by configuring its builder.
   *
   * @param consumer callback to configure block builder
   * @return bulleted list item block
   */
  public static BulletedListItemBlock bullet(Consumer<BulletedListItemBlock.Builder> consumer) {
    BulletedListItemBlock.Builder builder = BulletedListItemBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates multiple bulleted list items from plain text values.
   *
   * @param items list item texts
   * @return list of bullet blocks
   */
  public static List<Block> bullets(String... items) {
    return bullets(Arrays.asList(items));
  }

  /**
   * Creates multiple bulleted list items from plain text values.
   *
   * @param items list item texts
   * @return list of bullet blocks
   */
  public static List<Block> bullets(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(bullet(item));
    }
    return blocks;
  }

  // Callout

  /**
   * Creates a callout block from plain text.
   *
   * @param text callout text
   * @return callout block
   */
  public static CalloutBlock callout(String text) {
    return callout(plainText(text));
  }

  /**
   * Creates a callout block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return callout block
   */
  public static CalloutBlock callout(RichText... texts) {
    return callout(Arrays.asList(texts));
  }

  /**
   * Creates a callout block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return callout block
   */
  public static CalloutBlock callout(List<RichText> texts) {
    return CalloutBlock.builder().text(texts).build();
  }

  /**
   * Creates a callout block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return callout block
   */
  public static CalloutBlock callout(Consumer<CalloutBlock.Builder> consumer) {
    CalloutBlock.Builder builder = CalloutBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a callout block with an emoji icon and plain text content.
   *
   * @param emoji emoji icon
   * @param text callout text
   * @return callout block
   */
  public static CalloutBlock callout(String emoji, String text) {
    return callout(Icon.emoji(emoji), text);
  }

  /**
   * Creates a callout block with an icon and plain text content.
   *
   * @param emoji icon payload
   * @param text callout text
   * @return callout block
   */
  public static CalloutBlock callout(Icon emoji, String text) {
    return callout(emoji, plainText(text));
  }

  /**
   * Creates a callout block with an icon and rich text content.
   *
   * @param emoji icon payload
   * @param texts rich text fragments
   * @return callout block
   */
  public static CalloutBlock callout(Icon emoji, RichText... texts) {
    return callout(emoji, Arrays.asList(texts));
  }

  /**
   * Creates a callout block with an icon and rich text content.
   *
   * @param emoji icon payload
   * @param texts rich text fragments
   * @return callout block
   */
  public static CalloutBlock callout(Icon emoji, List<RichText> texts) {
    return CalloutBlock.builder().emoji(emoji).text(texts).build();
  }

  // Code

  /**
   * Creates a code block with language and code string.
   *
   * @param language language token
   * @param code code content
   * @return code block
   */
  public static CodeBlock code(String language, String code) {
    return CodeBlock.builder().language(language).code(code).build();
  }

  /**
   * Creates a code block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return code block
   */
  public static CodeBlock code(Consumer<CodeBlock.Builder> consumer) {
    CodeBlock.Builder builder = CodeBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Columns (layout)

  /**
   * Creates a column from prebuilt block content.
   *
   * <p>The resulting column should be wrapped in a {@link ColumnListBlock}; see {@code
   * columns(...)} helpers.
   *
   * @param content column children
   * @return column block
   */
  public static ColumnBlock column(List<Block> content) {
    return column(null, content);
  }

  /**
   * Creates a column with an optional width ratio.
   *
   * <p>The resulting column should be wrapped in a {@link ColumnListBlock}; see {@code
   * columns(...)} helpers.
   *
   * @param widthRatio optional ratio used by Notion for column width distribution
   * @param content column children
   * @return column block
   */
  public static ColumnBlock column(Double widthRatio, Block... content) {
    return column(widthRatio, Arrays.asList(content));
  }

  /**
   * Creates a column with an optional width ratio.
   *
   * <p>The resulting column should be wrapped in a {@link ColumnListBlock}; see {@code
   * columns(...)} helpers.
   *
   * @param widthRatio optional ratio used by Notion for column width distribution
   * @param content column children
   * @return column block
   */
  public static ColumnBlock column(Double widthRatio, List<Block> content) {
    ColumnBlock col = new ColumnBlock();
    col.getColumn().setChildren(new ArrayList<>(content));
    col.getColumn().setWidthRatio(widthRatio);
    return col;
  }

  /**
   * Creates a column by building its children via the block DSL.
   *
   * <p>The resulting column should be wrapped in a {@link ColumnListBlock}; see {@code
   * columns(...)} helpers.
   *
   * @param content callback used to build column children
   * @return column block
   */
  public static ColumnBlock column(Consumer<NotionBlocksBuilder> content) {
    return column(null, content);
  }

  /**
   * Creates a column with optional width ratio and DSL-built children.
   *
   * <p>The resulting column should be wrapped in a {@link ColumnListBlock}; see {@code
   * columns(...)} helpers.
   *
   * @param widthRatio optional ratio used by Notion for column width distribution
   * @param content callback used to build column children
   * @return column block
   */
  public static ColumnBlock column(Double widthRatio, Consumer<NotionBlocksBuilder> content) {
    NotionBlocksBuilder builder = blocksBuilder();
    content.accept(builder);
    ColumnBlock col = new ColumnBlock();
    col.getColumn().setChildren(builder.build());
    col.getColumn().setWidthRatio(widthRatio);
    return col;
  }

  /**
   * Creates a column list from prepared column blocks.
   *
   * @param cols column blocks
   * @return column list block
   */
  public static ColumnListBlock columns(ColumnBlock... cols) {
    if (cols == null || cols.length < 2) {
      throw new IllegalArgumentException("At least two columns are required");
    }
    return columns(Arrays.asList(cols));
  }

  /**
   * Creates a column list from per-column child block lists.
   *
   * @param columnContents content list for each column
   * @return column list block
   */
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

  /**
   * Creates a column list from prepared column blocks.
   *
   * @param columns prepared columns
   * @return column list block
   */
  public static ColumnListBlock columns(List<ColumnBlock> columns) {
    if (columns == null || columns.size() < 2) {
      throw new IllegalArgumentException("At least two columns are required");
    }
    ColumnListBlock block = new ColumnListBlock();
    block.getColumnList().setChildren(columns);
    return block;
  }

  /**
   * Creates a column list by building each column with a DSL callback.
   *
   * @param consumers column content builders
   * @return column list block
   */
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

  /**
   * Creates a divider block.
   *
   * @return divider block
   */
  public static DividerBlock divider() {
    return new DividerBlock();
  }

  // Embed

  /**
   * Creates an embed block for a URL.
   *
   * @param url embed URL
   * @return embed block
   */
  public static EmbedBlock embed(String url) {
    return EmbedBlock.builder().url(url).build();
  }

  /**
   * Creates an embed block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return embed block
   */
  public static EmbedBlock embed(Consumer<EmbedBlock.Builder> consumer) {
    EmbedBlock.Builder builder = EmbedBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Equation

  /**
   * Creates an equation block from a KaTeX expression.
   *
   * @param expression KaTeX expression
   * @return equation block
   */
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

  /**
   * Creates a file block from prebuilt file data.
   *
   * @param fileData file payload
   * @return file block
   */
  public static FileBlock file(FileData fileData) {
    FileBlock block = new FileBlock();
    block.setFile(fileData);
    return block;
  }

  /**
   * Creates a file block by configuring {@link FileData.Builder}.
   *
   * @param consumer callback to configure file data
   * @return file block
   */
  public static FileBlock file(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return file(builder.build());
  }

  // Headings

  /**
   * Creates a heading-1 block from plain text.
   *
   * @param text heading text
   * @return heading-1 block
   */
  public static HeadingOneBlock heading1(String text) {
    return heading1(plainText(text));
  }

  /**
   * Creates a heading-1 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-1 block
   */
  public static HeadingOneBlock heading1(RichText... texts) {
    return heading1(Arrays.asList(texts));
  }

  /**
   * Creates a heading-1 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-1 block
   */
  public static HeadingOneBlock heading1(List<RichText> texts) {
    return HeadingOneBlock.builder().text(texts).build();
  }

  /**
   * Creates a heading-1 block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return heading-1 block
   */
  public static HeadingOneBlock heading1(Consumer<HeadingOneBlock.Builder> consumer) {
    HeadingOneBlock.Builder builder = HeadingOneBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a heading-2 block from plain text.
   *
   * @param text heading text
   * @return heading-2 block
   */
  public static HeadingTwoBlock heading2(String text) {
    return heading2(plainText(text));
  }

  /**
   * Creates a heading-2 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-2 block
   */
  public static HeadingTwoBlock heading2(RichText... texts) {
    return heading2(Arrays.asList(texts));
  }

  /**
   * Creates a heading-2 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-2 block
   */
  public static HeadingTwoBlock heading2(List<RichText> texts) {
    return HeadingTwoBlock.builder().text(texts).build();
  }

  /**
   * Creates a heading-2 block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return heading-2 block
   */
  public static HeadingTwoBlock heading2(Consumer<HeadingTwoBlock.Builder> consumer) {
    HeadingTwoBlock.Builder builder = HeadingTwoBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a heading-3 block from plain text.
   *
   * @param text heading text
   * @return heading-3 block
   */
  public static HeadingThreeBlock heading3(String text) {
    return heading3(plainText(text));
  }

  /**
   * Creates a heading-3 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-3 block
   */
  public static HeadingThreeBlock heading3(RichText... texts) {
    return heading3(Arrays.asList(texts));
  }

  /**
   * Creates a heading-3 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-3 block
   */
  public static HeadingThreeBlock heading3(List<RichText> texts) {
    return HeadingThreeBlock.builder().text(texts).build();
  }

  /**
   * Creates a heading-3 block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return heading-3 block
   */
  public static HeadingThreeBlock heading3(Consumer<HeadingThreeBlock.Builder> consumer) {
    HeadingThreeBlock.Builder builder = HeadingThreeBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a heading-4 block from plain text.
   *
   * @param text heading text
   * @return heading-4 block
   */
  public static HeadingFourBlock heading4(String text) {
    return heading4(plainText(text));
  }

  /**
   * Creates a heading-4 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-4 block
   */
  public static HeadingFourBlock heading4(RichText... texts) {
    return heading4(Arrays.asList(texts));
  }

  /**
   * Creates a heading-4 block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return heading-4 block
   */
  public static HeadingFourBlock heading4(List<RichText> texts) {
    return HeadingFourBlock.builder().text(texts).build();
  }

  /**
   * Creates a heading-4 block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return heading-4 block
   */
  public static HeadingFourBlock heading4(Consumer<HeadingFourBlock.Builder> consumer) {
    HeadingFourBlock.Builder builder = HeadingFourBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Image

  /**
   * Creates an image block from a URL or file upload ID.
   *
   * @param imageRef either external URL or file upload ID (UUID string)
   * @return a new image block
   */
  public static ImageBlock image(String imageRef) {
    ImageBlock block = new ImageBlock();
    block.setImage(resolveFileData(imageRef));
    return block;
  }

  /**
   * Creates an image block from prebuilt file data.
   *
   * @param fileData file payload
   * @return image block
   */
  public static ImageBlock image(FileData fileData) {
    ImageBlock block = new ImageBlock();
    block.setImage(fileData);
    return block;
  }

  /**
   * Creates an image block by configuring {@link FileData.Builder}.
   *
   * @param consumer callback to configure file data
   * @return image block
   */
  public static ImageBlock image(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return image(builder.build());
  }

  // Links to blocks

  /**
   * Creates a block linking to a page.
   *
   * @param id page id
   * @return link-to-page block
   */
  public static LinkToPageBlock linkToPage(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("page_id");
    block.getLinkToPage().setPageId(id);
    return block;
  }

  /**
   * Creates a block linking to a database.
   *
   * @param id database id
   * @return link-to-page block
   */
  public static LinkToPageBlock linkToDatabase(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("database_id");
    block.getLinkToPage().setDatabaseId(id);
    return block;
  }

  /**
   * Creates a block linking to a comment.
   *
   * @param id comment id
   * @return link-to-page block
   */
  public static LinkToPageBlock linkToComment(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("comment_id");
    block.getLinkToPage().setCommentId(id);
    return block;
  }

  // Numbered list

  /**
   * Creates a numbered list item from plain text.
   *
   * @param text list item text
   * @return numbered list item block
   */
  public static NumberedListItemBlock numbered(String text) {
    return numbered(plainText(text));
  }

  /**
   * Creates a numbered list item from rich text fragments.
   *
   * @param texts rich text fragments
   * @return numbered list item block
   */
  public static NumberedListItemBlock numbered(RichText... texts) {
    return numbered(Arrays.asList(texts));
  }

  /**
   * Creates a numbered list item from rich text fragments.
   *
   * @param texts rich text fragments
   * @return numbered list item block
   */
  public static NumberedListItemBlock numbered(List<RichText> texts) {
    return NumberedListItemBlock.builder().text(texts).build();
  }

  /**
   * Creates a numbered list item by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return numbered list item block
   */
  public static NumberedListItemBlock numbered(Consumer<NumberedListItemBlock.Builder> consumer) {
    NumberedListItemBlock.Builder builder = NumberedListItemBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates multiple numbered list items from plain text values.
   *
   * @param items list item texts
   * @return list of numbered blocks
   */
  public static List<Block> numberedItems(String... items) {
    return numberedItems(Arrays.asList(items));
  }

  /**
   * Creates multiple numbered list items from plain text values.
   *
   * @param items list item texts
   * @return list of numbered blocks
   */
  public static List<Block> numberedItems(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(numbered(item));
    }
    return blocks;
  }

  // Paragraph

  /**
   * Creates a paragraph block from plain text.
   *
   * @param text paragraph text
   * @return paragraph block
   */
  public static ParagraphBlock paragraph(String text) {
    return paragraph(plainText(text));
  }

  /**
   * Creates a paragraph block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return paragraph block
   */
  public static ParagraphBlock paragraph(RichText... texts) {
    return paragraph(Arrays.asList(texts));
  }

  /**
   * Creates a paragraph block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return paragraph block
   */
  public static ParagraphBlock paragraph(List<RichText> texts) {
    return ParagraphBlock.builder().text(texts).build();
  }

  /**
   * Creates a paragraph block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return paragraph block
   */
  public static ParagraphBlock paragraph(Consumer<ParagraphBlock.Builder> consumer) {
    ParagraphBlock.Builder builder = ParagraphBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates multiple paragraph blocks from plain text values.
   *
   * @param items paragraph texts
   * @return list of paragraph blocks
   */
  public static List<Block> paragraphList(String... items) {
    return paragraphList(Arrays.asList(items));
  }

  /**
   * Creates multiple paragraph blocks from plain text values.
   *
   * @param items paragraph texts
   * @return list of paragraph blocks
   */
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

  /**
   * Creates a PDF block from prebuilt file data.
   *
   * @param fileData file payload
   * @return PDF block
   */
  public static PdfBlock pdf(FileData fileData) {
    PdfBlock block = new PdfBlock();
    block.setPdf(fileData);
    return block;
  }

  /**
   * Creates a PDF block by configuring {@link FileData.Builder}.
   *
   * @param consumer callback to configure file data
   * @return PDF block
   */
  public static PdfBlock pdf(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return pdf(builder.build());
  }

  // Quote

  /**
   * Creates a quote block from plain text.
   *
   * @param text quote text
   * @return quote block
   */
  public static QuoteBlock quote(String text) {
    return quote(plainText(text));
  }

  /**
   * Creates a quote block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return quote block
   */
  public static QuoteBlock quote(RichText... texts) {
    return quote(Arrays.asList(texts));
  }

  /**
   * Creates a quote block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return quote block
   */
  public static QuoteBlock quote(List<RichText> texts) {
    return QuoteBlock.builder().text(texts).build();
  }

  /**
   * Creates a quote block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return quote block
   */
  public static QuoteBlock quote(Consumer<QuoteBlock.Builder> consumer) {
    QuoteBlock.Builder builder = QuoteBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  // Synced block

  /**
   * Creates a synced block with child blocks.
   *
   * @param children child blocks
   * @return synced block
   */
  public static SyncedBlock synced(Block... children) {
    return synced(Arrays.asList(children));
  }

  /**
   * Creates a synced block by building children with the DSL.
   *
   * @param consumer callback to build children
   * @return synced block
   */
  public static SyncedBlock synced(Consumer<NotionBlocksBuilder> consumer) {
    NotionBlocksBuilder builder = blocksBuilder();
    consumer.accept(builder);
    return synced(builder.build());
  }

  /**
   * Creates a synced block with child blocks.
   *
   * @param children child blocks
   * @return synced block
   */
  public static SyncedBlock synced(List<Block> children) {
    SyncedBlock block = new SyncedBlock();
    block.getSyncedBlock().setChildren(children);
    return block;
  }

  /**
   * Creates a synced block reference to another synced block.
   *
   * @param blockId original synced block id
   * @return synced block reference
   */
  public static SyncedBlock syncedFrom(String blockId) {
    SyncedBlock block = new SyncedBlock();
    SyncedBlock.SyncedFrom syncedFrom = new SyncedBlock.SyncedFrom();
    syncedFrom.setBlockId(blockId);
    block.getSyncedBlock().setSyncedFrom(syncedFrom);
    return block;
  }

  // Plain table

  /**
   * Creates a table block by configuring its builder.
   *
   * @param consumer callback to configure table builder
   * @return table block
   */
  public static TableBlock table(Consumer<TableBlock.Builder> consumer) {
    TableBlock.Builder builder = TableBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a table from row blocks.
   *
   * @param rows row blocks
   * @return table block
   */
  public static TableBlock table(TableRowBlock... rows) {
    return table(Arrays.asList(rows));
  }

  /**
   * Creates a table from row blocks.
   *
   * @param rows row blocks
   * @return table block
   */
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

  /**
   * Creates a table row from plain cell strings.
   *
   * @param cellTexts cell values
   * @return table row block
   */
  public static TableRowBlock tableRow(String... cellTexts) {
    return tableRow(Arrays.asList(cellTexts));
  }

  /**
   * Creates a table row from plain cell strings.
   *
   * @param cellTexts cell values
   * @return table row block
   */
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

  /**
   * Creates a table-of-contents block with default color.
   *
   * @return table-of-contents block
   */
  public static TableOfContentsBlock tableOfContents() {
    return new TableOfContentsBlock();
  }

  /**
   * Creates a table-of-contents block with a color enum.
   *
   * @param color color enum
   * @return table-of-contents block
   */
  public static TableOfContentsBlock tableOfContents(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }
    return tableOfContents(color.getValue());
  }

  /**
   * Creates a table-of-contents block with a raw color token.
   *
   * @param color color token
   * @return table-of-contents block
   */
  public static TableOfContentsBlock tableOfContents(String color) {
    TableOfContentsBlock block = new TableOfContentsBlock();
    block.getTableOfContents().setColor(color);
    return block;
  }

  // To-do

  /**
   * Creates a to-do block from plain text.
   *
   * @param text to-do text
   * @return to-do block
   */
  public static ToDoBlock todo(String text) {
    return todo(plainText(text));
  }

  /**
   * Creates a to-do block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return to-do block
   */
  public static ToDoBlock todo(RichText... texts) {
    return todo(Arrays.asList(texts));
  }

  /**
   * Creates a to-do block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return to-do block
   */
  public static ToDoBlock todo(List<RichText> texts) {
    return ToDoBlock.builder().text(texts).build();
  }

  /**
   * Creates a to-do block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return to-do block
   */
  public static ToDoBlock todo(Consumer<ToDoBlock.Builder> consumer) {
    ToDoBlock.Builder builder = ToDoBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates multiple to-do blocks from plain text values.
   *
   * @param items to-do texts
   * @return list of to-do blocks
   */
  public static List<Block> todos(String... items) {
    return todos(Arrays.asList(items));
  }

  /**
   * Creates multiple to-do blocks from plain text values.
   *
   * @param items to-do texts
   * @return list of to-do blocks
   */
  public static List<Block> todos(List<String> items) {
    List<Block> blocks = new ArrayList<>();
    for (String item : items) {
      blocks.add(todo(item));
    }
    return blocks;
  }

  // Toggle

  /**
   * Creates a toggle block from plain text.
   *
   * @param text toggle text
   * @return toggle block
   */
  public static ToggleBlock toggle(String text) {
    return toggle(plainText(text));
  }

  /**
   * Creates a toggle block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return toggle block
   */
  public static ToggleBlock toggle(RichText... texts) {
    return toggle(Arrays.asList(texts));
  }

  /**
   * Creates a toggle block from rich text fragments.
   *
   * @param texts rich text fragments
   * @return toggle block
   */
  public static ToggleBlock toggle(List<RichText> texts) {
    return ToggleBlock.builder().text(texts).build();
  }

  /**
   * Creates a toggle block by configuring its builder.
   *
   * @param consumer callback to configure builder
   * @return toggle block
   */
  public static ToggleBlock toggle(Consumer<ToggleBlock.Builder> consumer) {
    ToggleBlock.Builder builder = ToggleBlock.builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates multiple toggle blocks from plain text values.
   *
   * @param items toggle texts
   * @return list of toggle blocks
   */
  public static List<Block> toggles(String... items) {
    return toggles(Arrays.asList(items));
  }

  /**
   * Creates multiple toggle blocks from plain text values.
   *
   * @param items toggle texts
   * @return list of toggle blocks
   */
  public static List<Block> toggles(List<String> items) {
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

  /**
   * Creates a video block from prebuilt file data.
   *
   * @param fileData file payload
   * @return video block
   */
  public static VideoBlock video(FileData fileData) {
    VideoBlock block = new VideoBlock();
    block.setVideo(fileData);
    return block;
  }

  /**
   * Creates a video block by configuring {@link FileData.Builder}.
   *
   * @param consumer callback to configure file data
   * @return video block
   */
  public static VideoBlock video(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    return video(builder.build());
  }

  /**
   * Creates a fluent block builder.
   *
   * @return new block builder
   */
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
