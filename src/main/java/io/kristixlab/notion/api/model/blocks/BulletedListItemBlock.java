package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion bulleted list item block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, nested children, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class BulletedListItemBlock extends Block {

  private BulletedListItem bulletedListItem;

  public BulletedListItemBlock() {
    setType("bulleted_list_item");
    bulletedListItem = new BulletedListItem();
  }

  /**
   * Creates a bulleted list item block with plain text content.
   *
   * @param text the list item text
   * @return a new BulletedListItemBlock
   */
  public static BulletedListItemBlock of(String text) {
    BulletedListItemBlock block = new BulletedListItemBlock();
    block.getBulletedListItem().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link BulletedListItemBlock} with rich text
   * formatting, block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link BulletedListItemBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, BulletedListItemBlock> {

    private Builder() {}

    @Override
    public BulletedListItemBlock build() {
      BulletedListItemBlock block = new BulletedListItemBlock();
      buildContent(block.getBulletedListItem());
      return block;
    }
  }

  /** The inner content object of a bulleted list item block. */
  @Getter
  @Setter
  public static class BulletedListItem extends BlockWithChildren {}
}
