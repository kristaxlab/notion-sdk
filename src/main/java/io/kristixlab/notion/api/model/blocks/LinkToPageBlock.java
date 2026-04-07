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

    private Boolean workspace;
  }
}
