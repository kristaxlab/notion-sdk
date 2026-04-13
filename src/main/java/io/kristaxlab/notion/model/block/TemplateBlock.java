package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A read-only Notion template block. Represents legacy template button blocks that have been
 * deprecated by Notion (unrelated to database templates).
 */
@Getter
@Setter
public class TemplateBlock extends Block {

  private Template template;

  public TemplateBlock() {
    setType(BlockType.TEMPLATE.getValue());
    template = new Template();
  }

  /** The inner content object of a template block. */
  @Getter
  @Setter
  public static class Template {

    private List<RichText> richText;

    private List<Block> children;
  }
}
