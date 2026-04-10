package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fluent builder for composing a list of Notion blocks with support for nesting.
 *
 * <p>Provides shorthand methods for common block types and {@link Consumer}-based lambdas for
 * expressing parent–child relationships inline.
 */
public class NotionBlocksBuilder {

  private final List<Block> blocks = new ArrayList<>();

  protected NotionBlocksBuilder() {}

  /**
   * Builds and returns the list of blocks.
   *
   * @return an unmodifiable list of blocks
   */
  public List<Block> build() {
    return new ArrayList<>(blocks);
  }

  /**
   * Creates a new builder.
   *
   * @return a new empty builder
   */
  static NotionBlocksBuilder of() {
    return new NotionBlocksBuilder();
  }

  /**
   * Adds a pre-built block. Use this escape hatch when shorthand methods don't cover your use case.
   *
   * @param block the block to add
   * @return this builder
   */
  public NotionBlocksBuilder block(Block block) {
    blocks.add(block);
    return this;
  }

  /**
   * Adds multiple pre-built blocks (varargs).
   *
   * @param blocks the blocks to add
   * @return this builder
   */
  public NotionBlocksBuilder blocks(Block... blocks) {
    Collections.addAll(this.blocks, blocks);
    return this;
  }

  /**
   * Adds multiple pre-built blocks.
   *
   * @param blocks the blocks to add
   * @return this builder
   */
  public NotionBlocksBuilder blocks(List<? extends Block> blocks) {
    this.blocks.addAll(blocks);
    return this;
  }

  /**
   * Adds multiple pre-built blocks.
   *
   * @param blocks the blocks to add
   * @return this builder
   */
  public NotionBlocksBuilder blockLists(List<List<? extends Block>> blocks) {
    for (List<? extends Block> blockList : blocks) {
      this.blocks.addAll(blockList);
    }
    return this;
  }

  public NotionBlocksBuilder blankLine() {
    blocks.add(NotionBlocks.blankLine());
    return this;
  }

  // Audio

  public NotionBlocksBuilder audio(String audioRef) {
    blocks.add(NotionBlocks.audio(audioRef));
    return this;
  }

  public NotionBlocksBuilder audio(FileData audioData) {
    blocks.add(NotionBlocks.audio(audioData));
    return this;
  }

  public NotionBlocksBuilder audio(Consumer<FileData.Builder> consumer) {
    blocks.add(NotionBlocks.audio(consumer));
    return this;
  }

  // Breadcrumb

  public NotionBlocksBuilder breadcrumb() {
    blocks.add(new BreadcrumbBlock());
    return this;
  }

  // Bookmark

  public NotionBlocksBuilder bookmark(String url) {
    blocks.add(NotionBlocks.bookmark(url));
    return this;
  }

  public NotionBlocksBuilder bookmark(Consumer<BookmarkBlock.Builder> consumer) {
    blocks.add(NotionBlocks.bookmark(consumer));
    return this;
  }

  // Bulleted List

  public NotionBlocksBuilder bullet(String text) {
    blocks.add(NotionBlocks.bullet(text));
    return this;
  }

  public NotionBlocksBuilder bullet(RichText... texts) {
    blocks.add(NotionBlocks.bullet(texts));
    return this;
  }

  public NotionBlocksBuilder bullet(List<RichText> texts) {
    blocks.add(NotionBlocks.bullet(texts));
    return this;
  }

  public NotionBlocksBuilder bullet(Consumer<BulletedListItemBlock.Builder> consumer) {
    blocks.add(NotionBlocks.bullet(consumer));
    return this;
  }

  public NotionBlocksBuilder bulletedList(String... bulletItems) {
    blocks.addAll(NotionBlocks.bulletedList(bulletItems));
    return this;
  }

  public NotionBlocksBuilder bulletedList(List<String> bulletItems) {
    blocks.addAll(NotionBlocks.bulletedList(bulletItems));
    return this;
  }

  // Callout

  public NotionBlocksBuilder callout(String text) {
    blocks.add(NotionBlocks.callout(text));
    return this;
  }

  public NotionBlocksBuilder callout(RichText... texts) {
    blocks.add(NotionBlocks.callout(texts));
    return this;
  }

  public NotionBlocksBuilder callout(List<RichText> texts) {
    blocks.add(NotionBlocks.callout(texts));
    return this;
  }

