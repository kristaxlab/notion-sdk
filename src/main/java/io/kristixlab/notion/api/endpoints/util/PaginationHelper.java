package io.kristixlab.notion.api.endpoints.util;

import io.kristixlab.notion.api.http.request.ApiPath;

/**
 * Notion-specific pagination helper for building {@link ApiPath} instances with optional {@code
 * start_cursor} and {@code page_size} query parameters.
 */
public final class PaginationHelper {

  private PaginationHelper() {}

  /**
   * Creates a builder pre-populated with the given URL and optional Notion pagination parameters.
   *
   * @param url the API path (e.g. {@code "/users"})
   * @param startCursor pagination cursor; skipped when {@code null}
   * @param pageSize number of results per page; skipped when {@code null}
   * @return a builder that can be further customized before calling {@link ApiPath.Builder#build()}
   */
  public static ApiPath.Builder paginatedPath(String url, String startCursor, Integer pageSize) {
    ApiPath.Builder builder = ApiPath.builder(url);
    if (startCursor != null) {
      builder.queryParam("start_cursor", startCursor);
    }
    if (pageSize != null) {
      builder.queryParam("page_size", String.valueOf(pageSize));
    }
    return builder;
  }
}
