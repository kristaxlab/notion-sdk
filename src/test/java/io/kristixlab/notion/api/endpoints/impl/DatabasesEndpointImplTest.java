package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.ApiClientStub;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DatabasesEndpointImplTest {

  private ApiClientStub client;
  private DatabasesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new DatabasesEndpointImpl(client);
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("db-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/databases/{database_id}", client.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", client.getLastUrlInfo().getPathParams().get("database_id"));
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

    assertEquals("POST", client.getLastMethod());
    assertEquals("/databases", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void update() {
    UpdateDatabaseParams request = new UpdateDatabaseParams();

    endpoint.update("db-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/databases/{database_id}", client.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", client.getLastUrlInfo().getPathParams().get("database_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void update_fromRequest() {
    UpdateDatabaseParams request = new UpdateDatabaseParams();
    endpoint.update("db-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/databases/{database_id}", client.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", client.getLastUrlInfo().getPathParams().get("database_id"));
    assertSame(request, client.getLastBody());
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
        IllegalArgumentException.class, () -> endpoint.update(id, new UpdateDatabaseParams()));
  }

  @Test
  void delete() {
    endpoint.delete("db-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/databases/{database_id}", client.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", client.getLastUrlInfo().getPathParams().get("database_id"));
    assertTrue(((UpdateDatabaseParams) client.getLastBody()).getInTrash());
  }

  @Test
  void restore() {
    endpoint.restore("db-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/databases/{database_id}", client.getLastUrlInfo().getUrl());
    assertEquals("db-id-1", client.getLastUrlInfo().getPathParams().get("database_id"));
    assertFalse(((UpdateDatabaseParams) client.getLastBody()).getInTrash());
  }
}
