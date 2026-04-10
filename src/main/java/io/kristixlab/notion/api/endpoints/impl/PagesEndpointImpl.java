package io.kristixlab.notion.api.endpoints.impl;

import static io.kristixlab.notion.api.endpoints.util.Validator.checkNotNull;
import static io.kristixlab.notion.api.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristixlab.notion.api.endpoints.PagesEndpoint;
import io.kristixlab.notion.api.http.base.client.ApiClient;
import io.kristixlab.notion.api.http.base.request.ApiPath;
import io.kristixlab.notion.api.model.block.Block;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.helper.NotionBlocksBuilder;
import io.kristixlab.notion.api.model.page.*;
import io.kristixlab.notion.api.model.page.markdown.ContentUpdate;
import io.kristixlab.notion.api.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristixlab.notion.api.model.page.property.PageProperty;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * API for interacting with Notion Pages endpoints. Provides methods to retrieve, create, and update
 * pages.
 */
public class PagesEndpointImpl extends BaseEndpointImpl implements PagesEndpoint {

  public PagesEndpointImpl(ApiClient client) {
    super(client);
  }

  public Page create(Consumer<CreatePageParams.Builder> consumer) {
    checkNotNull(consumer, "consumer");

    CreatePageParams.Builder builder = CreatePageParams.builder();
    consumer.accept(builder);
    return create(builder.build());
  }

  /**
   * Create a new page.
   *
   * @param request the request containing page data
   * @return the created page
   */
  public Page create(CreatePageParams request) {
    checkNotNull(request, "request");

    ApiPath urlInfo = ApiPath.from("/pages");
    return getClient().call("POST", urlInfo, request, Page.class);
  }

  /**
   * Creates a blank page with a title under the given parent.
   *
   * @param parent the parent page or datasource
   * @param title the page title
   * @return the created page
   */
  public Page create(Parent parent, String title) {
    return create(CreatePageParams.of(parent, title));
  }

  /**
   * Creates a page with a title and markdown body under the given parent.
   *
   * @param parent the parent page or datasource
   * @param title the page title
   * @param markdownContent the page body as a markdown string
   * @return the created page
   */
  public Page create(Parent parent, String title, String markdownContent) {
    return create(CreatePageParams.of(parent, title, markdownContent));
  }

  /**
   * Creates a page with a title and pre-built block content under the given parent.
   *
   * @param parent the parent page or datasource
   * @param title the page title
   * @param content the page body as a list of blocks
   * @return the created page
   */
  public Page create(Parent parent, String title, List<Block> content) {
    return create(CreatePageParams.builder().parent(parent).title(title).children(content).build());
  }

  /**
   * Creates a page with a title and inline content defined via the {@link NotionBlocksBuilder} DSL.
   *
   * @param parent the parent page or datasource
   * @param title the page title
   * @param consumer a consumer that populates the content builder
   * @return the created page
   */
  public Page create(Parent parent, String title, Consumer<NotionBlocksBuilder> consumer) {
    return create(
        CreatePageParams.builder().parent(parent).title(title).children(consumer).build());
  }

  /**
   * Retrieve a page by its ID.
   *
   * @param pageId The ID of the page to retrieve
   * @return The page object
   */
  public Page retrieve(String pageId) {
    checkNotNullOrEmpty(pageId, "pageId");

    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}").pathParam("page_id", pageId).build();
    return getClient().call("GET", urlInfo, Page.class);
  }

  public PageAsMarkdown retrieveAsMarkdown(String pageId) {
    return retrieveAsMarkdown(pageId, false);
  }

  public PageAsMarkdown retrieveAsMarkdown(String pageId, boolean includeTranscript) {
    checkNotNullOrEmpty(pageId, "pageId");

    ApiPath urlInfo =
        ApiPath.builder("/pages/{page_id}/markdown")
            .pathParam("page_id", pageId)
            .queryParam("include_transcript", String.valueOf(includeTranscript))
            .build();
    return getClient().call("GET", urlInfo, PageAsMarkdown.class);
  }

  public PageAsMarkdown updateAsMarkdown(String pageId, UpdatePageAsMarkdownParams request) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(request, "request");

    ApiPath urlInfo =
        ApiPath.builder("/pages/{page_id}/markdown").pathParam("page_id", pageId).build();
    return getClient().call("PATCH", urlInfo, request, PageAsMarkdown.class);
  }

  public PageAsMarkdown updateAsMarkdown(String pageId, String replaceStr, boolean allowDelete) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(replaceStr, "replaceContent");

    UpdatePageAsMarkdownParams request =
        UpdatePageAsMarkdownParams.replaceContent(replaceStr, allowDelete);

    return updateAsMarkdown(pageId, request);
  }

  public PageAsMarkdown updateAsMarkdown(
      String pageId, List<ContentUpdate> updates, boolean allowDelete) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(updates, "updates");

    UpdatePageAsMarkdownParams request =
        UpdatePageAsMarkdownParams.updateContent(updates, allowDelete);
    return updateAsMarkdown(pageId, request);
  }

  /**
   * Retrieve a specific page property.
   *
   * @param pageId The ID of the page
   * @param propertyId The ID of the property to retrieve
   * @return The property object
   */
  public PageProperty retrieveProperty(String pageId, String propertyId) {
    return retrieveProperty(pageId, propertyId, null, null);
  }

  /**
   * Retrieve a specific page property with pagination.
   *
   * @param pageId The ID of the page
   * @param propertyId The ID of the property to retrieve
   * @param startCursor Cursor for pagination (optional)
   * @param pageSize Number of items to return (optional, max 100)
   * @return The property object
   */
  public PageProperty retrieveProperty(
      String pageId, String propertyId, String startCursor, Integer pageSize) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNullOrEmpty(propertyId, "propertyId");

    ApiPath.Builder urlInfo =
        paginatedPath("/pages/{page_id}/properties/{property_id}", startCursor, pageSize)
            .pathParam("page_id", pageId)
            .pathParam("property_id", URLDecoder.decode(propertyId, StandardCharsets.UTF_8));
    return getClient().call("GET", urlInfo.build(), PageProperty.class);
  }

  /**
   * Update page properties.
   *
   * @param pageId The ID of the page to update
   * @param request The update request
   * @return The updated page
   */
  public Page update(String pageId, UpdatePageParams request) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(request, "request");

    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}").pathParam("page_id", pageId).build();
    return getClient().call("PATCH", urlInfo, request, Page.class);
  }

  public Page move(String pageId, Parent newParent) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(newParent, "newParent");

    MovePageParams request = new MovePageParams();
    request.setParent(newParent);
    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}/move").pathParam("page_id", pageId).build();

    return getClient().call("POST", urlInfo, request, Page.class);
  }

  /**
   * Archive a page.
   *
   * @param pageId The ID of the page to archive
   * @return The archived page
   */
  public Page delete(String pageId) {
    UpdatePageParams updatePageParams = new UpdatePageParams();
    updatePageParams.setInTrash(true);
    return update(pageId, updatePageParams);
  }

  /**
   * Unarchive a page.
   *
   * @param pageId The ID of the page to unarchive
   * @return The unarchived page
   */
  public Page restore(String pageId) {
    UpdatePageParams updatePageParams = new UpdatePageParams();
    updatePageParams.setInTrash(false);
    return update(pageId, updatePageParams);
  }
}
