package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.datasource.CreateDataSourceParams;
import io.kristaxlab.notion.model.datasource.DataSourcePageList;
import io.kristaxlab.notion.model.datasource.DataSourceQuery;
import io.kristaxlab.notion.model.datasource.UpdateDataSourceParams;
import io.kristaxlab.notion.model.page.templates.Templates;
import javax.sql.DataSource;

/**
 * Interface defining operations for Notion Datasources.
 *
 * @see <a href="https://developers.notion.com/reference/data-sources">Notion Datasources API</a>
 */
public interface DataSourcesEndpoint {
  DataSource create(CreateDataSourceParams request);

  DataSource update(String dataSourceId, UpdateDataSourceParams request);

  DataSource retrieve(String dataSourceId);

  DataSourcePageList query(String dataSourceId);

  DataSourcePageList query(String dataSourceId, DataSourceQuery request);

  DataSourcePageList query(String dataSourceId, String startCursor, Integer pageSize);

  DataSourcePageList query(
      String dataSourceId, DataSourceQuery request, String startCursor, Integer pageSize);

  Templates retrieveTemplates(String dataSourceId);

  DataSource delete(String dataSourceId);

  DataSource restore(String dataSourceId);
}
