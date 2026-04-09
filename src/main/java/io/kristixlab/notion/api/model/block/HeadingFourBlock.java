package io.kristixlab.notion.api.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion heading 4 block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, toggleable state, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class HeadingFourBlock extends Block {

  @JsonProperty("heading_4")
  private Heading heading4;

  public HeadingFourBlock() {
    setType("heading_4");
    heading4 = new Heading();
  }

  /**
   * Creates a heading 4 block with plain text content.
   *
   * @param text the heading text
   * @return a new HeadingFourBlock
   */
  public static HeadingFourBlock of(String text) {
    HeadingFourBlock block = new HeadingFourBlock();
    block.getHeading4().setRichText(RichText.of(text));
    return block;
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
