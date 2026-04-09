package io.kristixlab.notion.api.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion heading 3 block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, toggleable state, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class HeadingThreeBlock extends Block {

  @JsonProperty("heading_3")
  private Heading heading3;

  public HeadingThreeBlock() {
    setType("heading_3");
    heading3 = new Heading();
  }

  /**
   * Creates a heading 3 block with plain text content.
   *
   * @param text the heading text
   * @return a new HeadingThreeBlock
   */
  public static HeadingThreeBlock of(String text) {
    HeadingThreeBlock block = new HeadingThreeBlock();
    block.getHeading3().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link HeadingThreeBlock} with rich text formatting,
   * toggleable state, and/or block-level color.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link HeadingThreeBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, HeadingThreeBlock> {

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
    public HeadingThreeBlock build() {
      HeadingThreeBlock block = new HeadingThreeBlock();
      buildContent(block.getHeading3());
      if (isToggleable != null) {
        block.getHeading3().setIsToggleable(isToggleable);
      }
      return block;
    }
  }
}
