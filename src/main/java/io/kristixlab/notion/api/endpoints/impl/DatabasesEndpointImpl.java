package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.DatabasesEndpoint;
import io.kristixlab.notion.api.http.base.client.ApiClient;
import io.kristixlab.notion.api.http.base.request.ApiPath;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseParams;

/**
 * API for interacting with Notion Databases endpoints. Provides methods to retrieve, create,
 * update, and query databases.
 */
public class DatabasesEndpointImpl implements DatabasesEndpoint {

  private static final String DATABASE_ID = "database_id";
  private final ApiClient client;

  public DatabasesEndpointImpl(ApiClient client) {
    this.client = client;
  }

  /**
   * Retrieve a database by its ID.
   *
   * @param databaseId The ID of the database to retrieve
   * @return The database object
   */
  public Database retrieve(String databaseId) {
    validateDatabaseId(databaseId);
    ApiPath urlInfo =
        ApiPath.builder("/databases/{database_id}").pathParam(DATABASE_ID, databaseId).build();
    return client.call("GET", urlInfo, Database.class);
  }

  /**
   * Create a new database.
   *
   * @param request The request containing database data
   * @return The created database
   */
  public Database create(CreateDatabaseParams request) {
    validateRequest(request);
    ApiPath urlInfo = ApiPath.from("/databases");
    return client.call("POST", urlInfo, request, Database.class);
  }

  /**
   * Update an existing database.
   *
   * @param databaseId The ID of the database to update
   * @param request The request containing updated database data
   * @return The updated database
   */
  public Database update(String databaseId, UpdateDatabaseParams request) {
    validateDatabaseId(databaseId);
    validateRequest(request);

    ApiPath urlInfo =
        ApiPath.builder("/databases/{database_id}").pathParam(DATABASE_ID, databaseId).build();

    return client.call("PATCH", urlInfo, request, Database.class);
  }

  /**
   * Delete a database by moving it to trash. This operation sets the database's inTrash property to
   * true.
   *
   * @param databaseId The ID of the database to delete
   * @return The updated database with inTrash set to true
   */
  public Database delete(String databaseId) {
    UpdateDatabaseParams deleteRequest = new UpdateDatabaseParams();
    deleteRequest.setInTrash(true);
    return update(databaseId, deleteRequest);
  }

  /**
   * Restore a database from trash. This operation sets the database's inTrash property to false.
   *
   * @param databaseId The ID of the database to restore
   * @return The updated database with inTrash set to false
   */
  public Database restore(String databaseId) {
    UpdateDatabaseParams restoreRequest = new UpdateDatabaseParams();
    restoreRequest.setInTrash(false);
    return update(databaseId, restoreRequest);
  }

  /** Validates the database ID parameter. */
  private void validateDatabaseId(String databaseId) {
    if (databaseId == null || databaseId.trim().isEmpty()) {
      throw new IllegalArgumentException("Database ID cannot be null or empty");
    }
  }

  /** Validates the request object. */
  private void validateRequest(CreateDatabaseParams request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }

  /** Validates the request object. */
  private void validateRequest(UpdateDatabaseParams request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }
}
