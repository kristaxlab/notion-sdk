package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseParams;

/**
 * Interface defining operations for Notion Databases.
 *
 * @see <a href="https://developers.notion.com/reference/databases">Notion Databases API</a>
 */
public interface DatabasesEndpoint {
  Database create(CreateDatabaseParams request);

  Database update(String databaseId, UpdateDatabaseParams request);

  Database retrieve(String databaseId);

  Database delete(String databaseId);

  Database restore(String databaseId);
}
