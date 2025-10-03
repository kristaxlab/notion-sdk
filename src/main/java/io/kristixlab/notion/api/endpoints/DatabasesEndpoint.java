package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.databases.CreateDatabaseRequest;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseRequest;

/**
 * Interface defining operations for Notion Databases.
 *
 * @see <a href="https://developers.notion.com/reference/databases">Notion Databases API</a>
 */
public interface DatabasesEndpoint {
  Database create(CreateDatabaseRequest request);

  Database retrieve(String databaseId);

  Database update(UpdateDatabaseRequest request);

  Database update(String databaseId, UpdateDatabaseRequest request);

  Database delete(String databaseId);

  Database restore(String databaseId);
}
