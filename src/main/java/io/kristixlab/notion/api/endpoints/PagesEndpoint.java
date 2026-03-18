package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageParams;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;

/*
 * Interface defining operations for Notion Pages.
 * @see <a href="https://developers.notion.com/reference/post-page">Notion Pages Endpoint</a>
 */
public interface PagesEndpoint {

  Page create(CreatePageParams request);

  Page retrieve(String pageId);

  PageProperty retrieveProperty(String pageId, String propertyId);

  PageProperty retrieveProperty(
      String pageId, String propertyId, String startCursor, Integer pageSize);

  Page update(String pageId, UpdatePageParams request);

  Page move(String pageId, Parent parent);

  Page delete(String pageId);

  Page restore(String pageId);
}
