package io.kristixlab.notion.api.model.search;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import io.kristixlab.notion.api.model.common.NotionObject;
import io.kristixlab.notion.api.model.pages.Page;
import org.junit.jupiter.api.Test;

public class SearchDeserializationTest extends BaseTest {

  @Test
  void testSearchAllResponse() throws Exception {
    SearchResponse searchResponse = loadFromFile("search/search-all.json", SearchResponse.class);

    // Validate basic response structure
    assertNotNull(searchResponse);
    assertEquals("list", searchResponse.getObject());
    assertEquals("page_or_database", searchResponse.getType());
    assertNotNull(searchResponse.getResults());
    assertFalse(searchResponse.getResults().isEmpty());

    // Test first result (should be a page)
    NotionObject firstResult = searchResponse.getResults().get(0);
    assertNotNull(firstResult);
    assertEquals("page", firstResult.getObject());
    assertEquals("246c5b96-8ec4-80a6-b8d6-f61a77fd8fd6", firstResult.getId());
    assertEquals("2025-08-05T10:32:00.000Z", firstResult.getCreatedTime());
    assertEquals("2025-08-20T13:49:00.000Z", firstResult.getLastEditedTime());
    assertFalse(firstResult.getArchived());
    assertFalse(firstResult.getInTrash());

    // Verify created_by and last_edited_by users
    assertNotNull(firstResult.getCreatedBy());
    assertEquals("user", firstResult.getCreatedBy().getObject());
    assertEquals("44444444-4444-43b3-8288-444444444444", firstResult.getCreatedBy().getId());

    assertNotNull(firstResult.getLastEditedBy());
    assertEquals("user", firstResult.getLastEditedBy().getObject());
    assertEquals("44444444-4444-43b3-8288-444444444444", firstResult.getLastEditedBy().getId());

    // Verify parent structure
    assertNotNull(firstResult.getParent());
    assertEquals("page_id", firstResult.getParent().getType());
    assertEquals("255c5b96-8ec4-80d6-9467-e07a18d9ab15", firstResult.getParent().getPageId());

    // If the result is a Page, test page-specific properties
    if (firstResult instanceof Page) {
      Page page = (Page) firstResult;
      assertNotNull(page.getProperties());
      assertTrue(page.getProperties().containsKey("title"));
      assertEquals(
          "https://www.notion.so/Testing-Comments-246c5b968ec480a6b8d6f61a77fd8fd6", page.getUrl());
      assertNull(page.getPublicUrl());
      assertNull(page.getCover());
      assertNull(page.getIcon());
    }
  }

  @Test
  void testSearchEmptyResponse() throws Exception {
    SearchResponse emptyResponse =
        loadFromFile("search/search-empty-response.json", SearchResponse.class);

    // Validate basic response structure
    assertNotNull(emptyResponse);
    assertEquals("list", emptyResponse.getObject());
    assertEquals("f88fda5f-2a4c-4bd2-9e36-a3b8586b8b64", emptyResponse.getRequestId());

    // Validate empty results
    assertNotNull(emptyResponse.getResults());
    assertTrue(emptyResponse.getResults().isEmpty());

    // Validate pagination fields
    assertNull(emptyResponse.getNextCursor());
    assertNotNull(emptyResponse.hasMore());
    assertFalse(emptyResponse.hasMore());

    // Validate search-specific fields
    assertEquals("page_or_database", emptyResponse.getType());
  }

  @Test
  void testSearchResponsePagination() throws Exception {
    SearchResponse paginatedResponse =
        loadFromFile("search/search-paginated.json", SearchResponse.class);

    // Validate basic response structure
    assertNotNull(paginatedResponse);
    assertEquals("list", paginatedResponse.getObject());
    assertEquals("f88fda5f-2a4c-4bd2-9e36-a3b8586b8b64", paginatedResponse.getRequestId());

    // Test pagination fields - this response has more results available
    assertNotNull(paginatedResponse.getNextCursor());
    assertEquals(
        "eyJsYXN0X2VkaXRlZF90aW1lIjoiMjAyNS0wOC0yMFQxNDoyMjowMC4wMDBaIiwibGFzdF9lZGl0ZWRfaWQiOiIyNGNjNWI5Ni04ZWM0LTgwMGEtYTgwOS1jN2Y2NTA4ZjQ1ZjIifQ==",
        paginatedResponse.getNextCursor());
    assertNotNull(paginatedResponse.hasMore());
    assertTrue(paginatedResponse.hasMore());

    // Validate search-specific fields
    assertEquals("page_or_database", paginatedResponse.getType());

    // Test results content - should have exactly 2 results (1 page, 1 database)
    assertNotNull(paginatedResponse.getResults());
    assertEquals(2, paginatedResponse.getResults().size());

    // Test first result (page)
    NotionObject firstResult = paginatedResponse.getResults().get(0);
    assertEquals("page", firstResult.getObject());
    assertEquals("246c5b96-8ec4-80a6-b8d6-f61a77fd8fd6", firstResult.getId());
    assertEquals("2025-08-05T10:32:00.000Z", firstResult.getCreatedTime());
    assertEquals("2025-08-20T13:49:00.000Z", firstResult.getLastEditedTime());

    // Test second result (database)
    NotionObject secondResult = paginatedResponse.getResults().get(1);
    assertEquals("database", secondResult.getObject());
    assertEquals("24cc5b96-8ec4-800a-a809-c7f6508f45f2", secondResult.getId());
    assertEquals("2025-08-11T09:15:00.000Z", secondResult.getCreatedTime());
    assertEquals("2025-08-20T14:22:00.000Z", secondResult.getLastEditedTime());
  }

  @Test
  void testSearchResponseWithMultipleResultTypes() throws Exception {
    SearchResponse searchResponse = loadFromFile("search/search-all.json", SearchResponse.class);

    assertNotNull(searchResponse.getResults());

    // Verify we can iterate through results regardless of their specific type
    for (NotionObject result : searchResponse.getResults()) {
      assertNotNull(result);
      assertNotNull(result.getObject());
      assertNotNull(result.getId());

      // Each result should be either a page or database
      assertTrue(result.getObject().equals("page") || result.getObject().equals("database"));

      // Common fields should be present
      assertNotNull(result.getCreatedTime());
      assertNotNull(result.getLastEditedTime());
      assertNotNull(result.getArchived());
      assertNotNull(result.getInTrash());
    }
  }
}
