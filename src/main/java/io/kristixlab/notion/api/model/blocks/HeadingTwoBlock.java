package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion heading 2 block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, toggleable state, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class HeadingTwoBlock extends Block {

  @JsonProperty("heading_2")
  private Heading heading2;

  public HeadingTwoBlock() {
    setType("heading_2");
    heading2 = new Heading();
  }

  public static HeadingTwoBlock of(String text) {
    HeadingTwoBlock block = new HeadingTwoBlock();
    block.getHeading2().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link HeadingTwoBlock} with rich text formatting,
   * toggleable state, and/or block-level color.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends BlockWithChildren.Builder<Builder, HeadingTwoBlock> {

    private Boolean isToggleable;

    private Builder() {}

    /** Sets whether the heading can be toggled to reveal/hide children. */
    public Builder toggleable(boolean toggleable) {
      this.isToggleable = toggleable;
      return self();
    }

    @Override
    public HeadingTwoBlock build() {
      HeadingTwoBlock block = new HeadingTwoBlock();
      buildContent(block.getHeading2());
      if (isToggleable != null) {
        block.getHeading2().setIsToggleable(isToggleable);
      }
      return block;
    }
  }
}
