package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.PagesEndpoint;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.http.transport.HttpTransportImpl;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.http.transport.util.URLInfoBuilder;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageParams;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.util.Pagination;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * API for interacting with Notion Pages endpoints. Provides methods to retrieve, create, and update
 * pages.
 */
public class PagesEndpointImpl implements PagesEndpoint {

  private static final String PAGE_ID = "page_id";
  private static final String PROPERTY_ID = "property_id";

  private final HttpTransportImpl transport;

  public PagesEndpointImpl(NotionHttpTransport transport) {
    this.transport = transport;
  }

  /**
   * Create a new page.
   *
   * @param request The request containing page data
   * @return The created page
   */
  public Page create(CreatePageParams request) {
    validateRequest(request);
    URLInfo urlInfo = URLInfo.build("/pages");
    return transport.call("POST", urlInfo, request, Page.class);
  }

  /**
   * Retrieve a page by its ID.
   *
   * @param pageId The ID of the page to retrieve
   * @return The page object
   */
  public Page retrieve(String pageId) {
    validatePageId(pageId);
    URLInfo urlInfo = URLInfo.builder("/pages/{page_id}").pathParam(PAGE_ID, pageId).build();
    return transport.call("GET", urlInfo, Page.class);
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

    URLInfoBuilder urlInfo =
        URLInfo.builder("/pages/{page_id}/properties/{property_id}")
            .pathParam(PAGE_ID, pageId)
            .pathParam(PROPERTY_ID, URLDecoder.decode(propertyId, StandardCharsets.UTF_8));

    if (startCursor != null) {
      urlInfo.queryParam(Pagination.START_CURSOR, startCursor);
    }

    if (pageSize != null) {
      urlInfo.queryParam(Pagination.PAGE_SIZE, pageSize).build();
    }

    return transport.call("GET", urlInfo.build(), PageProperty.class);
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

    URLInfo urlInfo = URLInfo.builder("/pages/{page_id}").pathParam(PAGE_ID, pageId).build();

    return transport.call("PATCH", urlInfo, request, Page.class);
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
