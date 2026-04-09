package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion numbered list item block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, nested children, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class NumberedListItemBlock extends Block {

  private NumberedListItem numberedListItem;

  public NumberedListItemBlock() {
    setType("numbered_list_item");
    numberedListItem = new NumberedListItem();
  }

  /**
   * Creates a numbered list item block with plain text content.
   *
   * @param text the list item text
   * @return a new NumberedListItemBlock
   */
  public static NumberedListItemBlock of(String text) {
    NumberedListItemBlock block = new NumberedListItemBlock();
    block.getNumberedListItem().setRichText(RichText.of(text));
    return block;
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
  public static class NumberedListItem extends BlockWithChildren {}
}
