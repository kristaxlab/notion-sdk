package io.kristixlab.notion;

public class Notion {

  private NotionClient client;

  public Notion(NotionClient client) {
    this.client = client;
  }

  public PageOperations page(String pageId) {
    return new PageOperations(client, pageId);
  }

  static class PageOperations {
    private NotionClient client;
    private String pageId;

    public PageOperations(NotionClient client, String pageId) {
      this.client = client;
      this.pageId = pageId;
    }

    // TODO do we need to add a separate fluent model? Page class with Blocks inside
    public NotionApiRetriveBlocksCall withContent() {
      return new NotionApiRetriveBlocksCall(client, pageId);
    }
  }
}
