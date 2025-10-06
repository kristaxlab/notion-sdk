package io.kristixlab.notion.api.examples;

import io.kristixlab.notion.api.endpoints.impl.SearchEndpointImpl;
import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchQuery;
import io.kristixlab.notion.api.model.search.SearchResult;
import io.kristixlab.notion.api.model.search.SearchSort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchIntegrationExample extends IntegrationTest {
  private static SearchEndpointImpl searchApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    searchApi = new SearchEndpointImpl(getTransport());
  }

  @Test
  void searchAll() throws Exception {
    SearchResult results = searchApi.getAll();
    saveToFile(results, "search-all-response.json");
  }

  @Test
  void searchWithQuery() throws Exception {
    SearchResult results = searchApi.search("Testing search page");
    saveToFile(results, "search-query-response.json");
  }

  @Test
  void searchPagesOnly() throws Exception {
    SearchResult results = searchApi.searchPages("notes");
    saveToFile(results, "search-pages-only-response.json");
  }

  @Test
  void searchDataSourcesOnly() throws Exception {
    SearchResult results = searchApi.searchDataSources("tasks");
    saveToFile(results, "search-databases-only-response.json");
  }

  @Test
  void searchWithSorting() throws Exception {
    SearchResult results =
        searchApi.search("project", SearchFilter.pages(), SearchSort.descending());
    saveToFile(results, "search-sorted-response.json");
  }

  @Test
  void searchWithCompleteRequest() throws Exception {
    SearchQuery request = new SearchQuery();
    request.setQuery("notes");
    request.setFilter(SearchFilter.dataSources());
    request.setSort(SearchSort.descending());
    request.setPageSize(3);
    saveToFile(request, "search-rq.json");

    SearchResult results = searchApi.search(request);
    saveToFile(results, "search-rs.json");
  }
}
