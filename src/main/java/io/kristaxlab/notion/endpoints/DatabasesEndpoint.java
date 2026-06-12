package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.database.UpdateDatabaseParams;

public interface DatabasesEndpoint {

  Database create(CreateDatabaseParams request);

  Database update(String databaseId, UpdateDatabaseParams request);

  Database retrieve(String databaseId);

  Database delete(String databaseId);

  Database restore(String databaseId);
}