  public NotionBlocksBuilder callout(Consumer<CalloutBlock.Builder> consumer) {
    blocks.add(NotionBlocks.callout(consumer));
    return this;
  }

  public NotionBlocksBuilder callout(String emoji, String text) {
    blocks.add(NotionBlocks.callout(Icon.emoji(emoji), text));
    return this;
  }

  public NotionBlocksBuilder callout(Icon emoji, String text) {
    blocks.add(NotionBlocks.callout(emoji, text));
    return this;
  }

  public NotionBlocksBuilder callout(Icon emoji, RichText... texts) {
    blocks.add(NotionBlocks.callout(emoji, texts));
    return this;
  }

  public NotionBlocksBuilder callout(Icon emoji, List<RichText> texts) {
    blocks.add(NotionBlocks.callout(emoji, texts));
    return this;
  }

  // Code

  public NotionBlocksBuilder code(String language, String code) {
    blocks.add(NotionBlocks.code(language, code));
    return this;
  }

  public NotionBlocksBuilder code(Consumer<CodeBlock.Builder> consumer) {
    blocks.add(NotionBlocks.code(consumer));
    return this;
  }

  // Columns (layout)

  public NotionBlocksBuilder columns(ColumnBlock... cols) {
    blocks.add(NotionBlocks.columns(cols));
    return this;
  }

  public NotionBlocksBuilder columns(List<ColumnBlock> cols) {
    blocks.add(NotionBlocks.columns(cols));
    return this;
  }

  public NotionBlocksBuilder columns(Consumer<NotionBlocksBuilder>... consumers) {
    blocks.add(NotionBlocks.columns(consumers));
    return this;
  }

  public NotionBlocksBuilder columns(List<Block>... columnContents) {
    blocks.add(NotionBlocks.columns(columnContents));
    return this;
  }

  // Divider

  public NotionBlocksBuilder divider() {
    blocks.add(new DividerBlock());
    return this;
  }

  // Equation

  public NotionBlocksBuilder equation(String expression) {
    blocks.add(NotionBlocks.equation(expression));
    return this;
  }

  // Headings

  public NotionBlocksBuilder heading1(String text) {
    blocks.add(NotionBlocks.heading1(text));
    return this;
  }

  public NotionBlocksBuilder heading1(RichText... texts) {
    blocks.add(NotionBlocks.heading1(texts));
    return this;
  }

  public NotionBlocksBuilder heading1(List<RichText> texts) {
    blocks.add(NotionBlocks.heading1(texts));
    return this;
  }

  public NotionBlocksBuilder heading1(Consumer<HeadingOneBlock.Builder> consumer) {
    blocks.add(NotionBlocks.heading1(consumer));
    return this;
  }

  public NotionBlocksBuilder heading2(String text) {
    blocks.add(NotionBlocks.heading2(text));
    return this;
  }

  public NotionBlocksBuilder heading2(RichText... texts) {
    blocks.add(NotionBlocks.heading2(texts));
    return this;
  }

  public NotionBlocksBuilder heading2(List<RichText> texts) {
    blocks.add(NotionBlocks.heading2(texts));
    return this;
  }

  public NotionBlocksBuilder heading2(Consumer<HeadingTwoBlock.Builder> consumer) {
    blocks.add(NotionBlocks.heading2(consumer));
    return this;
  }

  public NotionBlocksBuilder heading3(String text) {
    blocks.add(NotionBlocks.heading3(text));
    return this;
  }

  public NotionBlocksBuilder heading3(RichText... texts) {
    blocks.add(NotionBlocks.heading3(texts));
    return this;
  }

  public NotionBlocksBuilder heading3(List<RichText> texts) {
    blocks.add(NotionBlocks.heading3(texts));
    return this;
  }

  public NotionBlocksBuilder heading3(Consumer<HeadingThreeBlock.Builder> consumer) {
    blocks.add(NotionBlocks.heading3(consumer));
    return this;
  }

  public NotionBlocksBuilder heading4(String text) {
    blocks.add(NotionBlocks.heading4(text));
    return this;
  }

  public NotionBlocksBuilder heading4(RichText... texts) {
    blocks.add(NotionBlocks.heading4(texts));
    return this;
  }

  public NotionBlocksBuilder heading4(List<RichText> texts) {
    blocks.add(NotionBlocks.heading4(texts));
    return this;
  }

