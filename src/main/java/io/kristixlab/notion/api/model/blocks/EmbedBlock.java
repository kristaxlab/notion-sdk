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
}
