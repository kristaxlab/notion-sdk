package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.transport.ApiTransport;
import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchRequest;
import io.kristixlab.notion.api.model.search.SearchResponse;
import io.kristixlab.notion.api.model.search.SearchSort;

/**
 * API for searching through pages and data sources in a workspace. Provides methods to search with
 * various filters and sorting options.
 */
public class SearchApi {

  private final ApiTransport transport;

  public SearchApi(NotionApiTransport transport) {
    this.transport = transport;
  }

  /**
   * Search through pages and data sources in the workspace.
   *
   * @param request The search request with query, filters, and pagination options
   * @return SearchResponse containing results and pagination info
   */
  public SearchResponse search(SearchRequest request) {
    validateRequest(request);
    return transport.call("POST", "/search", null, null, request, SearchResponse.class);
  }

  /**
   * Simple search with just a query string.
   *
   * @param query The text to search for in page and data source titles
   * @return SearchResponse containing results
   */
  public SearchResponse search(String query) {
    SearchRequest request = new SearchRequest();
    request.setQuery(query);
    return search(request);
  }

  /**
   * Search with query and filter (pages or data sources only).
   *
   * @param query The text to search for
   * @param filter Filter to limit results to pages or data sources
   * @return SearchResponse containing filtered results
   */
  public SearchResponse search(String query, SearchFilter filter) {
    SearchRequest request = new SearchRequest();
    request.setQuery(query);
    request.setFilter(filter);
    return search(request);
  }

  /**
   * Search with query, filter, and sorting.
   *
   * @param query The text to search for
   * @param filter Filter to limit results to pages or data sources
   * @param sort Sorting criteria for results
   * @return SearchResponse containing filtered and sorted results
   */
  public SearchResponse search(String query, SearchFilter filter, SearchSort sort) {
    SearchRequest request = new SearchRequest();
    request.setQuery(query);
    request.setFilter(filter);
    request.setSort(sort);
    return search(request);
  }

  /**
   * Search with pagination support.
   *
   * @param query The text to search for
   * @param pageSize Number of results per page (max 100)
   * @param startCursor Cursor for pagination
   * @return SearchResponse with paginated results
   */
  public SearchResponse search(String query, Integer pageSize, String startCursor) {
    SearchRequest request = new SearchRequest();
    request.setQuery(query);
    request.setPageSize(pageSize);
    request.setStartCursor(startCursor);
    return search(request);
  }

  /**
   * Search only pages in the workspace.
   *
   * @param query The text to search for in page titles
   * @return SearchResponse containing only page results
   */
  public SearchResponse searchPages(String query) {
    return search(query, SearchFilter.pages());
  }

  /**
   * Search only data sources in the workspace (API version 2025-09-03+).
   *
   * @param query The text to search for in data source titles
   * @return SearchResponse containing only data source results
   */
  public SearchResponse searchDataSources(String query) {
    return search(query, SearchFilter.dataSources());
  }

  /**
   * Get all pages and data sources (no query filter).
   *
   * @return SearchResponse containing all accessible pages and data sources
   */
  public SearchResponse getAll() {
    SearchRequest request = new SearchRequest();
    return search(request);
  }

  /**
   * Get all pages and data sources with pagination.
   *
   * @param pageSize Number of results per page (max 100)
   * @param startCursor Cursor for pagination
   * @return SearchResponse with paginated results
   */
  public SearchResponse getAll(Integer pageSize, String startCursor) {
    SearchRequest request = new SearchRequest();
    request.setPageSize(pageSize);
    request.setStartCursor(startCursor);
    return search(request);
  }

  private void validateRequest(SearchRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Search request cannot be null");
    }

    if (request.getPageSize() != null
        && (request.getPageSize() < 1 || request.getPageSize() > 100)) {
      throw new IllegalArgumentException("Page size must be between 1 and 100");
    }
  }
}
