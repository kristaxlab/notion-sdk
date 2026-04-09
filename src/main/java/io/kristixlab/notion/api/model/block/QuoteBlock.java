package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion quote block.
 *
 * <p>Simple construction via {@link #of(String)}. For rich text formatting, nested children, or
 * block color use {@link #builder()}.
 */
@Getter
@Setter
public class QuoteBlock extends Block {

  private Quote quote;

  public QuoteBlock() {
    setType("quote");
    quote = new Quote();
  }

  /**
   * Creates a quote block with plain text content.
   *
   * @param text the quote text
   * @return a new QuoteBlock
   */
  public static QuoteBlock of(String text) {
    QuoteBlock block = new QuoteBlock();
    block.getQuote().setRichText(RichText.of(text));
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link QuoteBlock} with rich text formatting,
   * block-level color, and/or nested children.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link QuoteBlock}. */
  public static class Builder extends BlockWithChildren.Builder<Builder, QuoteBlock> {

    private Builder() {}

    @Override
    public QuoteBlock build() {
      QuoteBlock block = new QuoteBlock();
      buildContent(block.getQuote());
      return block;
    }
  }

  /** The inner content object of a quote block. */
  @Getter
  @Setter
  public static class Quote extends BlockWithChildren {}
}
