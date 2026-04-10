package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.block.Block;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.helper.NotionBlocksBuilder;
import io.kristixlab.notion.api.model.page.*;
import io.kristixlab.notion.api.model.page.markdown.ContentUpdate;
import io.kristixlab.notion.api.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristixlab.notion.api.model.page.property.PageProperty;
import java.util.List;
import java.util.function.Consumer;

/*
 * Interface defining operations for Notion Pages.
 * @see <a href="https://developers.notion.com/reference/post-page">Notion Pages Endpoint</a>
 */
public interface PagesEndpoint {

  Page create(CreatePageParams request);

  Page create(Consumer<CreatePageParams.Builder> consumer);

  Page create(Parent parent, String title);

  Page create(Parent parent, String title, String markdownContent);

  Page create(Parent parent, String title, List<Block> content);

  Page create(Parent parent, String title, Consumer<NotionBlocksBuilder> consumer);

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

  PageAsMarkdown updateAsMarkdown(String pageId, String replaceContent, boolean allowDelete);

  PageAsMarkdown updateAsMarkdown(String pageId, List<ContentUpdate> updates, boolean allowDelete);
}
