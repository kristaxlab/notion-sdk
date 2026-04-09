package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
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
    setType("paragraph");
    paragraph = new Paragraph();
  }

  /**
   * Creates a paragraph block with plain text content.
   *
   * @param text the paragraph text
   * @return a new ParagraphBlock
   */
  public static ParagraphBlock of(String text) {
    ParagraphBlock block = new ParagraphBlock();
    block.getParagraph().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Creates a paragraph block with colored text.
   *
   * @param text the paragraph text
   * @param color the text color
   * @return a new ParagraphBlock
   */
  public static ParagraphBlock of(String text, Color color) {
    ParagraphBlock block = new ParagraphBlock();
    block.getParagraph().setRichText(RichText.builder().text(text).color(color).buildList());
    return block;
  }

  /**
   * Creates a paragraph block from pre-built rich text elements.
   *
   * @param richTexts the rich text content
   * @return a new ParagraphBlock
   */
  public static ParagraphBlock of(List<RichText> richTexts) {
    return of(richTexts, null);
  }

  /**
   * Creates a paragraph block from pre-built rich text elements with an optional block-level color.
   *
   * @param richTexts the rich text content
   * @param color the block-level color, or {@code null} for default
   * @return a new ParagraphBlock
   */
  public static ParagraphBlock of(List<RichText> richTexts, Color color) {
    ParagraphBlock block = new ParagraphBlock();
    block.getParagraph().setRichText(richTexts);
    if (color != null) {
      block.getParagraph().setColor(color.getValue());
    }
    return block;
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
  public static class Paragraph extends BlockWithChildren {

    private Icon icon;
  }
}
