package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.blocks.*;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.FileData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fluent builder for composing a list of Notion blocks with support for nesting.
 *
 * <p>Provides shorthand methods for common block types and {@link Consumer}-based lambdas for
 * expressing parent–child relationships inline.
 *
 * <pre>{@code
 * List<Block> content = BlocksBuilder.of()
 *     .heading1("Project Plan")
 *     .paragraph("Introduction text.")
 *     .divider()
 *     .toggle("Details", children -> children
 *         .paragraph("Hidden content")
 *         .bulletedListItem("Point 1")
 *         .bulletedListItem("Point 2")
 *     )
 *     .callout("⚠️", "Don't forget the deadline!")
 *     .todo("Review PR")
 *     .build();
 *
 * notion.blocks().appendChildren(pageId, content);
 * }</pre>
 */
public class BlocksBuilder {

  private final List<Block> blocks = new ArrayList<>();

  protected BlocksBuilder() {}

  /**
   * Creates a new builder.
   *
   * @return a new empty builder
   */
  public static BlocksBuilder of() {
    return new BlocksBuilder();
  }

  /**
   * Adds a pre-built block. Use this escape hatch when shorthand methods don't cover your use case.
   *
   * @param block the block to add
   * @return this builder
   */
  public BlocksBuilder add(Block block) {
    blocks.add(block);
    return this;
  }

  /**
   * Adds multiple pre-built blocks.
   *
   * @param blocks the blocks to add
   * @return this builder
   */
  public BlocksBuilder addAll(List<Block> blocks) {
    this.blocks.addAll(blocks);
    return this;
  }

  /**
   * Adds multiple pre-built blocks (varargs).
   *
   * @param blocks the blocks to add
   * @return this builder
   */
  public BlocksBuilder addAll(Block... blocks) {
    Collections.addAll(this.blocks, blocks);
    return this;
  }

  /**
   * Builds and returns the list of blocks.
   *
   * @return an unmodifiable list of blocks
   */
  public List<Block> build() {
    return new ArrayList<>(blocks);
  }

  // Paragraph

  public BlocksBuilder paragraph(String text) {
    blocks.add(ParagraphBlock.of(text));
    return this;
  }

