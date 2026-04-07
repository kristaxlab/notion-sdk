package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateBlock extends Block {

  private Template template;

  public TemplateBlock() {
    setType("template");
    template = new Template();
  }

  @Getter
  @Setter
  public static class Template {

    private List<RichText> richText;

    private List<Block> children;
  }
}
