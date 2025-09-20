package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.transport.ApiTransport;
import io.kristixlab.notion.api.exchange.transport.URLInfo;
import io.kristixlab.notion.api.model.databases.CreateDatabaseRequest;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseRequest;

/**
 * API for interacting with Notion Databases endpoints. Provides methods to retrieve, create,
 * update, and query databases.
 */
public class DatabasesApi {

  private static final String DATABASE_ID = "database_id";
  private final ApiTransport transport;

  public DatabasesApi(NotionApiTransport transport) {
    this.transport = transport;
  }

  /**
   * Retrieve a database by its ID.
   *
   * @param databaseId The ID of the database to retrieve
   * @return The database object
   */
  public Database retrieve(String databaseId) {
    validateDatabaseId(databaseId);
    URLInfo urlInfo = URLInfo.builder("/databases/{database_id}").pathParam(DATABASE_ID, databaseId).build();
    return transport.call("GET", urlInfo, Database.class);
  }

  /**
   * Create a new database.
   *
   * @param request The request containing database data
   * @return The created database
   */
  public Database create(CreateDatabaseRequest request) {
    validateRequest(request);
    URLInfo urlInfo = URLInfo.build("/databases");
    return transport.call("POST", urlInfo, request, Database.class);
  }

  /**
   * Update an existing database.
   *
   * @param request The request containing updated database data
   * @return The updated database
   */
  public Database update(UpdateDatabaseRequest request) {
    return update(request.getId(), request);
  }

  /**
   * Update an existing database.
   *
   * @param databaseId The ID of the database to update
   * @param request    The request containing updated database data
   * @return The updated database
   */
  public Database update(String databaseId, UpdateDatabaseRequest request) {
    validateDatabaseId(databaseId);
    validateRequest(request);

    URLInfo urlInfo = URLInfo.builder("/databases/{database_id}").pathParam(DATABASE_ID, databaseId).build();

    return transport.call("PATCH", urlInfo, request, Database.class);
  }

  /**
   * Delete a database by moving it to trash. This operation sets the database's inTrash property to
   * true.
   *
   * @param databaseId The ID of the database to delete
   * @return The updated database with inTrash set to true
   */
  public Database delete(String databaseId) {
    UpdateDatabaseRequest deleteRequest = new UpdateDatabaseRequest();
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
    UpdateDatabaseRequest restoreRequest = new UpdateDatabaseRequest();
    restoreRequest.setInTrash(false);
    return update(databaseId, restoreRequest);
  }

  /**
   * Validates the database ID parameter.
   */
  private void validateDatabaseId(String databaseId) {
    if (databaseId == null || databaseId.trim().isEmpty()) {
      throw new IllegalArgumentException("Database ID cannot be null or empty");
    }
  }

  /**
   * Validates the request object.
   */
  private void validateRequest(Object request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }
}
