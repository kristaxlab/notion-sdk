package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.ApiClientStub;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.DataSourceQuery;
import io.kristixlab.notion.api.model.datasources.UpdateDataSourceParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DataSourcesEndpointImplTest {

  private ApiClientStub client;
  private DataSourcesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new DataSourcesEndpointImpl(client);
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("ds-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieve_rejectsBlankOrNullDataSourceId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(id));
  }

  @Test
  void create() {
    CreateDataSourceParams request = new CreateDataSourceParams();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/data_sources", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void update() {
    UpdateDataSourceParams request = new UpdateDataSourceParams();

    endpoint.update("ds-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void update_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.update("ds-id-1", null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void update_rejectsBlankOrNullDataSourceId(String id) {
    assertThrows(
        IllegalArgumentException.class, () -> endpoint.update(id, new UpdateDataSourceParams()));
  }

  @Test
  void delete() {
    endpoint.delete("ds-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertTrue(((UpdateDataSourceParams) client.getLastBody()).getInTrash());
  }

  @Test
  void restore() {
    endpoint.restore("ds-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertFalse(((UpdateDataSourceParams) client.getLastBody()).getInTrash());
  }

  @Test
  void query_withoutFilters() {
    endpoint.query("ds-id-1");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/query", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
  }

  @Test
  void query() {
    DataSourceQuery request = new DataSourceQuery();

    endpoint.query("ds-id-1", request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/query", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void query_withPaginationParams() {
    endpoint.query("ds-id-1", "cursor-abc", 25);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/query", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
    DataSourceQuery body = (DataSourceQuery) client.getLastBody();
    assertEquals("cursor-abc", body.getStartCursor());
    assertEquals(25, body.getPageSize());
  }

  @Test
  void query_withStartCursorOnly() {
    endpoint.query("ds-id-1", "cursor-abc", null);

    DataSourceQuery body = (DataSourceQuery) client.getLastBody();
    assertEquals("cursor-abc", body.getStartCursor());
    assertNull(body.getPageSize());
  }

  @Test
  void query_withPageSizeOnly() {
    endpoint.query("ds-id-1", null, 50);

    DataSourceQuery body = (DataSourceQuery) client.getLastBody();
    assertNull(body.getStartCursor());
    assertEquals(50, body.getPageSize());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void query_rejectsBlankOrNullDataSourceId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.query(id));
  }

  @Test
  void retrieveTemplates() {
    endpoint.retrieveTemplates("ds-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/templates", client.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", client.getLastUrlInfo().getPathParams().get("data_source_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieveTemplates_rejectsBlankOrNullDataSourceId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieveTemplates(id));
  }
}
