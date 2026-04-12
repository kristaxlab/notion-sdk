package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.Icon;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion paragraph block.
 *
 * <p>Simple construction via {@link #of(String)} or {@link #of(String, Color)}. For rich text
 * formatting, nested children, or conditional fields use {@link #builder()}.
 */
@Getter
@Setter
public class ParagraphBlock extends Block {

  private Paragraph paragraph;

  public ParagraphBlock() {
    setType(BlockType.PARAGRAPH.getValue());
    paragraph = new Paragraph();
  }

  /**
   * Returns a new builder for constructing a {@link ParagraphBlock} with rich text formatting,
   * block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link ParagraphBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, ParagraphBlock> {

    private Icon icon;

    private Builder() {}

    @Override
    public ParagraphBlock build() {
      ParagraphBlock block = new ParagraphBlock();
      buildContent(block.getParagraph());
      block.getParagraph().setIcon(icon);
      return block;
    }

    /**
     * Sets the paragraph icon.
     *
     * @param icon the icon to display alongside the paragraph
     * @return this builder
     */
    public Builder icon(Icon icon) {
      this.icon = icon;
      return self();
    }
  }

  /** The inner content object of a paragraph block. */
  @Getter
  @Setter
  public static final class Paragraph extends BlockWithChildren {

    private Icon icon;
  }
}
