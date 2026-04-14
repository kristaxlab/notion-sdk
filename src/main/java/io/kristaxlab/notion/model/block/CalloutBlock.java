package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.common.Icon;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion callout block.
 *
 * <p>Simple construction via {@link NotionBlocks#callout(String)} and related overloads. For rich
 * text formatting, custom icons, nested children, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class CalloutBlock extends Block {

  private Callout callout;

  public CalloutBlock() {
    setType(BlockType.CALLOUT.getValue());
    callout = new Callout();
  }

  /** The inner content object of a callout block. */
  @Getter
  @Setter
  public static final class Callout extends BlockWithChildren {

    private Icon icon;
  }

  /**
   * Returns a new builder for constructing a {@link CalloutBlock} with rich text formatting, custom
   * icon, block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link CalloutBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, CalloutBlock> {

    private Icon icon;

    private Builder() {}

    /**
     * Sets the callout icon to an emoji character.
     *
     * @param emoji the emoji string (e.g., {@code "💡"})
     * @return this builder
     */
    public Builder emoji(Icon emoji) {
      if (emoji == null) {
        this.icon = null;
        return self();
      }
      this.icon = Icon.emoji(emoji.getEmoji());
      return self();
    }

    /**
     * Sets the callout icon to an emoji character.
     *
     * @param emoji the emoji string (e.g., {@code "💡"})
     * @return this builder
     */
    public Builder emoji(String emoji) {
      if (emoji == null) {
        this.icon = null;
        return self();
      }
      this.icon = Icon.emoji(emoji);
      return self();
    }

    /**
     * Sets the callout icon directly.
     *
     * @param icon the icon to display
     * @return this builder
     */
    public Builder icon(Icon icon) {
      this.icon = icon;
      return self();
    }

    @Override
    public CalloutBlock build() {
      CalloutBlock block = new CalloutBlock();
      buildContent(block.getCallout());
      if (icon != null) {
        block.getCallout().setIcon(icon);
      }
      return block;
    }
  }
}
