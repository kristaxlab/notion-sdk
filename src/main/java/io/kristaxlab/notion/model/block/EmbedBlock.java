package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.fluent.NotionTextBuilder;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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
    setType(BlockType.EMBED.getValue());
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

    private List<RichText> caption = new ArrayList<>();

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

    public Builder caption(String caption) {
      this.caption.add(NotionText.plainText(caption));
      return this;
    }

    public Builder caption(RichText... caption) {
      return caption(Arrays.asList(caption));
    }

    public Builder caption(List<RichText> caption) {
      this.caption.addAll(caption);
      return this;
    }

    public Builder caption(Consumer<NotionTextBuilder> consumer) {
      NotionTextBuilder builder = new NotionTextBuilder();
      consumer.accept(builder);
      this.caption.addAll(builder.build());
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
      if (caption != null && !caption.isEmpty()) {
        block.getEmbed().setCaption(new ArrayList<>(caption));
      }
      return block;
    }
  }
}
