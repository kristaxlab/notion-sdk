package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbedBlock extends Block {
  private Embed embed;

  public EmbedBlock() {
    setType("embed");
    embed = new Embed();
  }

  @Getter
  @Setter
  public static class Embed {

    private String url;

    private List<RichText> caption;
  }

  public static EmbedBlock of(String url) {
    EmbedBlock block = new EmbedBlock();
    block.getEmbed().setUrl(url);
    return block;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String url;

    private List<RichText> caption;

    private Builder() {}

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder caption(String caption) {
      this.caption = RichText.of(caption);
      return this;
    }

    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    public EmbedBlock build() {
      EmbedBlock block = new EmbedBlock();
      block.getEmbed().setUrl(url);
      block.getEmbed().setCaption(caption);
      return block;
    }
  }
}
