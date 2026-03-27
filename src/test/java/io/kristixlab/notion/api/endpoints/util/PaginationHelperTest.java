package io.kristixlab.notion.api.endpoints.util;

import static io.kristixlab.notion.api.endpoints.util.PaginationHelper.paginatedPath;
import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.request.ApiPath;
import java.util.List;
import org.junit.jupiter.api.Test;

class PaginationHelperTest {

  @Test
  void withBothParams() {
    ApiPath path = paginatedPath("/pages", "cursor-abc", 25).build();

    assertEquals("/pages", path.getUrl());
    assertEquals(List.of("cursor-abc"), path.getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), path.getQueryParams().get("page_size"));
  }

  @Test
  void withStartCursorOnly() {
    ApiPath path = paginatedPath("/pages", "cursor-abc", null).build();

    assertEquals(List.of("cursor-abc"), path.getQueryParams().get("start_cursor"));
    assertFalse(path.getQueryParams().containsKey("page_size"));
  }

  @Test
  void withPageSizeOnly() {
    ApiPath path = paginatedPath("/pages", null, 50).build();

    assertEquals(List.of("50"), path.getQueryParams().get("page_size"));
    assertFalse(path.getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void withNullParams() {
    ApiPath path = paginatedPath("/pages", null, null).build();

    assertTrue(path.getQueryParams().isEmpty());
  }

  @Test
  void builderCanBeExtendedAfterPagination() {
    ApiPath path =
        paginatedPath("/blocks/{block_id}/children", "cursor-abc", 10)
            .pathParam("block_id", "block-id-1")
            .queryParam("filter", "active")
            .build();

    assertEquals("/blocks/{block_id}/children", path.getUrl());
    assertEquals("block-id-1", path.getPathParams().get("block_id"));
    assertEquals(List.of("active"), path.getQueryParams().get("filter"));
    assertEquals(List.of("cursor-abc"), path.getQueryParams().get("start_cursor"));
    assertEquals(List.of("10"), path.getQueryParams().get("page_size"));
  }
}
