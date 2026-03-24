package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.PagesEndpoint;
import io.kristixlab.notion.api.http.client.ApiClient;
import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.*;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * API for interacting with Notion Pages endpoints. Provides methods to retrieve, create, and update
 * pages.
 */
public class PagesEndpointImpl implements PagesEndpoint {

  private static final String PAGE_ID = "page_id";
  private static final String PROPERTY_ID = "property_id";

  private final ApiClient client;

  public PagesEndpointImpl(ApiClient client) {
    this.client = client;
  }

  /**
   * Create a new page.
   *
   * @param request The request containing page data
   * @return The created page
   */
  public Page create(CreatePageParams request) {
    validateRequest(request);
    ApiPath urlInfo = ApiPath.from("/pages");
    return client.call("POST", urlInfo, request, Page.class);
  }

  /**
   * Retrieve a page by its ID.
   *
   * @param pageId The ID of the page to retrieve
   * @return The page object
   */
  public Page retrieve(String pageId) {
    validatePageId(pageId);
    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}").pathParam(PAGE_ID, pageId).build();
    return client.call("GET", urlInfo, Page.class);
  }

  public PageAsMarkdown retrieveAsMarkdown(String pageId) {
    return retrieveAsMarkdown(pageId, false);
  }

  public PageAsMarkdown retrieveAsMarkdown(String pageId, boolean includeTranscript) {
    validatePageId(pageId);
    ApiPath urlInfo =
        ApiPath.builder("/pages/{page_id}/markdown")
            .pathParam(PAGE_ID, pageId)
            .queryParam("include_transcript", includeTranscript)
            .build();
    return client.call("GET", urlInfo, PageAsMarkdown.class);
  }

  public PageAsMarkdown updateAsMarkdown(String pageId, UpdatePageAsMarkdownParams request) {
    validatePageId(pageId);
    validateRequest(request);
    ApiPath urlInfo =
        ApiPath.builder("/pages/{page_id}/markdown").pathParam(PAGE_ID, pageId).build();
    return client.call("PATCH", urlInfo, request, PageAsMarkdown.class);
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
    validatePageId(pageId);
    validatePropertyId(propertyId);
    ApiPath.Builder urlInfo =
        ApiPath.builder("/pages/{page_id}/properties/{property_id}", startCursor, pageSize)
            .pathParam(PAGE_ID, pageId)
            .pathParam(PROPERTY_ID, URLDecoder.decode(propertyId, StandardCharsets.UTF_8));
    return client.call("GET", urlInfo.build(), PageProperty.class);
  }

  /**
   * Update page properties.
   *
   * @param pageId The ID of the page to update
   * @param request The update request
   * @return The updated page
   */
  public Page update(String pageId, UpdatePageParams request) {
    validatePageId(pageId);
    validateRequest(request);
    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}").pathParam(PAGE_ID, pageId).build();
    return client.call("PATCH", urlInfo, request, Page.class);
  }

  public Page move(String pageId, Parent newParent) {
    validatePageId(pageId);
    validateRequest(newParent);

    MovePageParams request = new MovePageParams();
    request.setParent(newParent);
    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}/move").pathParam(PAGE_ID, pageId).build();

    return client.call("POST", urlInfo, request, Page.class);
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

  /**
   * Validates the page ID.
   *
   * @param pageId The page ID to validate
   * @throws IllegalArgumentException if the page ID is null or empty
   */
  private void validatePageId(String pageId) {
    if (pageId == null || pageId.trim().isEmpty()) {
      throw new IllegalArgumentException("Page ID cannot be null or empty");
    }
  }

  /**
   * Validates the request object.
   *
   * @param request The request object to validate
   * @throws IllegalArgumentException if the request is null
   */
  private void validateRequest(Object request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }

  /**
   * Validates the property ID.
   *
   * @param propertyId The property ID to validate
   * @throws IllegalArgumentException if the property ID is null or empty
   */
  private void validatePropertyId(String propertyId) {
    if (propertyId == null || propertyId.trim().isEmpty()) {
      throw new IllegalArgumentException("Property ID cannot be null or empty");
    }
  }
}
