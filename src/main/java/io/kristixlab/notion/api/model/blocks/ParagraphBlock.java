package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.Color;
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

  public static ParagraphBlock of(String text) {
    ParagraphBlock block = new ParagraphBlock();
    block.getParagraph().setRichText(RichText.of(text));
    return block;
  }

  public static ParagraphBlock of(String text, Color color) {
    ParagraphBlock block = new ParagraphBlock();
    block.getParagraph().setRichText(RichText.builder().text(text).color(color).buildList());
    return block;
  }

  public static ParagraphBlock of(List<RichText> richTexts) {
    return of(richTexts, null);
  }

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
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends BlockWithChildren.Builder<Builder, ParagraphBlock> {

    private Builder() {}

    @Override
    public ParagraphBlock build() {
      ParagraphBlock block = new ParagraphBlock();
      buildContent(block.getParagraph());
      return block;
    }
  }

  @Getter
  @Setter
  public static class Paragraph extends BlockWithChildren {}
}
