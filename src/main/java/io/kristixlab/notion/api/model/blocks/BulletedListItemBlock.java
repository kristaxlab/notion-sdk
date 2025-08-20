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
public class BulletedListItemBlock extends Block {
  @JsonProperty("bulleted_list_item")
  private BulletedListItem bulletedListItem;

  @Data
  public static class BulletedListItem {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("color")
    private String color;

    @JsonProperty("children")
    private List<Block> children;
  }
}
