package integration.datasources;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT2_DataSources_CRUD extends BaseIntegrationTest {

  private static final String FIRST_COVER_PATH = "files/it-1/first-cover.jpg";
  private static final String FIRST_COVER_NAME = "first-cover.jpg";

  private static final String SECOND_COVER_PATH = "files/it-1/second-cover.jpg";
  private static final String SECOND_COVER_NAME = "second-cover.jpg";

  private static String firstCoverId;
  private static String secondCoverId;

  @BeforeAll
  public static void setup() {
    firstCoverId = uploadFile(FIRST_COVER_PATH, FIRST_COVER_NAME);
    secondCoverId = uploadFile(SECOND_COVER_PATH, SECOND_COVER_NAME);
  }

  @Test
  @DisplayName("IT-2: Data sources - Basic check of CRUD operations for a data source")
  public void testDataSourceCrudOperations() {

    // Step 1 = Create a datasource with a title column, a database endpoint is used to create a
    // datasource in one shot

    Database db =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Test Data Source")
                    .icon("🧪")
                    .cover(firstCoverId)
                    .properties(p -> p.title("Title column").checkbox("Checkbox column").build())
                    .build());

    assertNotNull(db);
    assertNotNull(db.getDataSources(), "Data sources should not be null");
    assertEquals(1, db.getDataSources().size(), "Data sources list size mismatch");

    // Step 2 = Fetch a data source

    DataSource dataSource =
        getNotionClient().dataSources().retrieve(db.getDataSources().get(0).getId());

    assertNotNull(dataSource);
    assertNotNull(dataSource.getTitle());
    assertEquals(1, dataSource.getTitle().size(), "Title rich text list size mismatch");
    assertEquals(
        "Test Data Source",
        dataSource.getTitle().get(0).getPlainText(),
        "Data source title mismatch");
    assertEquals(2, dataSource.getProperties().size(), "Properties size mismatch");
    assertNotNull(dataSource.getIcon(), "Icon should not be null");
    assertEquals("🧪", dataSource.getIcon().getEmoji());
    assertNotNull(dataSource.getCover(), "Cover should not be null");
    assertEquals("file", dataSource.getCover().getType(), "Cover info should be of type 'file'");
    assertNotNull(dataSource.getProperties().get("Title column"), "Title property is missing");
    assertNotNull(
        dataSource.getProperties().get("Checkbox column"), "Checkbox property is missing");

    // Step 3 = Update data source title, an existing column name and add a new column

    DataSource updated =
        getNotionClient()
            .dataSources()
            .update(
                dataSource.getId(),
                b ->
                    b.title("Updated Data Source Title")
                        .icon("🤝")
                        .properties(
                            p ->
                                p.rename("Title column", "New title column name")
                                    .remove("Checkbox column")
                                    .select("Select", "Option 1", "Option 2")
                                    .build())
                        .build());

    assertNotNull(updated);
    assertEquals(1, updated.getTitle().size(), "Title rich text list size mismatch");
    assertEquals(
        "Updated Data Source Title",
        updated.getTitle().get(0).getPlainText(),
        "Data source title mismatch");
    assertNotNull(updated.getIcon(), "Icon should not be null");
    assertEquals("🤝", updated.getIcon().getEmoji());
    assertNotNull(updated.getCover(), "Cover should not be null");
    assertEquals("file", updated.getCover().getType(), "Cover info should be of type 'file'");
    assertEquals(2, updated.getProperties().size(), "Properties size mismatch");
    assertNotNull(
        updated.getProperties().get("New title column name"), "Title property is missing");
    assertNull(
        updated.getProperties().get("Checkbox column"), "Checkbox property should be removed");
    assertNotNull(updated.getProperties().get("Select"), "Select property is missing");

    // Step 4 = Move data source to trash

    DataSource trashed = getNotionClient().dataSources().moveToTrash(updated.getId());

    assertNotNull(trashed);
    assertTrue(trashed.getInTrash(), "Data source should be in trash after moveToTrash operation");

    // Step 5 = Restore data source from trash

    DataSource restored = getNotionClient().dataSources().restore(trashed.getId());

    assertNotNull(restored);
    assertFalse(
        restored.getInTrash(), "Data source should not be in trash after restore operation");
  }

  @AfterAll
  public static void tearDown() {}
}
