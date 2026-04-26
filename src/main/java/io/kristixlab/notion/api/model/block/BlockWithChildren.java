package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.helper.NotionBlocks;
import io.kristixlab.notion.api.model.helper.NotionBlocksBuilder;
import io.kristixlab.notion.api.model.helper.NotionText;
import io.kristixlab.notion.api.model.helper.NotionTextBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * Common content model for block types that contain rich text, a color, and optional child blocks.
 *
 * <p>Concrete block types (e.g., {@link ParagraphBlock.Paragraph}, {@link QuoteBlock.Quote}) extend
 * this class to inherit shared fields. The companion {@link Builder} provides fluent construction
 * with rich text formatting, color, child nesting, and text annotation shortcuts.
 */
@Getter
@Setter
public sealed class BlockWithChildren
    permits ParagraphBlock.Paragraph,
        ToggleBlock.Toggle,
        BulletedListItemBlock.BulletedListItem,
        ToDoBlock.ToDo,
        NumberedListItemBlock.NumberedListItem,
        CalloutBlock.Callout,
        QuoteBlock.Quote,
        Heading {

  private List<RichText> richText;

  private String color;

  private List<Block> children;

  /**
   * Generic base builder for all blocks whose inner content extends {@link BlockWithChildren}.
   *
   * <p>Subclasses supply two type parameters:
   *
   * <ul>
   *   <li>{@code B} — the concrete builder type (enables fluent method chaining)
   *   <li>{@code R} — the outer block type returned by {@link #build()}
   * </ul>
   *
   * @param <B> self-referencing builder type
   * @param <R> the return type produced by {@link #build()}
   */
  public abstract static class Builder<B extends Builder<B, R>, R extends Block> {
    private List<RichText> richText = new ArrayList<>();
    private String color;
    private List<Block> children = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected B self() {
      return (B) this;
    }

    /**
     * Populates a {@link BlockWithChildren} instance with the accumulated rich text, color, and
     * children.
     *
     * @param content the content object to populate
     * @param <C> the concrete content type
     * @return the populated content object
     */
    protected <C extends BlockWithChildren> C buildContent(C content) {
      if (richText.isEmpty()) {
        richText.add(NotionText.plainText(""));
      }
      content.setRichText(new ArrayList(richText));
      if (color != null) {
        content.setColor(color);
      }
      if (!children.isEmpty()) {
        content.setChildren(new ArrayList<>(children));
      }
      return content;
    }

    /**
     * Builds the outer block. Each concrete builder implements this to wire the content into the
     * correct block type.
     */
    public abstract R build();

    /**
     * Sets the block-level color using a {@link Color} enum value.
     *
     * @param color the color to apply (must not be {@code null})
     * @return this builder
     * @throws IllegalArgumentException if {@code color} is {@code null}
     */
    public B blockColor(Color color) {
      if (color == null) {
        throw new IllegalArgumentException("Color cannot be null");
      }
      return blockColor(color.getValue());
    }

    /**
     * Sets the block-level color using a raw color string.
     *
     * @param color the color value (e.g., {@code "red"}, {@code "blue_background"})
     * @return this builder
     */
    public B blockColor(String color) {
      this.color = color;
      return self();
    }

    /**
     * Sets the rich text content from a plain text string.
     *
     * @param text the plain text content
     * @return this builder
     */
    public B text(String text) {
      this.richText.add(NotionText.plainText(text));
      return self();
    }

    /**
     * Sets the rich text content from a pre-built list.
     *
     * @param richText the rich text elements
     * @return this builder
     */
    public B text(RichText... richText) {
      this.richText.addAll(Arrays.asList(richText));
      return self();
    }

    /**
     * Sets the rich text content from a pre-built list.
     *
     * @param richText the rich text elements
     * @return this builder
     */
    public B text(List<RichText> richText) {
      this.richText.addAll(richText);
      return self();
    }

    public B text(Consumer<NotionTextBuilder> richTextsBuilder) {
      if (richTextsBuilder == null) {
        return self();
      }
      NotionTextBuilder builder = NotionText.textBuilder();
      richTextsBuilder.accept(builder);
      this.richText.addAll(builder.build());
      return self();
    }

    /**
     * Adds child blocks from a pre-built list.
     *
     * @param children the child blocks to add
     * @return this builder
     */
    public B children(Block... children) {
      this.children.addAll(List.of(children));
      return self();
    }

    /**
     * Adds child blocks from a pre-built list.
     *
     * @param children the child blocks to add
     * @return this builder
     */
    public B children(List<Block> children) {
      this.children.addAll(children);
      return self();
    }

    /**
     * Adds child blocks using a {@link NotionBlocksBuilder} consumer for fluent nesting.
     *
     * @param childrenBuilder a consumer that populates the children builder
     * @return this builder
     */
    public B children(Consumer<NotionBlocksBuilder> childrenBuilder) {
      if (childrenBuilder == null) {
        return self();
      }
      NotionBlocksBuilder builder = NotionBlocks.blocksBuilder();
      childrenBuilder.accept(builder);
      children(builder.build());
      return self();
    }
  }
}
