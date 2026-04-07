package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion heading 1 block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, toggleable state, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class HeadingOneBlock extends Block {

  private Heading heading1;

  public HeadingOneBlock() {
    setType("heading_1");
    heading1 = new Heading();
  }

  public static HeadingOneBlock of(String text) {
    HeadingOneBlock block = new HeadingOneBlock();
    block.getHeading1().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link HeadingOneBlock} with rich text formatting,
   * toggleable state, and/or block-level color.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends BlockWithChildren.Builder<Builder, HeadingOneBlock> {

    private Boolean isToggleable;

    private Builder() {}

    /** Sets whether the heading can be toggled to reveal/hide children. */
    public Builder toggleable(boolean toggleable) {
      this.isToggleable = toggleable;
      return self();
    }

    @Override
    public HeadingOneBlock build() {
      HeadingOneBlock block = new HeadingOneBlock();
      buildContent(block.getHeading1());
      if (isToggleable != null) {
        block.getHeading1().setIsToggleable(isToggleable);
      }
      return block;
    }
  }
}
