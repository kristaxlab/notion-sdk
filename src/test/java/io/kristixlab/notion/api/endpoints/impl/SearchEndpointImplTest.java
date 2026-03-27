package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.ApiClientStub;
import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchQuery;
import io.kristixlab.notion.api.model.search.SearchSort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SearchEndpointImplTest {

  private ApiClientStub client;
  private SearchEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new SearchEndpointImpl(client);
  }

  @Test
  void search() {
    endpoint.search("notion");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    assertEquals("notion", ((SearchQuery) client.getLastBody()).getQuery());
  }

  @Test
  void search_withFilter() {
    SearchFilter filter = SearchFilter.pages();

    endpoint.search("notion", filter);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertEquals("notion", body.getQuery());
    assertEquals(filter, body.getFilter());
  }

  @Test
  void search_withSort() {
    SearchSort sort = SearchSort.descending();

    endpoint.search("notion", SearchFilter.pages(), sort);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertEquals(sort, body.getSort());
  }

  @Test
  void search_withPaginationInBody() {
    endpoint.search("notion", 25, "cursor-abc");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertEquals(25, body.getPageSize());
    assertEquals("cursor-abc", body.getStartCursor());
  }

  @Test
  void searchPages() {
    endpoint.searchPages("notion");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertEquals("notion", body.getQuery());
    assertEquals("page", body.getFilter().getValue());
  }

  @Test
  void searchDataSources() {
    endpoint.searchDataSources("notion");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertEquals("notion", body.getQuery());
    assertEquals("data_source", body.getFilter().getValue());
  }

  @Test
  void getAll() {
    endpoint.getAll();

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertNull(body.getQuery());
    assertNull(body.getFilter());
  }

  @Test
  void getAll_withPagination() {
    endpoint.getAll(50, "cursor-xyz");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) client.getLastBody();
    assertEquals(50, body.getPageSize());
    assertEquals("cursor-xyz", body.getStartCursor());
  }

  @Test
  void search_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.search((SearchQuery) null));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, 101, Integer.MAX_VALUE})
  void search_rejectsPageSizeOutOfRange(int pageSize) {
    SearchQuery request = new SearchQuery();
    request.setPageSize(pageSize);

    assertThrows(IllegalArgumentException.class, () -> endpoint.search(request));
  }
}
