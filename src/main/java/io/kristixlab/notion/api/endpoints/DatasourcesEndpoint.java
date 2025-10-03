package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.datasources.CreateDataSourceRequest;
import io.kristixlab.notion.api.model.datasources.Datasource;
import io.kristixlab.notion.api.model.datasources.DatasourceQueryRequest;
import io.kristixlab.notion.api.model.datasources.DatasourceQueryResponse;

/**
 * Interface defining operations for Notion Datasources.
 *
 * @see <a href="https://developers.notion.com/reference/data-sources">Notion Datasources API</a>
 */
public interface DatasourcesEndpoint {
  Datasource create(CreateDataSourceRequest request);

  Datasource update(String dataSourceId, Datasource request);

  Datasource retrieve(String dataSourceId);

  DatasourceQueryResponse query(String dataSourceId);

  DatasourceQueryResponse query(String dataSourceId, DatasourceQueryRequest request);

  DatasourceQueryResponse query(String dataSourceId, String startCursor, Integer pageSize);

  DatasourceQueryResponse query(String dataSourceId, DatasourceQueryRequest request, String startCursor, Integer pageSize);

  Datasource delete(String dataSourceId);

  Datasource restore(String dataSourceId);
}
