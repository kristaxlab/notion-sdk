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

  /**
   * Creates a child page block with the given title (for testing or local construction only).
   *
   * @param title the child page title
   * @return a new ChildPageBlock
   */
  public static ChildPageBlock of(String title) {
    ChildPageBlock block = new ChildPageBlock();
    block.getChildPage().setTitle(title);
    return block;
  }

  /** The inner content object of a child page block. */
  @Getter
  @Setter
  public static class ChildPage {

    private String title;
  }
}
