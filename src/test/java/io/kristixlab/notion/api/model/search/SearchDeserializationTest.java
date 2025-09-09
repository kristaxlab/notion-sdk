package io.kristixlab.notion.api.model.search;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import org.junit.jupiter.api.Test;

public class SearchDeserializationTest extends BaseTest {

  @Test
  void testSearchPagesRequest() throws Exception {
    SearchRequest request = loadFromFile("search/search-rq-pages.json", SearchRequest.class);

    assertEquals("notes", request.getQuery());
    assertNotNull(request.getFilter());
    assertEquals("page", request.getFilter().getValue());
    assertNotNull(request.getStartCursor());
    assertEquals("descending", request.getSort().getDirection());
    assertEquals("last_edited_time", request.getSort().getTimestamp());
  }

  @Test
  void testSearchDataSourcesRequest() throws Exception {
    SearchRequest request = loadFromFile("search/search-rq-data-sources.json", SearchRequest.class);

    assertEquals("some query string", request.getQuery());
    assertNotNull(request.getFilter());
    assertEquals("data_source", request.getFilter().getValue());
    assertNull(request.getStartCursor());
    assertEquals("descending", request.getSort().getDirection());
    assertEquals("last_edited_time", request.getSort().getTimestamp());
  }

  @Test
  void testSearchResponse() throws Exception {
    SearchResponse searchResponse =
        loadFromFile("search/search-rs-paginated.json", SearchResponse.class);

    assertNotNull(searchResponse);
    assertEquals("list", searchResponse.getObject());

    assertEquals("page_or_data_source", searchResponse.getType());
    assertNotNull(searchResponse.getPageOrDataSource());

    assertNotNull(searchResponse.getResults());
    assertEquals(3, searchResponse.getResults().size());

    assertTrue(searchResponse.hasMore());
    assertNotNull(searchResponse.getNextCursor());
  }

  @Test
  void testEmptyResponse() throws Exception {
    SearchResponse searchResponse =
        loadFromFile("search/search-rs-empty.json", SearchResponse.class);

    assertEquals("list", searchResponse.getObject());
    assertEquals("page_or_data_source", searchResponse.getType());
    assertNotNull(searchResponse.getPageOrDataSource());

    assertNull(searchResponse.getNextCursor());
    assertFalse(searchResponse.hasMore());

    assertNotNull(searchResponse.getResults());
    assertEquals(0, searchResponse.getResults().size());
  }
}
