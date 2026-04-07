package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.helper.BlocksBuilder;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

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

    /** Sets the block-level color (background or foreground for the entire block). */
    public B color(String color) {
      this.blockColor = color;
      return self();
    }

    /** Sets the rich texts directly. */
    public B text(String text) {
      this.richText = RichText.of(text);
      return self();
    }

    /** Sets the rich texts directly. */
    public B richText(List<RichText> richText) {
      this.richText = richText;
      return self();
    }

    /** Sets the child blocks using a {@link BlocksBuilder} lambda for fluent nesting. */
    public B richText(Consumer<RichText.Builder> richTextsBuilder) {
      if (richTextsBuilder == null) {
        return self();
      }
      RichText.Builder builder = RichText.builder();
      richTextsBuilder.accept(builder);
      this.richText = builder.buildList();
      return self();
    }

    /** Sets the child blocks directly. */
    public B children(List<Block> children) {
      this.childBlocks = children;
      return self();
    }

    /** Sets the child blocks using a {@link BlocksBuilder} lambda for fluent nesting. */
    public B children(Consumer<BlocksBuilder> childrenBuilder) {
      if (childrenBuilder == null) {
        return self();
      }
      BlocksBuilder builder = BlocksBuilder.of();
      childrenBuilder.accept(builder);
      this.childBlocks = builder.build();
      return self();
    }

    public B bold() {
      return bold(true);
    }

    public B bold(boolean bold) {
      for (RichText rt : richText) {
        if (rt.getAnnotations() == null) {
          rt.setAnnotations(new RichText.Annotations());
        }
        rt.getAnnotations().setBold(bold);
      }
      return self();
    }

    public B italic() {
      return italic(true);
    }

    public B italic(boolean italic) {
      for (RichText rt : richText) {
        if (rt.getAnnotations() == null) {
          rt.setAnnotations(new RichText.Annotations());
        }
        rt.getAnnotations().setItalic(italic);
      }
      return self();
    }

    public B strikethrough() {
      return strikethrough(true);
    }

    public B strikethrough(boolean strikethrough) {
      for (RichText rt : richText) {
        if (rt.getAnnotations() == null) {
          rt.setAnnotations(new RichText.Annotations());
        }
        rt.getAnnotations().setStrikethrough(strikethrough);
      }
      return self();
    }

    public B underline() {
      return underline(true);
    }

    public B underline(boolean underline) {
      for (RichText rt : richText) {
        if (rt.getAnnotations() == null) {
          rt.setAnnotations(new RichText.Annotations());
        }
        rt.getAnnotations().setUnderline(underline);
      }
      return self();
    }

    public B code() {
      return code(true);
    }

    public B code(boolean code) {
      for (RichText rt : richText) {
        if (rt.getAnnotations() == null) {
          rt.setAnnotations(new RichText.Annotations());
        }
        rt.getAnnotations().setCode(code);
      }
      return self();
    }
  }
}