  public NotionBlocksBuilder heading4(Consumer<HeadingFourBlock.Builder> consumer) {
    blocks.add(NotionBlocks.heading4(consumer));
    return this;
  }

  // link to page

  public NotionBlocksBuilder linkToPage(String pageId) {
    blocks.add(NotionBlocks.linkToPage(pageId));
    return this;
  }

  public NotionBlocksBuilder linkToDatabase(String databaseId) {
    blocks.add(NotionBlocks.linkToDatabase(databaseId));
    return this;
  }

  public NotionBlocksBuilder linkToComment(String commentId) {
    blocks.add(NotionBlocks.linkToComment(commentId));
    return this;
  }

  // Paragraph

  public NotionBlocksBuilder paragraph(String text) {
    blocks.add(NotionBlocks.paragraph(text));
    return this;
  }

  public NotionBlocksBuilder paragraph(RichText... texts) {
    blocks.add(NotionBlocks.paragraph(texts));
    return this;
  }

  public NotionBlocksBuilder paragraph(List<RichText> texts) {
    blocks.add(NotionBlocks.paragraph(texts));
    return this;
  }

  public NotionBlocksBuilder paragraph(Consumer<ParagraphBlock.Builder> consumer) {
    blocks.add(NotionBlocks.paragraph(consumer));
    return this;
  }

  public NotionBlocksBuilder paragraphList(String... items) {
    blocks.addAll(NotionBlocks.paragraphList(items));
    return this;
  }

  public NotionBlocksBuilder paragraphList(List<String> items) {
    blocks.addAll(NotionBlocks.paragraphList(items));
    return this;
  }

  public NotionBlocksBuilder numbered(String text) {
    blocks.add(NotionBlocks.numbered(text));
    return this;
  }

  public NotionBlocksBuilder numbered(RichText... texts) {
    blocks.add(NotionBlocks.numbered(texts));
    return this;
  }

  public NotionBlocksBuilder numbered(List<RichText> texts) {
    blocks.add(NotionBlocks.numbered(texts));
    return this;
  }

  public NotionBlocksBuilder numbered(Consumer<NumberedListItemBlock.Builder> consumer) {
    blocks.add(NotionBlocks.numbered(consumer));
    return this;
  }

  public NotionBlocksBuilder numberedList(String... numberedItems) {
    blocks.addAll(NotionBlocks.bulletedList(numberedItems));
    return this;
  }

  public NotionBlocksBuilder numberedList(List<String> numberedItems) {
    blocks.addAll(NotionBlocks.numberedList(numberedItems));
    return this;
  }

  // Synced Block

  public NotionBlocksBuilder synced(Block... children) {
    blocks.add(NotionBlocks.synced(children));
    return this;
  }

  public NotionBlocksBuilder synced(Consumer<NotionBlocksBuilder> consumer) {
    blocks.add(NotionBlocks.synced(consumer));
    return this;
  }

  public NotionBlocksBuilder synced(List<Block> children) {
    blocks.add(NotionBlocks.synced(children));
    return this;
  }

  public NotionBlocksBuilder syncedFrom(String blockId) {
    blocks.add(NotionBlocks.syncedFrom(blockId));
    return this;
  }

  // To do list

  public NotionBlocksBuilder todo(String text) {
    blocks.add(NotionBlocks.todo(text));
    return this;
  }

  public NotionBlocksBuilder todo(RichText... texts) {
    blocks.add(NotionBlocks.todo(texts));
    return this;
  }

  public NotionBlocksBuilder todo(List<RichText> texts) {
    blocks.add(NotionBlocks.todo(texts));
    return this;
  }

  public NotionBlocksBuilder todo(Consumer<ToDoBlock.Builder> consumer) {
    blocks.add(NotionBlocks.todo(consumer));
    return this;
  }

  public NotionBlocksBuilder todoList(String... items) {
    blocks.addAll(NotionBlocks.todoList(items));
    return this;
  }

  public NotionBlocksBuilder todoList(List<String> items) {
    blocks.addAll(NotionBlocks.todoList(items));
    return this;
  }

  public NotionBlocksBuilder toggle(String text) {
    blocks.add(NotionBlocks.toggle(text));
    return this;
  }

