package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageRequest;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;

/*
 * Interface defining operations for Notion Pages.
 * @see <a href="https://developers.notion.com/reference/post-page">Notion Pages Endpoint</a>
 */
public interface PagesEndpoint {

  Page create(Page request);

  Page retrieve(String pageId);

  PageProperty retrieveProperty(String pageId, String propertyId);

  PageProperty retrieveProperty(String pageId, String propertyId, String startCursor, Integer pageSize);

  Page update(String pageId, UpdatePageRequest request);

  Page delete(String pageId);

  Page restore(String pageId);
}
