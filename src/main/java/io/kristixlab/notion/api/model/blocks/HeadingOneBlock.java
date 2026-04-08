package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty("heading_1")
  private Heading heading1;

  public HeadingOneBlock() {
    setType("heading_1");
    heading1 = new Heading();
  }

  /**
   * Creates a heading 1 block with plain text content.
   *
   * @param text the heading text
   * @return a new HeadingOneBlock
   */
  public static HeadingOneBlock of(String text) {
    HeadingOneBlock block = new HeadingOneBlock();
    block.getHeading1().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link HeadingOneBlock} with rich text formatting,
   * toggleable state, and/or block-level color.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link HeadingOneBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, HeadingOneBlock> {

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
