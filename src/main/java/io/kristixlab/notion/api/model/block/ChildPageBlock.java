package io.kristixlab.notion.api.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * A read-only Notion child page block. Returned by the API to represent a nested page reference;
 * cannot be created via the API.
 */
@Getter
@Setter
public class ChildPageBlock extends Block {

  private ChildPage childPage;

  public ChildPageBlock() {
    setType("child_page");
    childPage = new ChildPage();
  }

  @Getter
  @Setter
  public static class ChildPage {

    private String title;
  }
}