  public NotionBlocksBuilder toggle(RichText... texts) {
    blocks.add(NotionBlocks.toggle(texts));
    return this;
  }

  public NotionBlocksBuilder toggle(List<RichText> texts) {
    blocks.add(NotionBlocks.toggle(texts));
    return this;
  }

  public NotionBlocksBuilder toggle(Consumer<ToggleBlock.Builder> consumer) {
    blocks.add(NotionBlocks.toggle(consumer));
    return this;
  }

  public NotionBlocksBuilder toggleList(String... items) {
    blocks.addAll(NotionBlocks.toggleList(items));
    return this;
  }

  public NotionBlocksBuilder toggleList(List<String> items) {
    blocks.addAll(NotionBlocks.toggleList(items));
    return this;
  }

  public NotionBlocksBuilder quote(String text) {
    blocks.add(NotionBlocks.quote(text));
    return this;
  }

  public NotionBlocksBuilder quote(RichText... texts) {
    blocks.add(NotionBlocks.quote(texts));
    return this;
  }

  public NotionBlocksBuilder quote(List<RichText> texts) {
    blocks.add(NotionBlocks.quote(texts));
    return this;
  }

  public NotionBlocksBuilder quote(Consumer<QuoteBlock.Builder> consumer) {
    blocks.add(NotionBlocks.quote(consumer));
    return this;
  }

  public NotionBlocksBuilder image(String urlOrFileUploadId) {
    blocks.add(NotionBlocks.image(urlOrFileUploadId));
    return this;
  }

  public NotionBlocksBuilder image(FileData imageData) {
    blocks.add(NotionBlocks.image(imageData));
    return this;
  }

  public NotionBlocksBuilder image(Consumer<FileData.Builder> consumer) {
    blocks.add(NotionBlocks.image(consumer));
    return this;
  }

  public NotionBlocksBuilder video(String videoRef) {
    blocks.add(NotionBlocks.video(videoRef));
    return this;
  }

  public NotionBlocksBuilder video(FileData videoData) {
    blocks.add(NotionBlocks.video(videoData));
    return this;
  }

  public NotionBlocksBuilder video(Consumer<FileData.Builder> consumer) {
    blocks.add(NotionBlocks.video(consumer));
    return this;
  }

  public NotionBlocksBuilder pdf(String pdfRef) {
    blocks.add(NotionBlocks.pdf(pdfRef));
    return this;
  }

  public NotionBlocksBuilder pdf(FileData pdfData) {
    blocks.add(NotionBlocks.pdf(pdfData));
    return this;
  }

  public NotionBlocksBuilder pdf(Consumer<FileData.Builder> consumer) {
    blocks.add(NotionBlocks.pdf(consumer));
    return this;
  }

  public NotionBlocksBuilder file(String fileRef) {
    blocks.add(NotionBlocks.file(fileRef));
    return this;
  }

  public NotionBlocksBuilder file(FileData fileData) {
    blocks.add(NotionBlocks.file(fileData));
    return this;
  }

  public NotionBlocksBuilder file(Consumer<FileData.Builder> consumer) {
    blocks.add(NotionBlocks.file(consumer));
    return this;
  }

  public NotionBlocksBuilder embed(String url) {
    blocks.add(NotionBlocks.embed(url));
    return this;
  }

  public NotionBlocksBuilder embed(Consumer<EmbedBlock.Builder> consumer) {
    blocks.add(NotionBlocks.embed(consumer));
    return this;
  }

  public NotionBlocksBuilder table(Consumer<TableBlock.Builder> consumer) {
    blocks.add(NotionBlocks.table(consumer));
    return this;
  }

  public NotionBlocksBuilder table(TableRowBlock... rows) {
    blocks.add(NotionBlocks.table(rows));
    return this;
  }

  public NotionBlocksBuilder table(List<TableRowBlock> rows) {
    blocks.add(NotionBlocks.table(rows));
    return this;
  }

  public NotionBlocksBuilder tableOfContents() {
    blocks.add(TableOfContentsBlock.of());
    return this;
  }

  public NotionBlocksBuilder tableOfContents(String color) {
    blocks.add(NotionBlocks.tableOfContents(color));
    return this;
  }

  public NotionBlocksBuilder tableOfContents(Color color) {
    blocks.add(NotionBlocks.tableOfContents(color));
    return this;
  }
}
