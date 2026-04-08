package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

/**
 * A Notion link-to-page block that creates a reference to another page, database, or comment.
 *
 * <p>Use the named factories {@link #pageLink(String)}, {@link #databaseLink(String)}, or {@link
 * #commentLink(String)} depending on the target type.
 */
@Getter
@Setter
public class LinkToPageBlock extends Block {

  private LinkToPage linkToPage;

  public LinkToPageBlock() {
    setType("link_to_page");
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

  /**
   * Creates a link-to-page block pointing to a page.
   *
   * @param id the target page ID
   * @return a new LinkToPageBlock
   */
  public static LinkToPageBlock pageLink(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("page_id");
    block.getLinkToPage().setPageId(id);
    return block;
  }

  /**
   * Creates a link-to-page block pointing to a database.
   *
   * @param id the target database ID
   * @return a new LinkToPageBlock
   */
  public static LinkToPageBlock databaseLink(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("database_id");
    block.getLinkToPage().setDatabaseId(id);
    return block;
  }

  /**
   * Creates a link-to-page block pointing to a comment.
   *
   * @param id the target comment ID
   * @return a new LinkToPageBlock
   */
  public static LinkToPageBlock commentLink(String id) {
    LinkToPageBlock block = new LinkToPageBlock();
    block.getLinkToPage().setType("comment_id");
    block.getLinkToPage().setCommentId(id);
    return block;
  }
}
