package io.kristixlab.notion.api;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.IntegrationTest;
import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchRequest;
import io.kristixlab.notion.api.model.search.SearchResponse;
import io.kristixlab.notion.api.model.search.SearchSort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchIntegrationExample extends IntegrationTest {
  private static SearchApi searchApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    searchApi = new SearchApi(getTransport());
  }

  @Test
  void searchAll() throws Exception {
    SearchResponse results = searchApi.getAll();
    saveToFile(results, "search-all-response.json");
  }

  @Test
  void searchWithQuery() throws Exception {
    SearchResponse results = searchApi.search("Testing search page");
    saveToFile(results, "search-query-response.json");
  }

  @Test
  void searchPagesOnly() throws Exception {
    SearchResponse results = searchApi.searchPages("notes");
    saveToFile(results, "search-pages-only-response.json");
  }

  @Test
  void searchDataSourcesOnly() throws Exception {
    SearchResponse results = searchApi.searchDataSources("tasks");
    saveToFile(results, "search-databases-only-response.json");
  }

  @Test
  void searchWithSorting() throws Exception {
    SearchResponse results =
        searchApi.search("project", SearchFilter.pages(), SearchSort.descending());
    saveToFile(results, "search-sorted-response.json");
  }

  @Test
  void searchWithCompleteRequest() throws Exception {
    SearchRequest request = new SearchRequest();
    request.setQuery("notes");
    request.setFilter(SearchFilter.dataSources());
    request.setSort(SearchSort.descending());
    request.setPageSize(3);
    saveToFile(request, "search-rq.json");

    SearchResponse results = searchApi.search(request);
    saveToFile(results, "search-rs.json");
  }
}
