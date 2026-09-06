package tests.databases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.kristaxlab.notion.fluent.NotionSchema;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.DatabaseType;
import io.kristaxlab.notion.model.database.InitialDatasource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT2_Databases_CreateTyped_ValidationException extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-2: Databases - Create a typed database with a schema throws ValidationException")
  public void testCreateTypedDatabaseRejectsCombinedSchema() {
    CreateDatabaseParams withInitialDataSource =
        CreateDatabaseParams.builder()
            .inPage(getTestPageId())
            .databaseType(DatabaseType.TASKS)
            .build();
    withInitialDataSource.setInitialDataSource(
        InitialDatasource.of(NotionSchema.schemaBuilder().title("Name").build()));

    ValidationException withInitialDataSourceError =
        assertThrows(
            ValidationException.class,
            () -> getNotionClient().databases().create(withInitialDataSource),
            "database type combined with an initial data source should throw ValidationException");
    assertEquals(400, withInitialDataSourceError.getStatus());

    ValidationException withPropertiesError =
        assertThrows(
            ValidationException.class,
            () ->
                getNotionClient()
                    .databases()
                    .create(
                        CreateDatabaseParams.builder()
                            .inPage(getTestPageId())
                            .databaseType(DatabaseType.TASKS)
                            .properties(s -> s.title("Name"))
                            .build()),
            "database type combined with properties should throw ValidationException");
    assertEquals(400, withPropertiesError.getStatus());
  }
}
