package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion toggle block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, nested children, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class ToggleBlock extends Block {

  private Toggle toggle;

  public ToggleBlock() {
    setType("toggle");
    toggle = new Toggle();
  }

  /**
   * Creates a toggle block with plain text content.
   *
   * @param text the toggle label text
   * @return a new ToggleBlock
   */
  public static ToggleBlock of(String text) {
    ToggleBlock block = new ToggleBlock();
    block.getToggle().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link ToggleBlock} with rich text formatting,
   * block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** The inner content object of a toggle block. */
  @Getter
  @Setter
  public static class Toggle extends BlockWithChildren {}

  /** Builder for {@link ToggleBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, ToggleBlock> {

    private Builder() {}

    @Override
    public ToggleBlock build() {
      ToggleBlock block = new ToggleBlock();
      buildContent(block.getToggle());
      return block;
    }
  }
}
