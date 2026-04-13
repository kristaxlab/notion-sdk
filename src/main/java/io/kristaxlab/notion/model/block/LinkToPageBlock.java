package io.kristaxlab.notion.model.block;

import lombok.Getter;
import lombok.Setter;

/** A Notion link-to-page block that creates a reference to another page, database, or comment. */
@Getter
@Setter
public class LinkToPageBlock extends Block {

  private LinkToPage linkToPage;

  public LinkToPageBlock() {
    setType(BlockType.LINK_TO_PAGE.getValue());
    linkToPage = new LinkToPage();
  }

  /** The inner content object of a link-to-page block. */
  @Getter
  @Setter
  public static class LinkToPage {

    private String type;

    private String pageId;

    private String databaseId;

    private String commentId;
  }
}
