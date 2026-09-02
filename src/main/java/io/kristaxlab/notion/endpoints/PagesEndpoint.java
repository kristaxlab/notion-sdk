package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.page.CreatePageParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.PageAsMarkdown;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import io.kristaxlab.notion.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.property.PagePropertyList;
import io.kristaxlab.notion.model.page.property.RetrievedProperty;

import java.util.function.Consumer;

/**
 * Notion Pages API: create, read, update, move, archive/restore, and Markdown import/export.
 *
 * @see <a href="https://developers.notion.com/reference/post-page">Create page</a>
 */
public interface PagesEndpoint {

  /**
   * Creates a page; use {@link CreatePageParams#builder()} and {@code underPage} / {@code
   * underDataSource} / etc.
   *
   * @param request page creation payload
   * @return created page
   */
  Page create(CreatePageParams request);

  /**
   * Creates a page by configuring {@link CreatePageParams.Builder} in a lambda.
   *
   * @param consumer callback that fills the creation builder
   * @return created page
   */
  Page create(Consumer<CreatePageParams.Builder> consumer);

  /**
   * Loads a page by ID (metadata and properties).
   *
   * @param pageId page identifier
   * @return retrieved page
   */
  Page retrieve(String pageId);

  /**
   * Returns a property from a page via {@code GET /pages/{id}/properties/{property_id}}.
   *
   * <p>Scalar types deserialize to typed {@link PageProperty} subclasses. Collection types
   * deserialize to {@link io.kristaxlab.notion.model.page.property.PagePropertyList} subclasses
   *
   * @param pageId page identifier
   * @param propertyId property identifier
   * @return retrieved property
   */
  RetrievedProperty retrieveProperty(String pageId, String propertyId);

  /**
   * Returns a paginated property list for collection types ('relation', 'title', 'people', 'rich_text')
   *
   * @param pageId page identifier
   * @param propertyId property identifier
   * @return first page of the paginated property list
   */
  PagePropertyList retrievePaginatedProperty(String pageId, String propertyId);

  /**
   * Returns a paginated property list for collection types ('relation', 'title', 'people', 'rich_text')
   * with cursor and page size.
   *
   * @param pageId page identifier
   * @param propertyId property identifier
   * @param startCursor pagination cursor
   * @param pageSize max page size
   * @return paginated property list page
   */
  PagePropertyList retrievePaginatedProperty(
      String pageId, String propertyId, String startCursor, Integer pageSize);

  /**
   * Patches page properties, icon, cover, trash state, etc.
   *
   * @param pageId page identifier
   * @param consumer callback that fills the update builder
   * @return updated page
   */
  Page update(String pageId, Consumer<UpdatePageParams.Builder> consumer);

  /**
   * Patches page properties, icon, cover, trash state, etc.
   *
   * @param pageId page identifier
   * @param request page update payload
   * @return updated page
   */
  Page update(String pageId, UpdatePageParams request);

  /**
   * Moves the page to a new parent ({@link Parent}).
   *
   * @param pageId page identifier
   * @param newParent destination parent
   * @return updated page in new location
   */
  Page move(String pageId, Parent newParent);

  /**
   * Archives the page (moves to trash).
   *
   * @param pageId page identifier
   * @return archived page
   */
  Page moveToTrash(String pageId);

  /**
   * Restores the page from trash.
   *
   * @param pageId page identifier
   * @return restored page
   */
  Page restore(String pageId);

  /**
   * Exports page content as Markdown.
   *
   * @param pageId page identifier
   * @return markdown export payload
   */
  PageAsMarkdown retrieveAsMarkdown(String pageId);

  /**
   * Exports page content as Markdown.
   *
   * @param pageId page identifier
   * @param includeTranscript whether to include transcript data where applicable
   * @return markdown export payload
   */
  PageAsMarkdown retrieveAsMarkdown(String pageId, boolean includeTranscript);

  /**
   * Updates page content from Markdown.
   *
   * @param pageId page identifier
   * @param request markdown update payload
   * @return page content after update
   */
  PageAsMarkdown updateAsMarkdown(String pageId, UpdatePageAsMarkdownParams request);

  /**
   * Updates page content from Markdown by configuring {@link UpdatePageAsMarkdownParams.Builder} in
   * a lambda.
   *
   * @param pageId page identifier
   * @param consumer callback that fills the update builder
   * @return page content after update
   */
  PageAsMarkdown updateAsMarkdown(
      String pageId, Consumer<UpdatePageAsMarkdownParams.Builder> consumer);

  /**
   * Replaces entire page content with the provided markdown string.
   *
   * @param pageId page identifier
   * @param markdown new markdown content
   * @return page content after update
   */
  PageAsMarkdown updateAsMarkdown(String pageId, String markdown);
}
