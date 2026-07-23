package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.fluent.NotionSchemaBuilder;
import io.kristaxlab.notion.model.datasource.CreateDataSourceParams;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.DataSourcePageList;
import io.kristaxlab.notion.model.datasource.DataSourceQuery;
import io.kristaxlab.notion.model.datasource.UpdateDataSourceParams;
import io.kristaxlab.notion.model.page.templates.Templates;
import java.util.function.Consumer;

/**
 * Interface defining operations for Notion Datasources.
 *
 * @see <a href="https://developers.notion.com/reference/data-sources">Notion Datasources API</a>
 */
public interface DataSourcesEndpoint {
  DataSource create(CreateDataSourceParams request);

  /**
   * Creates a data source by configuring {@link CreateDataSourceParams.Builder} in a lambda. Use
   * the {@code properties(Consumer)} hook to declare the schema with the {@link
   * NotionSchemaBuilder} DSL.
   *
   * @param consumer callback that fills the creation builder
   * @return created data source
   */
  DataSource create(Consumer<CreateDataSourceParams.Builder> consumer);

  DataSource update(String dataSourceId, UpdateDataSourceParams request);

  /**
   * Updates a data source by configuring {@link UpdateDataSourceParams.Builder} in a lambda. Use
   * the {@code properties(Consumer)} hook to add, modify, or remove columns with the {@link
   * NotionSchemaBuilder} DSL.
   *
   * @param dataSourceId data source identifier
   * @param consumer callback that fills the update builder
   * @return updated data source
   */
  DataSource update(String dataSourceId, Consumer<UpdateDataSourceParams.Builder> consumer);

  DataSource retrieve(String dataSourceId);

  DataSourcePageList query(String dataSourceId);

  DataSourcePageList query(String dataSourceId, DataSourceQuery request);

  DataSourcePageList query(String dataSourceId, String startCursor, Integer pageSize);

  DataSourcePageList query(
      String dataSourceId, DataSourceQuery request, String startCursor, Integer pageSize);

  Templates retrieveTemplates(String dataSourceId);

  DataSource moveToTrash(String dataSourceId);

  DataSource restore(String dataSourceId);
}
