package io.kristixlab.notion;

import io.kristixlab.notion.api.NotionApiClient;

public class Notion {

  private NotionApiClient client;

  public Notion(NotionApiClient client) {
    this.client = client;
  }

  public PageOperations page(String pageId) {
    return new PageOperations(client, pageId);
  }

  static class PageOperations {
    private NotionApiClient client;
    private String pageId;

    public PageOperations(NotionApiClient client, String pageId) {
      this.client = client;
      this.pageId = pageId;
    }

    // TODO do we need to add a separate fluent model? Page class with Blocks inside
    public NotionApiRetriveBlocksCall withContent() {
      return new NotionApiRetriveBlocksCall(client, pageId);
    }
  }
}
