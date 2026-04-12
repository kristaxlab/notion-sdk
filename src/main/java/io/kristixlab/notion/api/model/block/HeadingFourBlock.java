package io.kristixlab.notion.api.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.helper.NotionBlocks;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion heading 4 block.
 *
 * <p>Simple construction via {@link NotionBlocks#heading4(String) Blocks.heading4(...)}. For rich
 * text formatting, toggleable state, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class HeadingFourBlock extends Block {

  @JsonProperty("heading_4")
  private Heading heading4;

  public HeadingFourBlock() {
    setType(BlockType.HEADING_4.getValue());
    heading4 = new Heading();
  }

  /**
   * Returns a new builder for constructing a {@link HeadingFourBlock} with rich text formatting,
   * toggleable state, and/or block-level color.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link HeadingFourBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, HeadingFourBlock> {

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
    public HeadingFourBlock build() {
      HeadingFourBlock block = new HeadingFourBlock();
      buildContent(block.getHeading4());
      if (isToggleable != null) {
        block.getHeading4().setIsToggleable(isToggleable);
      }
      return block;
    }
  }
}
