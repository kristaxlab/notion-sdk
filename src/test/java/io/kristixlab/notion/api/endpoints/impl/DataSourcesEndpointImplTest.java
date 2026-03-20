package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.TransportStub;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.DataSourceQuery;
import io.kristixlab.notion.api.model.datasources.UpdateDataSourceParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DataSourcesEndpointImplTest {

  private TransportStub transport;
  private DataSourcesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    transport = new TransportStub();
    endpoint = new DataSourcesEndpointImpl(transport);
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("ds-id-1");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
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

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/data_sources", transport.getLastUrlInfo().getUrl());
    assertSame(request, transport.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void update() {
    UpdateDataSourceParams request = new UpdateDataSourceParams();

    endpoint.update("ds-id-1", request);

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertSame(request, transport.getLastBody());
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

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertTrue(((UpdateDataSourceParams) transport.getLastBody()).getInTrash());
  }

  @Test
  void restore() {
    endpoint.restore("ds-id-1");

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertFalse(((UpdateDataSourceParams) transport.getLastBody()).getInTrash());
  }

  @Test
  void query_withoutFilters() {
    endpoint.query("ds-id-1");

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/query", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
  }

  @Test
  void query() {
    DataSourceQuery request = new DataSourceQuery();

    endpoint.query("ds-id-1", request);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/query", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
    assertSame(request, transport.getLastBody());
  }

  @Test
  void query_withPaginationParams() {
    endpoint.query("ds-id-1", "cursor-abc", 25);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/query", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
    DataSourceQuery body = (DataSourceQuery) transport.getLastBody();
    assertEquals("cursor-abc", body.getStartCursor());
    assertEquals(25, body.getPageSize());
  }

  @Test
  void query_withStartCursorOnly() {
    endpoint.query("ds-id-1", "cursor-abc", null);

    DataSourceQuery body = (DataSourceQuery) transport.getLastBody();
    assertEquals("cursor-abc", body.getStartCursor());
    assertNull(body.getPageSize());
  }

  @Test
  void query_withPageSizeOnly() {
    endpoint.query("ds-id-1", null, 50);

    DataSourceQuery body = (DataSourceQuery) transport.getLastBody();
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

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/data_sources/{data_source_id}/templates", transport.getLastUrlInfo().getUrl());
    assertEquals("ds-id-1", transport.getLastUrlInfo().getPathParams().get("data_source_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieveTemplates_rejectsBlankOrNullDataSourceId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieveTemplates(id));
  }
}
