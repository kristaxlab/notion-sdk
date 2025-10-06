package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchQuery;
import io.kristixlab.notion.api.model.search.SearchResult;
import io.kristixlab.notion.api.model.search.SearchSort;

/**
 * Interface defining operations for Notion Search.
 *
 * @see <a href="https://developers.notion.com/reference/search">Notion Search API</a>
 */
public interface SearchEndpoint {
  SearchResult search(SearchQuery request);

  SearchResult search(String query);

  SearchResult search(String query, SearchFilter filter);

  SearchResult search(String query, SearchFilter filter, SearchSort sort);

  SearchResult search(String query, Integer pageSize, String startCursor);

  SearchResult searchPages(String query);

  SearchResult searchDataSources(String query);

  SearchResult getAll();

  SearchResult getAll(Integer pageSize, String startCursor);
}
