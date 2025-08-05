package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class HeadingTwoBlock extends Block {
  @JsonProperty("heading_2")
  private Heading heading2;

  @Data
  public static class Heading {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("color")
    private String color;

    @Accessors(fluent = true)
    @JsonProperty("is_toggleable")
    private Boolean isToggleable;

    @JsonProperty("children")
    private List<Block> children;
  }
}
