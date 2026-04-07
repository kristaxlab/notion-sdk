package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.blocks.*;
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
    return List.copyOf(blocks);
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

  public BlocksBuilder bookmark(String url) {
    blocks.add(BookmarkBlock.of(url));
    return this;
  }

  public BlocksBuilder breadcrumb() {
    blocks.add(new BreadcrumbBlock());
    return this;
  }

  public BlocksBuilder tableOfContents() {
    blocks.add(new TableOfContentsBlock());
    return this;
  }
}
