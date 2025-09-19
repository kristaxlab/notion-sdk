package io.kristixlab.notion.client;

import io.kristixlab.notion.DatabaseMenu;
import io.kristixlab.notion.DatabasesMenu;
import io.kristixlab.notion.PageMenu;
import io.kristixlab.notion.PagesMenu;
import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenRequest;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageRequest;
import io.kristixlab.notion.api.model.pages.properties.DateProperty;
import io.kristixlab.notion.api.model.pages.properties.NumberProperty;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.properties.RichTextProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class NotionClient {

  private NotionApiClient apiClient;

  public NotionClient() {
    String authToken = System.getenv("NOTION_API_KEY");
    if (authToken == null || authToken.isEmpty()) {
      throw new IllegalArgumentException("Environment variable NOTION_API_KEY is not set or empty");
    }
    this.apiClient = new NotionApiClient(authToken);
  }

  public PagesMenu pages() {
    return new PagesMenu(this);
  }

  public DatabasesMenu databases() {
    return new DatabasesMenu(this);
  }

  public Page execute(PageMenu pageMenu) {
    if (pageMenu.getPageId() == null) {
      throw new IllegalArgumentException("Page id cannot be null");
    }

    if (!pageMenu.getBlocksToAppend().isEmpty()) {
      AppendBlockChildrenRequest appendBlocksRq = new AppendBlockChildrenRequest();
      appendBlocksRq.getChildren().addAll(pageMenu.getBlocksToAppend());
      apiClient.blocks().appendChildren(pageMenu.getPageId(), appendBlocksRq);
      return apiClient.pages().retrieve(pageMenu.getPageId());
    }

    // TODO only with workaround
    UpdatePageRequest rq = new UpdatePageRequest();
    if (pageMenu.getMoveToPage() != null) {
      rq.setParent(Parent.pageParent(pageMenu.getMoveToPage()));
    } else if (pageMenu.getMoveToDatasource() != null) {
      rq.setParent(Parent.datasourceParent(pageMenu.getMoveToDatasource()));
    } else if (pageMenu.getMoveToTopLevel() != null && pageMenu.getMoveToTopLevel()) {
      rq.setParent(Parent.workspaceParent());
    } else {
      throw new IllegalArgumentException("No action specified for page move");
    }
    return apiClient.pages().update(pageMenu.getPageId(), rq);
  }

  public Page execute(DatabaseMenu databaseMenu) {
    if (databaseMenu.getDatabaseId() == null) {
      throw new IllegalArgumentException("Database id cannot be null");
    }

    if (databaseMenu.getOperation() != null && databaseMenu.getOperation().equals("addPage")) {
      Page page = new Page();
      page.setParent(Parent.datasourceParent(databaseMenu.getDatabaseId()));
      for (Map.Entry<String, Object> entry : databaseMenu.getProperties().entrySet()) {
        if (entry.getValue() instanceof PageProperty) {
          page.getProperties().put(entry.getKey(), (PageProperty) entry.getValue());
        } else if (entry.getValue() instanceof String) {
          page.getProperties().put(entry.getKey(), RichTextProperty.of((String) entry.getValue()));
        } else if (entry.getValue() instanceof Number) {
          page.getProperties().put(entry.getKey(), NumberProperty.of((Number) entry.getValue()));
        } else if (entry.getValue() instanceof LocalDateTime) {
          page.getProperties().put(entry.getKey(), DateProperty.of((LocalDateTime) entry.getValue()));
        } else if (entry.getValue() instanceof LocalDate) {
          page.getProperties().put(entry.getKey(), DateProperty.of((LocalDate) entry.getValue()));
        }
      }
      return apiClient.pages().create(page);
    }

    throw new IllegalStateException("Not enough data to perform operation");
  }

}
