package io.kristixlab.notion.api.http.transport.rq;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class URLInfoTest {

  @Test
  void testFrom() {
    URLInfo urlInfo = URLInfo.from("/pages");

    assertEquals("/pages", urlInfo.getUrl());
    assertTrue(urlInfo.getPathParams().isEmpty());
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  @Test
  void testBuilder() {
    URLInfo urlInfo = URLInfo.builder("/pages").build();

    assertTrue(urlInfo.getPathParams().isEmpty());
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  // ── query params ─────────────────────────────────────────────────────────────

  @Test
  void builder_withBothPaginationParams() {
    URLInfo urlInfo = URLInfo.builder("/pages", "cursor-abc", 25).build();

    assertEquals("/pages", urlInfo.getUrl());
    assertEquals(List.of("cursor-abc"), urlInfo.getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), urlInfo.getQueryParams().get("page_size"));
  }

  @Test
  void builder_withStartCursorOnly() {
    URLInfo urlInfo = URLInfo.builder("/pages", "cursor-abc", null).build();

    assertEquals(List.of("cursor-abc"), urlInfo.getQueryParams().get("start_cursor"));
    assertFalse(urlInfo.getQueryParams().containsKey("page_size"));
  }

  @Test
  void builder_withPageSizeOnly() {
    URLInfo urlInfo = URLInfo.builder("/pages", null, 50).build();

    assertEquals(List.of("50"), urlInfo.getQueryParams().get("page_size"));
    assertFalse(urlInfo.getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void builder_withNullPaginationParams() {
    URLInfo urlInfo = URLInfo.builder("/pages", null, null).build();

    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  @Test
  void builder_withPathParamAndQueryParamAndPagination() {
    URLInfo urlInfo =
        URLInfo.builder("/blocks/{block_id}/children", "cursor-abc", 10)
            .pathParam("block_id", "block-id-1")
            .queryParam("filter", "active")
            .build();

    assertEquals("/blocks/{block_id}/children", urlInfo.getUrl());
    assertEquals("block-id-1", urlInfo.getPathParams().get("block_id"));
    assertEquals(List.of("active"), urlInfo.getQueryParams().get("filter"));
    assertEquals(List.of("cursor-abc"), urlInfo.getQueryParams().get("start_cursor"));
    assertEquals(List.of("10"), urlInfo.getQueryParams().get("page_size"));
  }

  // ── pathParam ─────────────────────────────────────────────────────────────

  @Test
  void pathParam_storesValue() {
    URLInfo urlInfo = URLInfo.builder("/pages/{page_id}").pathParam("page_id", "abc-123").build();

    assertEquals("abc-123", urlInfo.getPathParams().get("page_id"));
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  @Test
  void pathParams_storesAllEntries() {
    URLInfo urlInfo =
        URLInfo.builder("/a/{x}/b/{y}").pathParams(Map.of("x", "val-x", "y", "val-y")).build();

    assertEquals("val-x", urlInfo.getPathParams().get("x"));
    assertEquals("val-y", urlInfo.getPathParams().get("y"));
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  // ── queryParam ────────────────────────────────────────────────────────────

  @Test
  void queryParam_storesStringValueAsSingleElementList() {
    URLInfo urlInfo = URLInfo.builder("/pages").queryParam("filter", "active").build();

    assertEquals(List.of("active"), urlInfo.getQueryParams().get("filter"));
  }

  @Test
  void queryParam_withObject_usesToString() {
    URLInfo urlInfo = URLInfo.builder("/pages").queryParam("page_size", 42).build();

    assertEquals(List.of("42"), urlInfo.getQueryParams().get("page_size"));
  }

  @Test
  void queryParam_withList_storesAllValues() {
    URLInfo urlInfo =
        URLInfo.builder("/pages").queryParam("ids", List.of("id-1", "id-2", "id-3")).build();

    assertEquals(List.of("id-1", "id-2", "id-3"), urlInfo.getQueryParams().get("ids"));
  }

  @Test
  void queryParam_withSameKey_mergesValues() {
    URLInfo urlInfo =
        URLInfo.builder("/pages").queryParam("tag", "a").queryParam("tag", "b").build();

    assertEquals(List.of("a", "b"), urlInfo.getQueryParams().get("tag"));
  }

  @Test
  void queryParam_withSameKey_mergesListValues() {
    URLInfo urlInfo =
        URLInfo.builder("/pages")
            .queryParam("tag", List.of("a", "b"))
            .queryParam("tag", List.of("c"))
            .build();

    assertEquals(List.of("a", "b", "c"), urlInfo.getQueryParams().get("tag"));
  }

  // ── queryParams (map variants) ────────────────────────────────────────────

  @Test
  void queryParams_fromStringMap_storesEachValueAsSingleElementList() {
    URLInfo urlInfo = URLInfo.builder("/pages").queryParams(Map.of("foo", "1", "bar", "2")).build();

    assertEquals(List.of("1"), urlInfo.getQueryParams().get("foo"));
    assertEquals(List.of("2"), urlInfo.getQueryParams().get("bar"));
  }

  @Test
  void queryParamsArrays_convertsArraysToLists() {
    URLInfo urlInfo =
        URLInfo.builder("/pages")
            .queryParamsArrays(Map.of("ids", new String[] {"id-1", "id-2"}))
            .build();

    assertEquals(List.of("id-1", "id-2"), urlInfo.getQueryParams().get("ids"));
  }

  @Test
  void queryParamsLists_storesAllLists() {
    URLInfo urlInfo =
        URLInfo.builder("/pages")
            .queryParamsLists(Map.of("ids", List.of("id-1", "id-2"), "tags", List.of("x")))
            .build();

    assertEquals(List.of("id-1", "id-2"), urlInfo.getQueryParams().get("ids"));
    assertEquals(List.of("x"), urlInfo.getQueryParams().get("tags"));
  }
}
