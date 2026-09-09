package tests.databases;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.database.DatabaseType;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT1_Databases_CreateTyped extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-1: Databases - Create a typed database")
  public void testCreateTypedDatabase() {
    Database created =
        getNotionClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .databaseType(DatabaseType.TASKS)
                    .build());

    assertNotNull(created);
    assertEquals(DatabaseType.TASKS.type(), created.getDatabaseType());
    assertNotNull(created.getTitle());
    assertFalse(created.getTitle().isEmpty());
    assertNotNull(created.getDataSources());
    assertEquals(1, created.getDataSources().size());

    Database retrieved = getNotionClient().databases().retrieve(created.getId());
    assertEquals(DatabaseType.TASKS.type(), retrieved.getDatabaseType());

    DataSource dataSource =
        getNotionClient().dataSources().retrieve(created.getDataSources().get(0).getId());
    assertEquals(DatabaseType.TASKS.type(), dataSource.getDatabaseType());
    assertContainsPropertyTypes(
        dataSource.getProperties(), List.of("title", "people", "status", "date"));
  }

  private static void assertContainsPropertyTypes(
      Map<String, DataSourcePropertySchema> properties, List<String> expectedTypes) {
    assertNotNull(properties);
    for (String expectedType : expectedTypes) {
      boolean present =
          properties.values().stream()
              .anyMatch(property -> expectedType.equals(property.getType()));
      assertTrue(present, "expected a column of type " + expectedType);
    }
  }
}
