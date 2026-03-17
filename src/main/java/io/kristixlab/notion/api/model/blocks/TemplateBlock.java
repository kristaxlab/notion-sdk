package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*
 * @notionapinotes deprecated after March 27, 2023
 * Read only, may return in response, but can not be created/updated with Notion API
 * more details: https://developers.notion.com/reference/block#template
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateBlock extends Block {
  @JsonProperty("template")
  private Template template;

  public TemplateBlock() {
    setType("template");
    template = new Template();
  }

  @Data
  public static class Template {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("children")
    private List<Block> children;
  }
}
