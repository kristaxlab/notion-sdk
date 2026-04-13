package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.page.*;
import io.kristixlab.notion.api.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristixlab.notion.api.model.page.property.PageProperty;
import java.util.function.Consumer;

/*
 * Interface defining operations for Notion Pages.
 * @see <a href="https://developers.notion.com/reference/post-page">Notion Pages Endpoint</a>
 */
public interface PagesEndpoint {

  Page create(CreatePageParams request);

  Page create(Consumer<CreatePageParams.Builder> consumer);

  Page retrieve(String pageId);

  PageProperty retrieveProperty(String pageId, String propertyId);

  PageProperty retrieveProperty(
      String pageId, String propertyId, String startCursor, Integer pageSize);

  Page update(String pageId, UpdatePageParams request);

  Page move(String pageId, Parent newParent);

  Page delete(String pageId);

  Page restore(String pageId);

  PageAsMarkdown retrieveAsMarkdown(String pageId);

  PageAsMarkdown retrieveAsMarkdown(String pageId, boolean includeTranscript);

  PageAsMarkdown updateAsMarkdown(String pageId, UpdatePageAsMarkdownParams request);
}
