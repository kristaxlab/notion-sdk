package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchRequest;
import io.kristixlab.notion.api.model.search.SearchResponse;
import io.kristixlab.notion.api.model.search.SearchSort;

/**
 * Interface defining operations for Notion Search.
 *
 * @see <a href="https://developers.notion.com/reference/search">Notion Search API</a>
 */
public interface SearchEndpoint {
  SearchResponse search(SearchRequest request);

  SearchResponse search(String query);

  SearchResponse search(String query, SearchFilter filter);

  SearchResponse search(String query, SearchFilter filter, SearchSort sort);

  SearchResponse search(String query, Integer pageSize, String startCursor);

  SearchResponse searchPages(String query);

  SearchResponse searchDataSources(String query);

  SearchResponse getAll();

  SearchResponse getAll(Integer pageSize, String startCursor);
}
