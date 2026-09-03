package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;
import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.PagesEndpoint;
import io.kristaxlab.notion.fluent.NotionBlocksBuilder;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.page.*;
import io.kristaxlab.notion.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.property.PagePropertyList;
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
   * @param parent the parent page or dataSource
   * @param title the page title
   * @return the created page
   */
  private Page create(Parent parent, String title) {
    return create(CreatePageParams.builder().parent(parent).title(title).build());
  }

  /**
   * Creates a page with a title and markdown body under the given parent.
   *
   * @param parent the parent page or dataSource
   * @param title the page title
   * @param markdownContent the page body as a markdown string
   * @return the created page
   */
  private Page create(Parent parent, String title, String markdownContent) {
    return create(
        CreatePageParams.builder().parent(parent).title(title).markdown(markdownContent).build());
  }

  /**
   * Creates a page with a title and pre-built block content under the given parent.
   *
   * @param parent the parent page or dataSource
   * @param title the page title
   * @param content the page body as a list of blocks
   * @return the created page
   */
  private Page create(Parent parent, String title, List<Block> content) {
    return create(CreatePageParams.builder().parent(parent).title(title).children(content).build());
  }

  /**
   * Creates a page with a title and inline content defined via the {@link NotionBlocksBuilder} DSL.
   *
   * @param parent the parent page or dataSource
   * @param title the page title
   * @param consumer a consumer that populates the content builder
   * @return the created page
   */
  private Page create(Parent parent, String title, Consumer<NotionBlocksBuilder> consumer) {
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

  public PageAsMarkdown updateAsMarkdown(
      String pageId, Consumer<UpdatePageAsMarkdownParams.Builder> consumer) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(consumer, "consumer");

    UpdatePageAsMarkdownParams.Builder builder = UpdatePageAsMarkdownParams.builder();
    consumer.accept(builder);
    return updateAsMarkdown(pageId, builder.build());
  }

  public PageAsMarkdown updateAsMarkdown(String pageId, String markdown) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNull(markdown, "markdown");

    return updateAsMarkdown(pageId, UpdatePageAsMarkdownParams.replaceContent(markdown));
  }

  /**
   * Retrieve a specific page property.
   *
   * @param pageId The ID of the page
   * @param propertyId The ID of the property to retrieve
   * @return The property object
   */
  @Override
  public PageProperty retrieveProperty(String pageId, String propertyId) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNullOrEmpty(propertyId, "propertyId");

    ApiPath urlInfo =
        ApiPath.builder("/pages/{page_id}/properties/{property_id}")
            .pathParam("page_id", pageId)
            .pathParam("property_id", URLDecoder.decode(propertyId, StandardCharsets.UTF_8))
            .build();
    return getClient().call("GET", urlInfo, PageProperty.class);
  }

  @Override
  public PagePropertyList retrievePaginatedProperty(String pageId, String propertyId) {
    return retrievePaginatedProperty(pageId, propertyId, null, null);
  }

  @Override
  public PagePropertyList retrievePaginatedProperty(
      String pageId, String propertyId, String startCursor, Integer pageSize) {
    checkNotNullOrEmpty(pageId, "pageId");
    checkNotNullOrEmpty(propertyId, "propertyId");

    ApiPath.Builder urlInfo =
        paginatedPath("/pages/{page_id}/properties/{property_id}", startCursor, pageSize)
            .pathParam("page_id", pageId)
            .pathParam("property_id", URLDecoder.decode(propertyId, StandardCharsets.UTF_8));
    return getClient().call("GET", urlInfo.build(), PagePropertyList.class);
  }

  @Override
  public Page update(String pageId, Consumer<UpdatePageParams.Builder> consumer) {
    UpdatePageParams.Builder builder = UpdatePageParams.builder();
    consumer.accept(builder);
    return update(pageId, builder.build());
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
  public Page moveToTrash(String pageId) {
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
