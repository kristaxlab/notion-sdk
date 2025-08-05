package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ToggleBlock extends Block {
  @JsonProperty("toggle")
  private Toggle toggle;

  @Data
  public static class Toggle {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("color")
    private String color;

    @JsonProperty("children")
    private List<Block> children;
  }
}
