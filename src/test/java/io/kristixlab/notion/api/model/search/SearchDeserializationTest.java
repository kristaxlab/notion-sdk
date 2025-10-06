package io.kristixlab.notion.api.model.search;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import org.junit.jupiter.api.Test;

public class SearchDeserializationTest extends BaseTest {

  @Test
  void testSearchPagesRequest() throws Exception {
    SearchQuery request = loadFromFile("search/search-rq-pages.json", SearchQuery.class);

    assertEquals("notes", request.getQuery());
    assertNotNull(request.getFilter());
    assertEquals("page", request.getFilter().getValue());
    assertNotNull(request.getStartCursor());
    assertEquals("descending", request.getSort().getDirection());
    assertEquals("last_edited_time", request.getSort().getTimestamp());
  }

  @Test
  void testSearchDataSourcesRequest() throws Exception {
    SearchQuery request = loadFromFile("search/search-rq-data-sources.json", SearchQuery.class);

    assertEquals("some query string", request.getQuery());
    assertNotNull(request.getFilter());
    assertEquals("data_source", request.getFilter().getValue());
    assertNull(request.getStartCursor());
    assertEquals("descending", request.getSort().getDirection());
    assertEquals("last_edited_time", request.getSort().getTimestamp());
  }

  @Test
  void testSearchResponse() throws Exception {
    SearchResult searchResult = loadFromFile("search/search-rs-paginated.json", SearchResult.class);

    assertNotNull(searchResult);
    assertEquals("list", searchResult.getObject());

    assertEquals("page_or_data_source", searchResult.getType());
    assertNotNull(searchResult.getPageOrDataSource());

    assertNotNull(searchResult.getResults());
    assertEquals(3, searchResult.getResults().size());

    assertTrue(searchResult.hasMore());
    assertNotNull(searchResult.getNextCursor());
  }

  @Test
  void testEmptyResponse() throws Exception {
    SearchResult searchResult = loadFromFile("search/search-rs-empty.json", SearchResult.class);

    assertEquals("list", searchResult.getObject());
    assertEquals("page_or_data_source", searchResult.getType());
    assertNotNull(searchResult.getPageOrDataSource());

    assertNull(searchResult.getNextCursor());
    assertFalse(searchResult.hasMore());

    assertNotNull(searchResult.getResults());
    assertEquals(0, searchResult.getResults().size());
  }
}
