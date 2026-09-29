package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;
import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.DatabasesEndpoint;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.database.UpdateDatabaseParams;

/**
 * API for interacting with Notion Databases endpoints. Provides methods to retrieve, create,
 * update, and query databases.
 */
public class DatabasesEndpointImpl extends BaseEndpointImpl implements DatabasesEndpoint {

  private static final String DATABASE_ID = "database_id";

  public DatabasesEndpointImpl(ApiClient client) {
    super(client);
  }

  /**
   * Retrieve a database by its ID.
   *
   * @param databaseId The ID of the database to retrieve
   * @return The database object
   */
  public Database retrieve(String databaseId) {
    checkNotNullOrEmpty(databaseId, "databaseId");
    ApiPath urlInfo =
        ApiPath.builder("/databases/{database_id}").pathParam(DATABASE_ID, databaseId).build();
    return getClient().call("GET", urlInfo, Database.class);
  }

  /** {@inheritDoc} */
  public Database create(CreateDatabaseParams request) {
    checkNotNull(request, "request");
    ApiPath urlInfo = ApiPath.from("/databases");
    return getClient().call("POST", urlInfo, request, Database.class);
  }

  /**
   * Update an existing database.
   *
   * @param databaseId The ID of the database to update
   * @param request The request containing updated database data
   * @return The updated database
   */
  public Database update(String databaseId, UpdateDatabaseParams request) {
    checkNotNullOrEmpty(databaseId, "databaseId");
    checkNotNull(request, "request");

    ApiPath urlInfo =
        ApiPath.builder("/databases/{database_id}").pathParam(DATABASE_ID, databaseId).build();

    return getClient().call("PATCH", urlInfo, request, Database.class);
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
}
