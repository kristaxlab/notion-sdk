package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkToPageBlock extends Block {

  private LinkToPage linkToPage;

  public LinkToPageBlock() {
    setType("link_to_page");
    linkToPage = new LinkToPage();
  }

  @Getter
  @Setter
  public static class LinkToPage {

    private String type;

    private String pageId;

    private String databaseId;

    private String commentId;
  }

  public static LinkToPageBlock pageLink(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("page_id");
    block.getLinkToPage().setPageId(id);
    return block;
  }

  public static LinkToPageBlock databaseLink(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("database_id");
    block.getLinkToPage().setDatabaseId(id);
    return block;
  }

  public static LinkToPageBlock commentLink(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("comment_id");
    block.getLinkToPage().setCommentId(id);
    return block;
  }
}
