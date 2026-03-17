package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.TransportStub;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DatabasesEndpointImplTest {

  private TransportStub transport;
  private DatabasesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    transport = new TransportStub();
    endpoint = new DatabasesEndpointImpl(transport);
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("db-id-1");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/databases/{database_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", transport.getLastUrlInfo().getPathParams().get("database_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieve_rejectsBlankOrNullDatabaseId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(id));
  }

  @Test
  void create() {
    CreateDatabaseParams request = new CreateDatabaseParams();

    endpoint.create(request);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/databases", transport.getLastUrlInfo().getUrl());
    assertSame(request, transport.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void update() {
    UpdateDatabaseRequest request = new UpdateDatabaseRequest();

    endpoint.update("db-id-1", request);

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/databases/{database_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", transport.getLastUrlInfo().getPathParams().get("database_id"));
    assertSame(request, transport.getLastBody());
  }

  @Test
  void update_fromRequest() {
    UpdateDatabaseRequest request = new UpdateDatabaseRequest();
    request.setId("db-id-1");

    endpoint.update(request);

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/databases/{database_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", transport.getLastUrlInfo().getPathParams().get("database_id"));
    assertSame(request, transport.getLastBody());
  }

  @Test
  void update_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.update("db-id-1", null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void update_rejectsBlankOrNullDatabaseId(String id) {
    assertThrows(
        IllegalArgumentException.class, () -> endpoint.update(id, new UpdateDatabaseRequest()));
  }

  @Test
  void delete() {
    endpoint.delete("db-id-1");

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/databases/{database_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", transport.getLastUrlInfo().getPathParams().get("database_id"));
    assertTrue(((UpdateDatabaseRequest) transport.getLastBody()).getInTrash());
  }

  @Test
  void restore() {
    endpoint.restore("db-id-1");

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/databases/{database_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", transport.getLastUrlInfo().getPathParams().get("database_id"));
    assertFalse(((UpdateDatabaseRequest) transport.getLastBody()).getInTrash());
  }
}
