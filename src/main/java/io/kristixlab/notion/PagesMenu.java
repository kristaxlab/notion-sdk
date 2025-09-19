package io.kristixlab.notion;

import io.kristixlab.notion.client.NotionClient;

public class PagesMenu {

  private final NotionClient client;

  public PagesMenu(NotionClient client) {
    this.client = client;
  }

  public PageMenu byId(String pageId) {
    return new PageMenu(client, pageId);
  }
}
