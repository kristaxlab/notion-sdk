package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.helper.NotionBlocks;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion toggle block.
 *
 * <p>Simple construction via {@link NotionBlocks#toggle(String) Blocks.toggle(...)}. For rich text
 * formatting, nested children, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class ToggleBlock extends Block {

  private Toggle toggle;

  public ToggleBlock() {
    setType(BlockType.TOGGLE.getValue());
    toggle = new Toggle();
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
  public static final class Toggle extends BlockWithChildren {}

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
