package io.kristixlab.notion.api.model.databases;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Database deserialization from JSON. Tests all database property types and their
 * configurations.
 */
public class DatabaseDeserializationTest extends BaseTest {

  @Test
  void testBasicFields() throws Exception {
    Database database = loadFromFile("databases/database-retrieve-rs.json", Database.class);

    assertNotNull(database);
    assertNotNull(database.getTitle());
    assertNotNull(database.getTitle().get(0).getPlainText());
    assertNotNull(database.getDescription());
    assertNotNull(database.getDescription().get(0).getPlainText());
    assertNotNull(database.getUrl());
    assertNotNull(database.getPublicUrl());
    assertFalse(database.getIsInline());
    assertNotNull(database.getDataSources());
    assertEquals(2, database.getDataSources().size());
    assertNotNull(database.getDataSources().get(0).getId());
    assertNotNull(database.getDataSources().get(0).getName());
  }

  @Test
  void testCreateDatabaseRequest() throws Exception {
    CreateDatabaseRequest request =
        loadFromFile("databases/database-create-rq.json", CreateDatabaseRequest.class);

    assertNotNull(request);
    assertNotNull(request.getTitle());
    assertNotNull(request.getTitle().get(0).getPlainText());
    assertNotNull(request.getDescription());
    assertNotNull(request.getDescription().get(0).getPlainText());
    assertNotNull(request.getParent());
    assertEquals("page_id", request.getParent().getType());
    assertNotNull(request.getParent().getPageId());
    assertTrue(request.getIsInline());
    assertNotNull(request.getInitialDataSource());
    assertNotNull(request.getInitialDataSource().getProperties());
    assertTrue(request.getInitialDataSource().getProperties().containsKey("Description"));
    assertTrue(request.getInitialDataSource().getProperties().containsKey("Priority"));
    assertTrue(request.getInitialDataSource().getProperties().containsKey("Completed"));
    assertTrue(request.getInitialDataSource().getProperties().containsKey("Select"));
    assertTrue(request.getInitialDataSource().getProperties().containsKey("Name"));
  }

  @Test
  void testCreateDatabaseResponse() throws Exception {
    Database database = loadFromFile("databases/database-create-rs.json", Database.class);
    assertNotNull(database);
    assertNotNull(database.getTitle());
    assertNotNull(database.getTitle().get(0).getPlainText());
    assertNotNull(database.getDescription());
    assertNotNull(database.getDescription().get(0).getPlainText());
    assertNotNull(database.getUrl());
    assertNotNull(database.getPublicUrl());
    assertTrue(database.getIsInline());
    assertNotNull(database.getDataSources());
    assertEquals(1, database.getDataSources().size());
    assertNotNull(database.getDataSources().get(0).getId());
    assertNotNull(database.getDataSources().get(0).getName());
  }
}
