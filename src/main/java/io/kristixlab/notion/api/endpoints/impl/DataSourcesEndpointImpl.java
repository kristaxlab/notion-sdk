package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.DataSourcesEndpoint;
import io.kristixlab.notion.api.http.client.ApiClient;
import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.model.datasources.*;
import io.kristixlab.notion.api.model.pages.templates.Templates;

/**
 * API for interacting with Notion Data Sources endpoints (API version 2025-09-03+). Provides
 * methods to retrieve, create, update, and query data sources.
 */
public class DataSourcesEndpointImpl implements DataSourcesEndpoint {

  private static final String DATA_SOURCE_ID = "data_source_id";

  private final ApiClient client;

  public DataSourcesEndpointImpl(ApiClient client) {
    this.client = client;
  }

  /**
   * Retrieve a data source by its ID.
   *
   * @param dataSourceId The ID of the data source to retrieve
   * @return The data source object
   */
  public DataSource retrieve(String dataSourceId) {
    validateDataSourceId(dataSourceId);
    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();
    return client.call("GET", urlInfo, DataSource.class);
  }

  /**
   * Create a new data source.
   *
   * @param request The request containing data source data
   * @return The created data source
   */
  public DataSource create(CreateDataSourceParams request) {
    validateRequest(request);
    return client.call("POST", ApiPath.from("/data_sources"), request, DataSource.class);
  }

  /**
   * Update an existing data source.
   *
   * @param dataSourceId The ID of the data source to update
   * @param request The request containing updated data source data
   * @return The updated data source
   */
  public DataSource update(String dataSourceId, UpdateDataSourceParams request) {
    validateDataSourceId(dataSourceId);
    validateRequest(request);

    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();

    return client.call("PATCH", urlInfo, request, DataSource.class);
  }

  /**
   * Delete a data source by moving it to trash. This operation sets the data source's inTrash
   * property to true.
   *
   * @param dataSourceId The ID of the data source to delete
   * @return The updated data source with inTrash set to true
   */
  public DataSource delete(String dataSourceId) {
    UpdateDataSourceParams deleteRequest = new UpdateDataSourceParams();
    deleteRequest.setInTrash(true);
    return update(dataSourceId, deleteRequest);
  }

  /**
   * Restore a data source from trash. This operation sets the data source's inTrash property to
   * false.
   *
   * @param dataSourceId The ID of the data source to restore
   * @return The updated data source with inTrash set to false
   */
  public DataSource restore(String dataSourceId) {
    UpdateDataSourceParams restoreRequest = new UpdateDataSourceParams();
    restoreRequest.setInTrash(false);
    return update(dataSourceId, restoreRequest);
  }

  /**
   * Query a data source to get pages that match the specified criteria.
   *
   * @param dataSourceId The ID of the data source to query
   * @return Response containing matching pages
   */
  public DataSourcePageList query(String dataSourceId) {
    return query(dataSourceId, null, null, null);
  }

  /**
   * Query a data source to get pages that match the specified criteria.
   *
   * @param dataSourceId The ID of the data source to query
   * @param request The query request containing filter and sort criteria
   * @return Response containing matching pages
   */
  public DataSourcePageList query(String dataSourceId, DataSourceQuery request) {
    return query(dataSourceId, request, null, null);
  }

  /**
   * Query a data source with pagination parameters.
   *
   * @param dataSourceId The ID of the data source to query
   * @param startCursor The cursor to start pagination from
   * @param pageSize The number of items to return (max 100)
   * @return Response containing matching pages
   */
  public DataSourcePageList query(String dataSourceId, String startCursor, Integer pageSize) {
    return query(dataSourceId, new DataSourceQuery(), startCursor, pageSize);
  }

  /**
   * Query a data source with pagination parameters.
   *
   * @param dataSourceId The ID of the data source to query
   * @param request The query request containing filter and sort criteria
   * @param pageSize The number of items to return (max 100)
   * @param startCursor The cursor to start pagination from
   * @return Response containing matching pages
   */
  public DataSourcePageList query(
      String dataSourceId, DataSourceQuery request, String startCursor, Integer pageSize) {
    validateDataSourceId(dataSourceId);

    if (startCursor != null) {
      request.setStartCursor(startCursor);
    }

    if (pageSize != null) {
      request.setPageSize(pageSize);
    }

    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}/query")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();

    return client.call("POST", urlInfo, request, DataSourcePageList.class);
  }

  /**
   * Retrieve a data source templates by its ID.
   *
   * @param dataSourceId The ID of the data source to retrieve templates for
   * @return Templates
   */
  public Templates retrieveTemplates(String dataSourceId) {
    validateDataSourceId(dataSourceId);
    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}/templates")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();
    return client.call("GET", urlInfo, Templates.class);
  }

  private void validateDataSourceId(String dataSourceId) {
    if (dataSourceId == null || dataSourceId.trim().isEmpty()) {
      throw new IllegalArgumentException("Data source ID cannot be null or empty");
    }
  }

  private void validateRequest(Object request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }
}
