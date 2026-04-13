package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.helper.NotionBlocks;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion numbered list item block.
 *
 * <p>Simple construction via {@link NotionBlocks#numbered(String) Blocks.numbered(...)}. For rich
 * text formatting, nested children, or block color use {@link #builder()}.
 */
@Getter
@Setter
public class NumberedListItemBlock extends Block {

  private NumberedListItem numberedListItem;

  public NumberedListItemBlock() {
    setType(BlockType.NUMBERED_LIST_ITEM.getValue());
    numberedListItem = new NumberedListItem();
  }

  /**
   * Returns a new builder for constructing a {@link NumberedListItemBlock} with rich text
   * formatting, block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link NumberedListItemBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, NumberedListItemBlock> {

    private Builder() {}

    @Override
    public NumberedListItemBlock build() {
      NumberedListItemBlock block = new NumberedListItemBlock();
      buildContent(block.getNumberedListItem());
      return block;
    }
  }

  /** The inner content object of a numbered list item block. */
  @Getter
  @Setter
  public static final class NumberedListItem extends BlockWithChildren {}
}
