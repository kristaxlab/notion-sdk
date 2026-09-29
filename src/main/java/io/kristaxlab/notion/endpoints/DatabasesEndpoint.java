package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.database.UpdateDatabaseParams;

public interface DatabasesEndpoint {

  /**
   * Creates a database and its first data source.
   *
   * <p>Set a database type on {@code request} to create a typed database. That field cannot be
   * combined with {@code initial_data_source}.
   *
   * @param request parent, optional title, and either a database type or an initial data source
   *     schema
   * @return the created database, including its database type when a typed database was created
   */
  Database create(CreateDatabaseParams request);

  Database update(String databaseId, UpdateDatabaseParams request);

  Database retrieve(String databaseId);

  Database delete(String databaseId);

  Database restore(String databaseId);
}
