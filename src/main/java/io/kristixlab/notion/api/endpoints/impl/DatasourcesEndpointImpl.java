package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.DatasourcesEndpoint;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.http.transport.HttpTransportImpl;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceRequest;
import io.kristixlab.notion.api.model.datasources.Datasource;
import io.kristixlab.notion.api.model.datasources.DatasourceQueryRequest;
import io.kristixlab.notion.api.model.datasources.DatasourceQueryResponse;

/**
 * API for interacting with Notion Data Sources endpoints (API version 2025-09-03+). Provides
 * methods to retrieve, create, update, and query data sources.
 */
public class DatasourcesEndpointImpl implements DatasourcesEndpoint {

  private static final String DATA_SOURCE_ID = "data_source_id";

  private final HttpTransportImpl transport;

  public DatasourcesEndpointImpl(NotionHttpTransport transport) {
    this.transport = transport;
  }

  /**
   * Retrieve a data source by its ID.
   *
   * @param dataSourceId The ID of the data source to retrieve
   * @return The data source object
   */
  public Datasource retrieve(String dataSourceId) {
    validateDataSourceId(dataSourceId);
    URLInfo urlInfo = URLInfo.builder("/data_sources/{data_source_id}")
            .pathParam(DATA_SOURCE_ID, dataSourceId).build();
    return transport.call("GET", urlInfo, Datasource.class);
  }

  /**
   * Create a new data source.
   *
   * @param request The request containing data source data
   * @return The created data source
   */
  public Datasource create(CreateDataSourceRequest request) {
    validateRequest(request);
    return transport.call("POST", URLInfo.build("/data_sources"), request, Datasource.class);
  }

  /**
   * Update an existing data source.
   *
   * @param dataSourceId The ID of the data source to update
   * @param request      The request containing updated data source data
   * @return The updated data source
   */
  // TODO replace with UpdateDatasourceRequest to only support fields that are actually possible to change via the API
  public Datasource update(String dataSourceId, Datasource request) {
    validateDataSourceId(dataSourceId);
    validateRequest(request);

    URLInfo urlInfo = URLInfo.builder("/data_sources/{data_source_id}")
            .pathParam(DATA_SOURCE_ID, dataSourceId).build();

    return transport.call("PATCH", urlInfo, request, Datasource.class);
  }

  /**
   * Delete a data source by moving it to trash. This operation sets the data source's inTrash
   * property to true.
   *
   * @param dataSourceId The ID of the data source to delete
   * @return The updated data source with inTrash set to true
   */
  public Datasource delete(String dataSourceId) {
    Datasource deleteRequest = new Datasource();
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
  public Datasource restore(String dataSourceId) {
    Datasource restoreRequest = new Datasource();
    restoreRequest.setInTrash(false);
    return update(dataSourceId, restoreRequest);
  }

  /**
   * Query a data source to get pages that match the specified criteria.
   *
   * @param dataSourceId The ID of the data source to query
   * @return Response containing matching pages
   */
  public DatasourceQueryResponse query(String dataSourceId) {
    return query(dataSourceId, null, null, null);
  }

  /**
   * Query a data source to get pages that match the specified criteria.
   *
   * @param dataSourceId The ID of the data source to query
   * @param request      The query request containing filter and sort criteria
   * @return Response containing matching pages
   */
  public DatasourceQueryResponse query(String dataSourceId, DatasourceQueryRequest request) {
    return query(dataSourceId, request, null, null);
  }

  /**
   * Query a data source with pagination parameters.
   *
   * @param dataSourceId The ID of the data source to query
   * @param startCursor  The cursor to start pagination from
   * @param pageSize     The number of items to return (max 100)
   * @return Response containing matching pages
   */
  public DatasourceQueryResponse query(String dataSourceId, String startCursor, Integer pageSize) {
    return query(dataSourceId, new DatasourceQueryRequest(), startCursor, pageSize);
  }

  /**
   * Query a data source with pagination parameters.
   *
   * @param dataSourceId The ID of the data source to query
   * @param request      The query request containing filter and sort criteria
   * @param pageSize     The number of items to return (max 100)
   * @param startCursor  The cursor to start pagination from
   * @return Response containing matching pages
   */
  public DatasourceQueryResponse query(
          String dataSourceId, DatasourceQueryRequest request, String startCursor, Integer pageSize) {
    validateDataSourceId(dataSourceId);
    validateRequest(request);

    if (startCursor != null) {
      request.setStartCursor(startCursor);
    }

    if (pageSize != null) {
      request.setPageSize(pageSize);
    }

    URLInfo urlInfo = URLInfo.builder("/data_sources/{data_source_id}/query").
            pathParam(DATA_SOURCE_ID, dataSourceId).build();

    return transport.call("POST", urlInfo, request, DatasourceQueryResponse.class);
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
