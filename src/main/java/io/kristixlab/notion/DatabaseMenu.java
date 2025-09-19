package io.kristixlab.notion;

import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.client.NotionClient;

import java.util.HashMap;
import java.util.Map;

public class DatabaseMenu {

  private final NotionClient client;
  private final String databaseId;
  private String operation;
  private String moveToPage;
  private String moveToDatasource;
  private Boolean moveToTopLevel;
  private Map<String, Object> properties = new HashMap<>();

  public DatabaseMenu(NotionClient client, String databaseId) {
    this.client = client;
    this.databaseId = databaseId;
  }

  public DatabaseMenu moveToPage(String pageId) {
    this.moveToPage = pageId;
    return this;
  }

  public DatabaseMenu moveToDatasource(String datasourceId) {
    this.moveToDatasource = datasourceId;
    return this;
  }

  public DatabaseMenu moveToTopLeve() {
    this.moveToTopLevel = true;
    return this;
  }

  public DatabaseMenu addPage(Map<String, Object> properties) {
    this.operation = "addPage";
    this.properties.putAll(properties);
    return this;
  }

  public String getDatabaseId() {
    return databaseId;
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public String getOperation() {
    return operation;
  }

  public Page execute() {
    return client.execute(this);
  }
}
