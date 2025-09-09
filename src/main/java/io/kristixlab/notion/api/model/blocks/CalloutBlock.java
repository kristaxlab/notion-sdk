package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class CalloutBlock extends Block {

  @JsonProperty("callout")
  private Callout callout;

  @Data
  public static class Callout {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("icon")
    private Icon icon;

    @JsonProperty("color")
    private String color;

    @JsonProperty("children")
    private List<Block> children;
  }
}
