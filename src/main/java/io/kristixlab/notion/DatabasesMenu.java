package io.kristixlab.notion;

import io.kristixlab.notion.client.NotionClient;
import lombok.Data;

@Data
public class DatabasesMenu {
  private final NotionClient client;

  public DatabasesMenu(NotionClient client) {
    this.client = client;
  }

  public DatabaseMenu byId(String pageId) {
    return new DatabaseMenu(client, pageId);
  }
}
