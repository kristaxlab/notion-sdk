package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.ApiTransport;
import io.kristixlab.notion.api.exchange.NotionApiTransport;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * API for interacting with Notion Pages endpoints. Provides methods to retrieve, create, and update
 * pages.
 */
public class PagesApi {

  private static final String PAGE_ID = "page_id";
  private static final String PROPERTY_ID = "property_id";

  private final ApiTransport transport;

  public PagesApi(NotionApiTransport transport) {
    this.transport = transport;
  }

  /**
   * Retrieve a page by its ID.
   *
   * @param pageId The ID of the page to retrieve
   * @return The page object
   */
  public Page retrieve(String pageId) {
    validatePageId(pageId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(PAGE_ID, pageId);

    return transport.call("GET", "/pages/{page_id}", null, pathParams, null, Page.class);
  }

  /**
   * Create a new page.
   *
   * @param request The request containing page data
   * @return The created page
   */
  public Page create(Page request) {
    validateRequest(request);

    return transport.call("POST", "/pages", null, null, request, Page.class);
  }

  /**
   * Update page properties.
   *
   * @param pageId The ID of the page to update
   * @param request The update request
   * @return The updated page
   */
  public Page updateProperties(String pageId, Page request) {
    validatePageId(pageId);
    validateRequest(request);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(PAGE_ID, pageId);

    return transport.call("PATCH", "/pages/{page_id}", null, pathParams, request, Page.class);
  }

  /**
   * Archive a page.
   *
   * @param pageId The ID of the page to archive
   * @return The archived page
   */
  public Page delete(String pageId) {
    validatePageId(pageId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(PAGE_ID, pageId);
    Page body = new Page();
    body.setArchived(true);
    body.setInTrash(true);

    return transport.call("PATCH", "/pages/{page_id}", null, pathParams, body, Page.class);
  }

  /**
   * Unarchive a page.
   *
   * @param pageId The ID of the page to unarchive
   * @return The unarchived page
   */
  public Page restore(String pageId) {
    validatePageId(pageId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(PAGE_ID, pageId);
    Page body = new Page();
    body.setArchived(false);
    body.setInTrash(false);

    return transport.call("PATCH", "/pages/{page_id}", null, pathParams, body, Page.class);
  }

  /**
   * Retrieve a specific page property.
   *
   * @param pageId The ID of the page
   * @param propertyId The ID of the property to retrieve
   * @return The property object
   */
  public PageProperty retrieveProperty(String pageId, String propertyId) {
    validatePageId(pageId);
    validatePropertyId(propertyId);

    Map<String, String> pathParams =
            ApiRequestUtil.createPathParams(
                    PAGE_ID, pageId, PROPERTY_ID, URLDecoder.decode(propertyId, StandardCharsets.UTF_8));

    return transport.call(
            "GET",
            "/pages/{page_id}/properties/{property_id}",
            null,
            pathParams,
            null,
            PageProperty.class);
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

    Map<String, String> pathParams =
            ApiRequestUtil.createPathParams(
                    PAGE_ID, pageId, PROPERTY_ID, URLDecoder.decode(propertyId, StandardCharsets.UTF_8));
    Map<String, String[]> queryParams = ApiRequestUtil.createQueryParams(startCursor, pageSize);

    return transport.call(
            "GET",
            "/pages/{page_id}/properties/{property_id}",
            queryParams,
            pathParams,
            null,
            PageProperty.class);
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