  public BlocksBuilder paragraph(Consumer<ParagraphBlock.Builder> consumer) {
    ParagraphBlock.Builder builder = ParagraphBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder heading1(String text) {
    blocks.add(HeadingOneBlock.of(text));
    return this;
  }

  public BlocksBuilder heading1(Consumer<HeadingOneBlock.Builder> consumer) {
    HeadingOneBlock.Builder builder = HeadingOneBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder heading2(String text) {
    blocks.add(HeadingTwoBlock.of(text));
    return this;
  }

  public BlocksBuilder heading2(Consumer<HeadingTwoBlock.Builder> consumer) {
    HeadingTwoBlock.Builder builder = HeadingTwoBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder heading3(String text) {
    blocks.add(HeadingThreeBlock.of(text));
    return this;
  }

  public BlocksBuilder heading3(Consumer<HeadingThreeBlock.Builder> consumer) {
    HeadingThreeBlock.Builder builder = HeadingThreeBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder heading4(String text) {
    blocks.add(HeadingFourBlock.of(text));
    return this;
  }

  public BlocksBuilder heading4(Consumer<HeadingFourBlock.Builder> consumer) {
    HeadingFourBlock.Builder builder = HeadingFourBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder bulletedListItem(String text) {
    blocks.add(BulletedListItemBlock.of(text));
    return this;
  }

  public BlocksBuilder bulletedListItem(Consumer<BulletedListItemBlock.Builder> consumer) {
    BulletedListItemBlock.Builder builder = BulletedListItemBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder numberedListItem(String text) {
    blocks.add(NumberedListItemBlock.of(text));
    return this;
  }

  public BlocksBuilder numberedListItem(Consumer<NumberedListItemBlock.Builder> consumer) {
    NumberedListItemBlock.Builder builder = NumberedListItemBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder todo(String text) {
    blocks.add(ToDoBlock.of(text));
    return this;
  }

  public BlocksBuilder todo(Consumer<ToDoBlock.Builder> consumer) {
    ToDoBlock.Builder builder = ToDoBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder toggle(String text) {
    blocks.add(ToggleBlock.of(text));
    return this;
  }

  public BlocksBuilder toggle(Consumer<ToggleBlock.Builder> consumer) {
    ToggleBlock.Builder builder = ToggleBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder quote(String text) {
    blocks.add(QuoteBlock.of(text));
    return this;
  }

  public BlocksBuilder quote(Consumer<QuoteBlock.Builder> consumer) {
    QuoteBlock.Builder builder = QuoteBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder callout(String emoji, String text) {
    blocks.add(CalloutBlock.of(emoji, text));
    return this;
  }

  public BlocksBuilder callout(Consumer<CalloutBlock.Builder> consumer) {
    CalloutBlock.Builder builder = CalloutBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder code(String content, String language) {
    blocks.add(CodeBlock.of(content, language));
    return this;
  }

  public BlocksBuilder code(Consumer<CodeBlock.Builder> consumer) {
    CodeBlock.Builder builder = CodeBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder divider() {
    blocks.add(new DividerBlock());
    return this;
  }

  public BlocksBuilder emptyLine() {
    blocks.add(ParagraphBlock.of(""));
    return this;
  }

  public BlocksBuilder image(FileData imageData) {
    blocks.add(ImageBlock.of(imageData));
    return this;
  }

  public BlocksBuilder image(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    ImageBlock block = ImageBlock.of(builder.build());
    blocks.add(block);
    return this;
  }

  public BlocksBuilder video(FileData videoData) {
    blocks.add(VideoBlock.of(videoData));
    return this;
  }

  public BlocksBuilder video(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    VideoBlock block = VideoBlock.of(builder.build());
    blocks.add(block);
    return this;
  }

  public BlocksBuilder pdf(FileData pdfData) {
    blocks.add(PdfBlock.of(pdfData));
    return this;
  }

  public BlocksBuilder pdf(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    PdfBlock block = PdfBlock.of(builder.build());
    blocks.add(block);
    return this;
  }

  public BlocksBuilder audio(FileData audioData) {
    blocks.add(AudioBlock.of(audioData));
    return this;
  }

  public BlocksBuilder audio(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    AudioBlock block = AudioBlock.of(builder.build());
    blocks.add(block);
    return this;
  }

  public BlocksBuilder file(FileData pdfData) {
    blocks.add(FileBlock.of(pdfData));
    return this;
  }

  public BlocksBuilder file(Consumer<FileData.Builder> consumer) {
    FileData.Builder builder = FileData.builder();
    consumer.accept(builder);
    FileBlock block = FileBlock.of(builder.build());
    blocks.add(block);
    return this;
  }

  public BlocksBuilder embed(String url) {
    blocks.add(EmbedBlock.of(url));
    return this;
  }

  public BlocksBuilder embed(Consumer<EmbedBlock.Builder> consumer) {
    EmbedBlock.Builder builder = EmbedBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder bookmark(String url) {
    blocks.add(BookmarkBlock.of(url));
    return this;
  }

  public BlocksBuilder bookmark(Consumer<BookmarkBlock.Builder> consumer) {
    BookmarkBlock.Builder builder = BookmarkBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder breadcrumb() {
    blocks.add(BreadcrumbBlock.of());
    return this;
  }

  public BlocksBuilder equation(String expression) {
    blocks.add(EquationBlock.of(expression));
    return this;
  }

  public BlocksBuilder linkToPage(String pageId) {
    blocks.add(LinkToPageBlock.pageLink(pageId));
    return this;
  }

  public BlocksBuilder linkToDatabase(String databaseId) {
    blocks.add(LinkToPageBlock.databaseLink(databaseId));
    return this;
  }

  public BlocksBuilder linkToComment(String commentId) {
    blocks.add(LinkToPageBlock.commentLink(commentId));
    return this;
  }

  public BlocksBuilder linkToPage(Consumer<LinkToPageBlock> consumer) {
    LinkToPageBlock block = new LinkToPageBlock();
    consumer.accept(block);
    blocks.add(block);
    return this;
  }

  public BlocksBuilder tableOfContents() {
    blocks.add(TableOfContentsBlock.of());
    return this;
  }

  public BlocksBuilder table(int tableWidth, boolean hasColumnHeader, boolean hasRowHeader) {
    TableBlock block =
        TableBlock.builder()
            .tableWidth(tableWidth)
            .hasColumnHeader(hasColumnHeader)
            .hasRowHeader(hasRowHeader)
            .build();
    blocks.add(block);
    return this;
  }

  public BlocksBuilder table(Consumer<TableBlock.Builder> consumer) {
    TableBlock.Builder builder = TableBlock.builder();
    consumer.accept(builder);
    blocks.add(builder.build());
    return this;
  }

  public BlocksBuilder columns(int numberOfColumns) {
    if (numberOfColumns < 2) {
      throw new IllegalArgumentException("Number of columns must be at least 2");
    }
    ColumnBlock.Builder builder = ColumnBlock.builder();
    for (int i = 0; i < numberOfColumns; i++) {
      builder.emptyColumn();
    }
    ColumnListBlock columnList = ColumnListBlock.builder().children(builder.buildList()).build();
    blocks.add(columnList);
    return this;
  }

  public BlocksBuilder columns(Consumer<ColumnBlock.Builder> consumer) {
    ColumnBlock.Builder builder = ColumnBlock.builder();
    consumer.accept(builder);
    ColumnListBlock block = ColumnListBlock.builder().children(builder.buildList()).build();
    blocks.add(block);
    return this;
  }

  public BlocksBuilder tableOfContents(Color color) {
    blocks.add(TableOfContentsBlock.of(color));
    return this;
  }

  public BlocksBuilder tableOfContents(String color) {
    blocks.add(TableOfContentsBlock.of(Color.fromValue(color)));
    return this;
  }
}
