package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO looks like it only can return in response but is not allowed in request
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ChildPageBlock extends Block {
  @JsonProperty("child_page")
  private ChildPage childPage;

  public ChildPageBlock() {
    setType("child_page");
    childPage = new ChildPage();
  }

  public static ChildPageBlock of(String title) {
    ChildPageBlock block = new ChildPageBlock();
    block.getChildPage().setTitle(title);
    return block;
  }

  @Data
  public static class ChildPage {
    @JsonProperty("title")
    private String title;
  }
}
