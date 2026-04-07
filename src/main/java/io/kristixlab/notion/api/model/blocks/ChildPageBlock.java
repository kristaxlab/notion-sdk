package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

/** TODO looks like it only can return in response but is not allowed in request */
@Getter
@Setter
public class ChildPageBlock extends Block {

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

  @Getter
  @Setter
  public static class ChildPage {

    private String title;
  }
}
