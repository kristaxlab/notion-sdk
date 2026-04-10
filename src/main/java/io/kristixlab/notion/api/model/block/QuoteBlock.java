package io.kristixlab.notion.api.model.block;

import lombok.Getter;
import lombok.Setter;

/** A Notion quote block. */
@Getter
@Setter
public class QuoteBlock extends Block {

  private Quote quote;

  public QuoteBlock() {
    setType("quote");
    quote = new Quote();
  }

  /** The inner content object of a quote block. */
  @Getter
  @Setter
  public static final class Quote extends BlockWithChildren {}

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
}
