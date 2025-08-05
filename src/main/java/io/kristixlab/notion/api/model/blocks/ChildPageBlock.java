package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ChildPageBlock extends Block {
  @JsonProperty("child_page")
  private ChildPage childPage;

  @Data
  public static class ChildPage {
    @JsonProperty("title")
    private String title;
  }
}
