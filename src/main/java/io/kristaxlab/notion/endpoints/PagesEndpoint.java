package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.page.*;
import io.kristaxlab.notion.model.page.CreatePageParams;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.PageAsMarkdown;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import io.kristaxlab.notion.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristaxlab.notion.model.page.property.PageProperty;
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
   */
  Page create(CreatePageParams request);

  /** Creates a page by configuring {@link CreatePageParams.Builder} in a lambda. */
  Page create(Consumer<CreatePageParams.Builder> consumer);

  /** Loads a page by ID (metadata and properties). */
  Page retrieve(String pageId);

  /** Returns a single property value from a page. */
  PageProperty retrieveProperty(String pageId, String propertyId);

  /** Returns a property with optional pagination (for rollups and similar). */
  PageProperty retrieveProperty(
      String pageId, String propertyId, String startCursor, Integer pageSize);

  /** Patches page properties, icon, cover, trash state, etc. */
  Page update(String pageId, UpdatePageParams request);

  /** Moves the page to a new parent ({@link Parent}). */
  Page move(String pageId, Parent newParent);

  /** Archives the page (moves to trash). */
  Page delete(String pageId);

  /** Restores the page from trash. */
  Page restore(String pageId);

  /** Exports page content as Markdown. */
  PageAsMarkdown retrieveAsMarkdown(String pageId);

  /**
   * Exports page content as Markdown.
   *
   * @param includeTranscript whether to include transcript data where applicable
   */
  PageAsMarkdown retrieveAsMarkdown(String pageId, boolean includeTranscript);

  /** Updates page content from Markdown. */
  PageAsMarkdown updateAsMarkdown(String pageId, UpdatePageAsMarkdownParams request);
}
