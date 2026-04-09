package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion embed block that displays an embedded external content (e.g., maps, tweets, videos).
 *
 * <p>Simple construction via {@link #of(String)}. For adding a caption use {@link #builder()}.
 */
@Getter
@Setter
public class EmbedBlock extends Block {
  private Embed embed;

  public EmbedBlock() {
    setType("embed");
    embed = new Embed();
  }

  /** The inner content object of an embed block. */
  @Getter
  @Setter
  public static class Embed {

    private String url;

    private List<RichText> caption;
  }

  /**
   * Creates an embed block with the given URL.
   *
   * @param url the URL to embed
   * @return a new EmbedBlock
   */
  public static EmbedBlock of(String url) {
    EmbedBlock block = new EmbedBlock();
    block.getEmbed().setUrl(url);
    return block;
  }

  /**
   * Returns a new builder for constructing an {@link EmbedBlock} with URL and optional caption.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link EmbedBlock}. */
  public static class Builder {

    private String url;

    private List<RichText> caption;

    private Builder() {}

    /**
     * Sets the embed URL.
     *
     * @param url the URL to embed
     * @return this builder
     */
    public Builder url(String url) {
      this.url = url;
      return this;
    }

    /**
     * Sets the caption from a plain text string.
     *
     * @param caption the caption text
     * @return this builder
     */
    public Builder caption(String caption) {
      this.caption = RichText.of(caption);
      return this;
    }

    /**
     * Sets the caption from a pre-built rich text list.
     *
     * @param caption the caption rich text elements
     * @return this builder
     */
    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    /**
     * Builds the {@link EmbedBlock}.
     *
     * @return a new EmbedBlock
     */
    public EmbedBlock build() {
      EmbedBlock block = new EmbedBlock();
      block.getEmbed().setUrl(url);
      block.getEmbed().setCaption(caption);
      return block;
    }
  }
}
