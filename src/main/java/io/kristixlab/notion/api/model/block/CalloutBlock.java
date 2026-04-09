package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion callout block.
 *
 * <p>Simple construction via {@link #of(String, String)}. For rich text formatting, custom icons,
 * nested children, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class CalloutBlock extends Block {

  private Callout callout;

  public CalloutBlock() {
    setType("callout");
    callout = new Callout();
  }

  /**
   * Creates a callout block with an emoji icon and text.
   *
   * @param emoji the emoji character for the callout icon
   * @param text the callout text content
   * @return a new CalloutBlock
   */
  public static CalloutBlock of(String emoji, String text) {
    CalloutBlock block = new CalloutBlock();
    block.getCallout().setRichText(RichText.of(text));
    Icon icon = new Icon();
    icon.setType("emoji");
    icon.setEmoji(emoji);
    block.getCallout().setIcon(icon);
    return block;
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
    public Builder emoji(String emoji) {
      Icon emojiIcon = new Icon();
      emojiIcon.setType("emoji");
      emojiIcon.setEmoji(emoji);
      this.icon = emojiIcon;
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

  /** The inner content object of a callout block. */
  @Getter
  @Setter
  public static class Callout extends BlockWithChildren {

    private Icon icon;
  }
}
