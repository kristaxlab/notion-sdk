package io.kristixlab.notion.api.http.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ApiPathTest {

  @Test
  void testFrom() {
    ApiPath urlInfo = ApiPath.from("/pages");

    assertEquals("/pages", urlInfo.getUrl());
    assertTrue(urlInfo.getPathParams().isEmpty());
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  @Test
  void testBuilder() {
    ApiPath urlInfo = ApiPath.builder("/pages").build();

    assertTrue(urlInfo.getPathParams().isEmpty());
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  @Test
  void pathParam_storesValue() {
    ApiPath urlInfo = ApiPath.builder("/pages/{page_id}").pathParam("page_id", "abc-123").build();

    assertEquals("abc-123", urlInfo.getPathParams().get("page_id"));
    assertTrue(urlInfo.getQueryParams().isEmpty());
  }

  @Test
  void queryParam_storesStringValueAsSingleElementList() {
    ApiPath urlInfo = ApiPath.builder("/pages").queryParam("filter", "active").build();

    assertEquals(List.of("active"), urlInfo.getQueryParams().get("filter"));
  }

  @Test
  void queryParam_storesStringifiedNumber() {
    ApiPath urlInfo = ApiPath.builder("/pages").queryParam("page_size", String.valueOf(42)).build();

    assertEquals(List.of("42"), urlInfo.getQueryParams().get("page_size"));
  }

  @Test
  void queryParam_withList_storesAllValues() {
    ApiPath urlInfo =
        ApiPath.builder("/pages").queryParam("ids", List.of("id-1", "id-2", "id-3")).build();

    assertEquals(List.of("id-1", "id-2", "id-3"), urlInfo.getQueryParams().get("ids"));
  }

  @Test
  void queryParam_withSameKey_mergesValues() {
    ApiPath urlInfo =
        ApiPath.builder("/pages").queryParam("tag", "a").queryParam("tag", "b").build();

    assertEquals(List.of("a", "b"), urlInfo.getQueryParams().get("tag"));
  }

  @Test
  void queryParam_withSameKey_mergesListValues() {
    ApiPath urlInfo =
        ApiPath.builder("/pages")
            .queryParam("tag", List.of("a", "b"))
            .queryParam("tag", List.of("c"))
            .build();

    assertEquals(List.of("a", "b", "c"), urlInfo.getQueryParams().get("tag"));
  }

  @Test
  void resolve_relativeUrl_prependsBaseUrl() {
    ApiPath path = ApiPath.from("/pages");
    assertEquals("https://api.notion.com/v1/pages", path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_absoluteUrl_ignoresBaseUrl() {
    ApiPath path = ApiPath.from("https://other.host/upload");
    assertEquals("https://other.host/upload", path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_emptyPath_returnsBaseUrl() {
    ApiPath path = ApiPath.from("");
    assertEquals("https://api.notion.com/v1", path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_withPathParam_substitutesValue() {
    ApiPath path = ApiPath.builder("/pages/{page_id}").pathParam("page_id", "abc-123").build();
    assertEquals(
        "https://api.notion.com/v1/pages/abc-123", path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_withPathParamSpecialChars_encodesValue() {
    ApiPath path = ApiPath.builder("/pages/{page_id}").pathParam("page_id", "hello world").build();
    assertEquals(
        "https://api.notion.com/v1/pages/hello%20world",
        path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_withQueryParam_appendsQueryString() {
    ApiPath path = ApiPath.builder("/pages").queryParam("filter", "active").build();
    assertEquals(
        "https://api.notion.com/v1/pages?filter=active",
        path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_withMultipleQueryParams_appendsAll() {
    ApiPath path =
        ApiPath.builder("/pages").queryParam("filter", "active").queryParam("page_size", "10").build();
    String result = path.resolve("https://api.notion.com/v1");
    assertTrue(result.startsWith("https://api.notion.com/v1/pages?"));
    assertTrue(result.contains("filter=active"));
    assertTrue(result.contains("page_size=10"));
  }

  @Test
  void resolve_withMultiValueQueryParam_appendsAllValues() {
    ApiPath path = ApiPath.builder("/pages").queryParam("ids", List.of("id-1", "id-2")).build();
    assertEquals(
        "https://api.notion.com/v1/pages?ids=id-1&ids=id-2",
        path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_withPathAndQueryParams_combined() {
    ApiPath path =
        ApiPath.builder("/blocks/{block_id}/children")
            .pathParam("block_id", "block-1")
            .queryParam("page_size", "10")
            .build();
    assertEquals(
        "https://api.notion.com/v1/blocks/block-1/children?page_size=10",
        path.resolve("https://api.notion.com/v1"));
  }

  @Test
  void resolve_noArg_worksForAbsoluteUrl() {
    ApiPath path = ApiPath.from("https://api.notion.com/v1/pages");
    assertEquals("https://api.notion.com/v1/pages", path.resolve());
  }
}
