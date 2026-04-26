package io.kristaxlab.notion.endpoints.impl;

import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;

/** Provides shared HTTP client access and common request helpers for endpoint implementations. */
public abstract class BaseEndpointImpl {

  private final ApiClient client;

  /** HTTP GET method token. */
  protected static final String GET = "GET";

  /** HTTP POST method token. */
  protected static final String POST = "POST";

  /** HTTP PUT method token. */
  protected static final String PUT = "PUT";

  /** HTTP PATCH method token. */
  protected static final String PATCH = "PATCH";

  /** HTTP DELETE method token. */
  protected static final String DELETE = "DELETE";

  /**
   * Creates a base endpoint wrapper around the configured API client.
   *
   * @param client client used to execute API requests
   */
  public BaseEndpointImpl(ApiClient client) {
    this.client = client;
  }

  /**
   * Returns the API client used by this endpoint.
   *
   * @return the configured API client
   */
  protected ApiClient getClient() {
    return client;
  }

  /**
   * Creates an API path builder with optional Notion pagination query parameters.
   *
   * @param url endpoint path, for example {@code "/users"}
   * @param startCursor pagination cursor; omitted when {@code null}
   * @param pageSize page size to request; omitted when {@code null}
   * @return a builder that can be further customized before {@link ApiPath.Builder#build()}
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
