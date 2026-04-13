package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristaxlab.notion.model.helper.NotionBlocks;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion heading 2 block.
 *
 * <p>Simple construction via {@link NotionBlocks#heading2(String) Blocks.heading2(...)}. For rich
 * text formatting, toggleable state, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class HeadingTwoBlock extends Block {

  @JsonProperty("heading_2")
  private Heading heading2;

  public HeadingTwoBlock() {
    setType(BlockType.HEADING_2.getValue());
    heading2 = new Heading();
  }

  /**
   * Returns a new builder for constructing a {@link HeadingTwoBlock} with rich text formatting,
   * toggleable state, and/or block-level color.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link HeadingTwoBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, HeadingTwoBlock> {

    private Boolean isToggleable;

    private Builder() {}

    /**
     * Sets whether the heading can be toggled to reveal or hide children.
     *
     * @param toggleable {@code true} to make the heading toggleable
     * @return this builder
     */
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
