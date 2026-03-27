package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.SearchEndpoint;
import io.kristixlab.notion.api.http.base.client.ApiClient;
import io.kristixlab.notion.api.http.base.request.ApiPath;
import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchQuery;
import io.kristixlab.notion.api.model.search.SearchResult;
import io.kristixlab.notion.api.model.search.SearchSort;

/**
 * API for searching through pages and data sources in a workspace. Provides methods to search with
 * various filters and sorting options.
 */
public class SearchEndpointImpl implements SearchEndpoint {

  private final ApiClient client;

  public SearchEndpointImpl(ApiClient client) {
    this.client = client;
  }

  // ...existing code...
  public SearchResult search(SearchQuery request) {
    validateRequest(request);
    return client.call("POST", ApiPath.from("/search"), request, SearchResult.class);
  }

  /**
   * Simple search with just a query string.
   *
   * @param query The text to search for in page and data source titles
   * @return SearchResponse containing results
   */
  public SearchResult search(String query) {
    SearchQuery request = new SearchQuery();
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
  public SearchResult search(String query, SearchFilter filter) {
    SearchQuery request = new SearchQuery();
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
  public SearchResult search(String query, SearchFilter filter, SearchSort sort) {
    SearchQuery request = new SearchQuery();
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
  public SearchResult search(String query, Integer pageSize, String startCursor) {
    SearchQuery request = new SearchQuery();
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
  public SearchResult searchPages(String query) {
    return search(query, SearchFilter.pages());
  }

  /**
   * Search only data sources in the workspace (API version 2025-09-03+).
   *
   * @param query The text to search for in data source titles
   * @return SearchResponse containing only data source results
   */
  public SearchResult searchDataSources(String query) {
    return search(query, SearchFilter.dataSources());
  }

  /**
   * Get all pages and data sources (no query filter).
   *
   * @return SearchResponse containing all accessible pages and data sources
   */
  public SearchResult getAll() {
    SearchQuery request = new SearchQuery();
    return search(request);
  }

  /**
   * Get all pages and data sources with pagination.
   *
   * @param pageSize Number of results per page (max 100)
   * @param startCursor Cursor for pagination
   * @return SearchResponse with paginated results
   */
  public SearchResult getAll(Integer pageSize, String startCursor) {
    SearchQuery request = new SearchQuery();
    request.setPageSize(pageSize);
    request.setStartCursor(startCursor);
    return search(request);
  }

  private void validateRequest(SearchQuery request) {
    if (request == null) {
      throw new IllegalArgumentException("Search request cannot be null");
    }

    if (request.getPageSize() != null
        && (request.getPageSize() < 1 || request.getPageSize() > 100)) {
      throw new IllegalArgumentException("Page size must be between 1 and 100");
    }
  }
}
