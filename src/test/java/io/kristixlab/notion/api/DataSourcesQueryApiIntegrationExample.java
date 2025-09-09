package io.kristixlab.notion.api;

import io.kristixlab.notion.api.model.IntegrationTest;
import io.kristixlab.notion.api.model.datasources.DatasourceQueryRequest;
import io.kristixlab.notion.api.model.datasources.DatasourceQueryResponse;
import io.kristixlab.notion.api.model.datasources.filter.*;
import io.kristixlab.notion.database.properties.SortDirectionType;
import io.kristixlab.notion.database.properties.TimestampType;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for PagesApi that calls actual Notion API endpoints. Stores all responses to
 * files for inspection.
 */
public class DataSourcesQueryApiIntegrationExample extends IntegrationTest {

  // Book notes data source id
  private static final String DATA_SOURCE_ID = "264c5b96-8ec4-8055-8b51-000b4a80c6cc";
  private static DatasourcesApi dataSourcesApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    dataSourcesApi = new DatasourcesApi(getTransport());
  }

  /**
   * Test querying a database. This will query the database with default parameters and save the
   * response.
   */
  @Test
  void testQueryDatabase() throws IOException {
    DatasourceQueryResponse response = dataSourcesApi.query(DATA_SOURCE_ID);
    saveToFile(response, "data-sources-query-rs.json");
  }

  /**
   * Test querying a database with pagination. This will query the database with a page size limit
   * and save the response.
   */
  @Test
  void testQueryDatabaseWithPagination() throws IOException {
    DatasourceQueryResponse onePageResponse = dataSourcesApi.query(DATA_SOURCE_ID, null, 2);
    saveToFile(onePageResponse, "database-query-rs-paginated.json");
  }

  /**
   * Test querying a database with a custom request. This will query the database with sorting and
   * save the response.
   */
  @Test
  void testQueryDatabaseWithSortRequest() throws IOException {
    DatasourceQueryRequest request = new DatasourceQueryRequest();
    request.addSort(TimestampType.LAST_EDITED_TIME, SortDirectionType.DESCENDING);
    saveToFile(request, "data-source-query-with-sort-request.json");

    DatasourceQueryResponse response = dataSourcesApi.query(DATA_SOURCE_ID, request);
    saveToFile(response, "data-source-query-with-sort-response.json");
  }

  /**
   * Test querying a database with a custom request. This will query the database with sorting and
   * save the response.
   */
  @Test
  void testNumberFilterQuery() throws IOException {
    DatasourceQueryRequest request = new DatasourceQueryRequest();
    request.setFilter("Genre", SelectFilter.equals("Literary Fiction"));
    request.addSort(TimestampType.LAST_EDITED_TIME, SortDirectionType.ASCENDING);
    saveToFile(request, "database-query-with-number-filter-rq.json");

    DatasourceQueryResponse response = dataSourcesApi.query(DATA_SOURCE_ID, request, null, 3);
    saveToFile(response, "database-query-with-number-filter-response.json");
  }
}
