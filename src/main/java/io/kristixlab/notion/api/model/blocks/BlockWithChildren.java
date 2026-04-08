package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.helper.BlocksBuilder;
import java.util.ArrayList;
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
public class BlockWithChildren {

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
    private List<RichText> richText;
    private String blockColor;
    private List<Block> childBlocks;

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
      if (richText == null || richText.isEmpty()) {
        richText = RichText.of("");
      }
      content.setRichText(richText);
      if (blockColor != null) {
        content.setColor(blockColor);
      }
      if (childBlocks != null) {
        content.setChildren(childBlocks);
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
    public B color(Color color) {
      if (color == null) {
        throw new IllegalArgumentException("Color cannot be null");
      }
      return color(color.getValue());
    }

    /**
     * Sets the block-level color using a raw color string.
     *
     * @param color the color value (e.g., {@code "red"}, {@code "blue_background"})
     * @return this builder
     */
    public B color(String color) {
      this.blockColor = color;
      return self();
    }

    /**
     * Sets the rich text content from a plain text string.
     *
     * @param text the plain text content
     * @return this builder
     */
    public B text(String text) {
      this.richText = RichText.of(text);
      return self();
    }

    /**
     * Sets the rich text content from a pre-built list.
     *
     * @param richText the rich text elements
     * @return this builder
     */
    public B richText(List<RichText> richText) {
      this.richText = richText;
      return self();
    }

    /**
     * Sets the rich text content using a {@link RichText.Builder} consumer for inline formatting.
     *
     * @param richTextsBuilder a consumer that configures the rich text builder
     * @return this builder
     */
    public B richText(Consumer<RichText.Builder> richTextsBuilder) {
      if (richTextsBuilder == null) {
        return self();
      }
      RichText.Builder builder = RichText.builder();
      richTextsBuilder.accept(builder);
      this.richText = builder.buildList();
      return self();
    }

    /**
     * Adds child blocks from a pre-built list.
     *
     * @param children the child blocks to add
     * @return this builder
     */
    public B children(List<Block> children) {
      getChildren().addAll(children);
      return self();
    }

    /**
     * Adds child blocks using a {@link BlocksBuilder} consumer for fluent nesting.
     *
     * @param childrenBuilder a consumer that populates the children builder
     * @return this builder
     */
    public B children(Consumer<BlocksBuilder> childrenBuilder) {
      if (childrenBuilder == null) {
        return self();
      }
      BlocksBuilder builder = BlocksBuilder.of();
      childrenBuilder.accept(builder);
      getChildren().addAll(builder.build());
      return self();
    }

    private List<Block> getChildren() {
      if (childBlocks == null) {
        childBlocks = new ArrayList<>();
      }
      return childBlocks;
    }

    /**
     * Applies bold annotation to all current rich text elements. Equivalent to {@code bold(true)}.
     *
     * @return this builder
     */
    public B bold() {
      return bold(true);
    }

    /**
     * Sets the bold annotation on all current rich text elements.
     *
     * @param bold {@code true} to apply bold, {@code false} to remove it
     * @return this builder
     */
    public B bold(boolean bold) {
      for (RichText rt : richText) {
        getAnnotations(rt).setBold(bold);
      }
      return self();
    }

    /**
     * Applies italic annotation to all current rich text elements. Equivalent to {@code
     * italic(true)}.
     *
     * @return this builder
     */
    public B italic() {
      return italic(true);
    }

    /**
     * Sets the italic annotation on all current rich text elements.
     *
     * @param italic {@code true} to apply italic, {@code false} to remove it
     * @return this builder
     */
    public B italic(boolean italic) {
      for (RichText rt : richText) {
        getAnnotations(rt).setItalic(italic);
      }
      return self();
    }

    /**
     * Applies strikethrough annotation to all current rich text elements. Equivalent to {@code
     * strikethrough(true)}.
     *
     * @return this builder
     */
    public B strikethrough() {
      return strikethrough(true);
    }

    /**
     * Sets the strikethrough annotation on all current rich text elements.
     *
     * @param strikethrough {@code true} to apply strikethrough, {@code false} to remove it
     * @return this builder
     */
    public B strikethrough(boolean strikethrough) {
      for (RichText rt : richText) {
        getAnnotations(rt).setStrikethrough(strikethrough);
      }
      return self();
    }

    /**
     * Applies underline annotation to all current rich text elements. Equivalent to {@code
     * underline(true)}.
     *
     * @return this builder
     */
    public B underline() {
      return underline(true);
    }

    /**
     * Sets the underline annotation on all current rich text elements.
     *
     * @param underline {@code true} to apply underline, {@code false} to remove it
     * @return this builder
     */
    public B underline(boolean underline) {
      for (RichText rt : richText) {
        getAnnotations(rt).setUnderline(underline);
      }
      return self();
    }

    /**
     * Applies inline code annotation to all current rich text elements. Equivalent to {@code
     * code(true)}.
     *
     * @return this builder
     */
    public B code() {
      return code(true);
    }

    /**
     * Sets the inline code annotation on all current rich text elements.
     *
     * @param code {@code true} to apply inline code, {@code false} to remove it
     * @return this builder
     */
    public B code(boolean code) {
      for (RichText rt : richText) {
        getAnnotations(rt).setCode(code);
      }
      return self();
    }

    /**
     * Gets the annotations object for a rich text element, initializing it if necessary.
     *
     * @param rt rich text element to retrieve annotations for
     * @return annotation
     */
    private static RichText.Annotations getAnnotations(RichText rt) {
      if (rt.getAnnotations() == null) {
        rt.setAnnotations(new RichText.Annotations());
      }
      return rt.getAnnotations();
    }
  }
}
