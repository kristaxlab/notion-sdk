package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.datasources.CreateDataSourceRequest;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.DataSourcePageList;
import io.kristixlab.notion.api.model.datasources.DataSourceQuery;

/**
 * Interface defining operations for Notion Datasources.
 *
 * @see <a href="https://developers.notion.com/reference/data-sources">Notion Datasources API</a>
 */
public interface DataSourcesEndpoint {
  DataSource create(CreateDataSourceRequest request);

  DataSource update(String dataSourceId, DataSource request);

  DataSource retrieve(String dataSourceId);

  DataSourcePageList query(String dataSourceId);

  DataSourcePageList query(String dataSourceId, DataSourceQuery request);

  DataSourcePageList query(String dataSourceId, String startCursor, Integer pageSize);

  DataSourcePageList query(
      String dataSourceId, DataSourceQuery request, String startCursor, Integer pageSize);

  DataSource delete(String dataSourceId);

  DataSource restore(String dataSourceId);
}
