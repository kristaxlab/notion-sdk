package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.TransportStub;
import io.kristixlab.notion.api.model.search.SearchFilter;
import io.kristixlab.notion.api.model.search.SearchQuery;
import io.kristixlab.notion.api.model.search.SearchSort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SearchEndpointImplTest {

  private TransportStub transport;
  private SearchEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    transport = new TransportStub();
    endpoint = new SearchEndpointImpl(transport);
  }

  @Test
  void search() {
    endpoint.search("notion");

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    assertEquals("notion", ((SearchQuery) transport.getLastBody()).getQuery());
  }

  @Test
  void search_withFilter() {
    SearchFilter filter = SearchFilter.pages();

    endpoint.search("notion", filter);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
    assertEquals("notion", body.getQuery());
    assertEquals(filter, body.getFilter());
  }

  @Test
  void search_withSort() {
    SearchSort sort = SearchSort.descending();

    endpoint.search("notion", SearchFilter.pages(), sort);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
    assertEquals(sort, body.getSort());
  }

  @Test
  void search_withPaginationInBody() {
    endpoint.search("notion", 25, "cursor-abc");

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
    assertEquals(25, body.getPageSize());
    assertEquals("cursor-abc", body.getStartCursor());
  }

  @Test
  void searchPages() {
    endpoint.searchPages("notion");

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
    assertEquals("notion", body.getQuery());
    assertEquals("page", body.getFilter().getValue());
  }

  @Test
  void searchDataSources() {
    endpoint.searchDataSources("notion");

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
    assertEquals("notion", body.getQuery());
    assertEquals("data_source", body.getFilter().getValue());
  }

  @Test
  void getAll() {
    endpoint.getAll();

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
    assertNull(body.getQuery());
    assertNull(body.getFilter());
  }

  @Test
  void getAll_withPagination() {
    endpoint.getAll(50, "cursor-xyz");

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/search", transport.getLastUrlInfo().getUrl());
    SearchQuery body = (SearchQuery) transport.getLastBody();
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
