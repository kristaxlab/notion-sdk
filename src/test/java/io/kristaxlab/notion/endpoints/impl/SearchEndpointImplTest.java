package io.kristaxlab.notion.endpoints.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.http.base.client.ApiClientStub;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.model.search.SearchFilter;
import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import io.kristaxlab.notion.model.search.SearchSort;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayName("Search endpoint behaviors")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SearchEndpointImplTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  private ApiClientStub client;
  private SearchEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new SearchEndpointImpl(client);
  }

  @Test
  @DisplayName("posts empty search body to /search")
  void search_emptyBody() {
    endpoint.search();

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    assertInstanceOf(SearchParams.class, client.getLastBody());
    SearchParams body = (SearchParams) client.getLastBody();
    assertNull(body.getQuery());
    assertNull(body.getFilter());
    assertNull(body.getSort());
    assertNull(body.getStartCursor());
    assertNull(body.getPageSize());
  }

  @Test
  @DisplayName("posts query-only search body")
  void search_queryString() {
    endpoint.search("meeting notes");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    SearchParams body = (SearchParams) client.getLastBody();
    assertEquals("meeting notes", body.getQuery());
  }

  @Test
  @DisplayName("rejects null query string")
  void search_rejectsNullQuery() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.search((String) null));
  }

  @Test
  @DisplayName("posts full search params")
  void search_params() {
    SearchParams request =
        SearchParams.builder()
            .query("tasks")
            .filter(SearchFilter.pages().withInTrash(false))
            .sort(SearchSort.byLastEditedTimeDescending())
            .startCursor("cursor-1")
            .pageSize(25)
            .build();

    endpoint.search(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/search", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  @DisplayName("rejects null search params")
  void search_rejectsNullParams() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.search((SearchParams) null));
  }

  @Test
  @DisplayName("builds params from consumer")
  void search_consumer() {
    endpoint.search(
        b ->
            b.query("notes")
                .filter(SearchFilter.dataSources())
                .sort(SearchSort.byRelevance())
                .pageSize(10));

    SearchParams body = (SearchParams) client.getLastBody();
    assertEquals("notes", body.getQuery());
    assertEquals("object", body.getFilter().getProperty());
    assertEquals("data_source", body.getFilter().getValue());
    assertEquals("relevance", body.getSort().getProperty());
    assertEquals(10, body.getPageSize());
  }

  @Test
  @DisplayName("rejects null consumer")
  void search_rejectsNullConsumer() {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.search((Consumer<SearchParams.Builder>) null));
  }

  @Test
  @DisplayName("posts pagination-only body")
  void search_paginationOnly() {
    endpoint.search("cursor-9", 50);

    SearchParams body = (SearchParams) client.getLastBody();
    assertEquals("cursor-9", body.getStartCursor());
    assertEquals(50, body.getPageSize());
    assertNull(body.getQuery());
  }

  @Test
  @DisplayName("stamps cursor and page size onto existing params")
  void search_paramsWithPagination() {
    SearchParams request =
        SearchParams.builder().query("x").filter(SearchFilter.inTrash(true)).build();

    endpoint.search(request, "cursor-2", 10);

    assertSame(request, client.getLastBody());
    assertEquals("cursor-2", request.getStartCursor());
    assertEquals(10, request.getPageSize());
    assertEquals(Boolean.TRUE, request.getFilter().getInTrash());
  }

  @Test
  @DisplayName("serializes search params with snake_case and omits nulls")
  void serialize_searchParamsUsesSnakeCaseAndOmitsNulls() {
    SearchParams params =
        SearchParams.builder()
            .query("meeting")
            .filter(SearchFilter.pages().withInTrash(true))
            .sort(SearchSort.byLastEditedTimeAscending())
            .pageSize(20)
            .build();

    String json = JSON.toJson(params);

    assertTrue(json.contains("\"query\""));
    assertTrue(json.contains("\"meeting\""));
    assertTrue(json.contains("\"filter\""));
    assertTrue(json.contains("\"property\""));
    assertTrue(json.contains("\"object\""));
    assertTrue(json.contains("\"value\""));
    assertTrue(json.contains("\"page\""));
    assertTrue(json.contains("\"in_trash\""));
    assertTrue(json.contains("\"sort\""));
    assertTrue(json.contains("\"timestamp\""));
    assertTrue(json.contains("\"last_edited_time\""));
    assertTrue(json.contains("\"direction\""));
    assertTrue(json.contains("\"ascending\""));
    assertTrue(json.contains("\"page_size\""));
    assertTrue(json.contains("start_cursor") == false);
  }

  @Test
  @DisplayName("deserializes mixed page and data source search list with result completeness")
  void deserialize_mixedSearchList() throws IOException {
    SearchList list = JSON.toObject(fixture("search-list-mixed.json"), SearchList.class);

    assertEquals("list", list.getObject());
    assertEquals("page_or_data_source", list.getType());
    assertEquals("cursor-2", list.getNextCursor());
    assertEquals(Boolean.TRUE, list.getHasMore());
    assertEquals(2, list.getResults().size());
    assertInstanceOf(io.kristaxlab.notion.model.page.Page.class, list.getResults().get(0));
    assertInstanceOf(
        io.kristaxlab.notion.model.datasource.DataSource.class, list.getResults().get(1));
    assertEquals("page-1111-2222-3333-444444444444", list.getResults().get(0).getId());
    assertEquals("ds-aaaa-bbbb-cccc-dddddddddddd", list.getResults().get(1).getId());
    assertEquals("incomplete", list.getRequestStatus().getType());
    assertEquals("query_result_limit_reached", list.getRequestStatus().getIncompleteReason());
  }

  @Test
  @DisplayName("deserializes request status on query list")
  void deserialize_queryListRequestStatus() throws IOException {
    io.kristaxlab.notion.model.datasource.QueryList list =
        JSON.toObject(
            fixture("query-list-request-status.json"),
            io.kristaxlab.notion.model.datasource.QueryList.class);

    assertEquals("incomplete", list.getRequestStatus().getType());
    assertEquals("query_result_limit_reached", list.getRequestStatus().getIncompleteReason());
    assertTrue(list.getResults() == null || list.getResults().isEmpty());
  }

  private static String fixture(String name) throws IOException {
    try (var stream = SearchEndpointImplTest.class.getResourceAsStream("/json/" + name)) {
      assertNotNull(stream, "Missing fixture: " + name);
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
