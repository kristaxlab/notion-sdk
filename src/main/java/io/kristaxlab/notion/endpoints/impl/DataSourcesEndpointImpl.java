package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;
import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.DataSourcesEndpoint;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.datasource.CreateDataSourceParams;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.DataSourcePageList;
import io.kristaxlab.notion.model.datasource.DataSourceQuery;
import io.kristaxlab.notion.model.datasource.UpdateDataSourceParams;
import io.kristaxlab.notion.model.page.templates.Templates;
import java.util.function.Consumer;

/**
 * API for interacting with Notion Data Sources endpoints (API version 2025-09-03+). Provides
 * methods to retrieve, create, update, and query data sources.
 */
public class DataSourcesEndpointImpl extends BaseEndpointImpl implements DataSourcesEndpoint {

  private static final String DATA_SOURCE_ID = "data_source_id";

  public DataSourcesEndpointImpl(ApiClient client) {
    super(client);
  }

  /**
   * Retrieve a data source by its ID.
   *
   * @param dataSourceId The ID of the data source to retrieve
   * @return The data source object
   */
  public DataSource retrieve(String dataSourceId) {
    checkNotNullOrEmpty(dataSourceId, "dataSourceId");
    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();
    return getClient().call("GET", urlInfo, DataSource.class);
  }

  /**
   * Create a new data source.
   *
   * @param request The request containing data source data
   * @return The created data source
   */
  public DataSource create(CreateDataSourceParams request) {
    checkNotNull(request, "request");
    return getClient().call("POST", ApiPath.from("/data_sources"), request, DataSource.class);
  }

  /**
   * Create a new data source by configuring the builder in a lambda.
   *
   * @param consumer callback that fills the creation builder
   * @return the created data source
   */
  public DataSource create(Consumer<CreateDataSourceParams.Builder> consumer) {
    checkNotNull(consumer, "consumer");
    CreateDataSourceParams.Builder builder = CreateDataSourceParams.builder();
    consumer.accept(builder);
    return create(builder.build());
  }

  /**
   * Update an existing data source.
   *
   * @param dataSourceId The ID of the data source to update
   * @param request The request containing updated data source data
   * @return The updated data source
   */
  public DataSource update(String dataSourceId, UpdateDataSourceParams request) {
    checkNotNullOrEmpty(dataSourceId, "dataSourceId");
    checkNotNull(request, "request");

    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();

    return getClient().call("PATCH", urlInfo, request, DataSource.class);
  }

  /**
   * Update an existing data source by configuring the builder in a lambda.
   *
   * @param dataSourceId The ID of the data source to update
   * @param consumer callback that fills the update builder
   * @return The updated data source
   */
  public DataSource update(String dataSourceId, Consumer<UpdateDataSourceParams.Builder> consumer) {
    checkNotNullOrEmpty(dataSourceId, "dataSourceId");
    checkNotNull(consumer, "consumer");
    UpdateDataSourceParams.Builder builder = UpdateDataSourceParams.builder();
    consumer.accept(builder);
    return update(dataSourceId, builder.build());
  }

  /**
   * Delete a data source by moving it to trash. This operation sets the data source's inTrash
   * property to true.
   *
   * @param dataSourceId The ID of the data source to delete
   * @return The updated data source with inTrash set to true
   */
  public DataSource moveToTrash(String dataSourceId) {
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
    checkNotNullOrEmpty(dataSourceId, "dataSourceId");

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

    return getClient().call("POST", urlInfo, request, DataSourcePageList.class);
  }

  /**
   * Retrieve a data source templates by its ID.
   *
   * @param dataSourceId The ID of the data source to retrieve templates for
   * @return Templates
   */
  public Templates retrieveTemplates(String dataSourceId) {
    checkNotNullOrEmpty(dataSourceId, "dataSourceId");
    ApiPath urlInfo =
        ApiPath.builder("/data_sources/{data_source_id}/templates")
            .pathParam(DATA_SOURCE_ID, dataSourceId)
            .build();
    return getClient().call("GET", urlInfo, Templates.class);
  }
}
