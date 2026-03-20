package integration.datasources;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import integration.BaseIntegrationTest;
import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.UpdateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.properties.*;
import io.kristixlab.notion.api.model.datasources.properties.NumberFormatType;
import java.util.*;
import org.junit.jupiter.api.*;

public class DataSourcesIT extends BaseIntegrationTest {
  private static String dataSourceTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    dataSourceTestsPageId = IntegrationTestAssisstant.createPageForTests("Data Sources");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(info.getDisplayName(), dataSourceTestsPageId);
  }

  @Test
  @DisplayName("[IT-52]: Data sources - Create and update data source and its properties")
  public void createAndUpdateDataSource() {
    // Step 1: Create a data source with initial properties
    Database db =
        getNotion()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(currTestPageId)
                    .title("Parent DB")
                    .isInline(true)
                    .propertiesBuilder()
                    .title("Name")
                    .text("Description")
                    .number("Price", NumberFormatType.ZLOTY)
                    .date("Due Date")
                    .buildProperties()
                    .build());

    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());
    assertPropertyPresent(ds, "Name", "Description", "Price", "Due Date");
    assertPropertyAbsent(ds, "Availability");
    assertPropertyType("rich_text", ds.getProperties().get("Description"));

    // Step 2: Update icon, title, number format (→ baht), delete due date prop, etc.
    String emoji = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();

    DataSource updated =
        getNotion()
            .dataSources()
            .update(
                ds.getId(),
                UpdateDataSourceParams.builder()
                    .dataSourceTitle("Updated Product catalog")
                    .icon(IconParams.fromEmoji(emoji))
                    .propertiesBuilder()
                    .rename("Name", "Title")
                    .delete("Due Date")
                    .number("Price", NumberFormatType.BAHT)
                    .multiSelect("Description", "Book", "Magazine")
                    .select("Availability", "In Stock", "Out of Stock")
                    .buildProperties()
                    .build());

    assertPropertyPresent(updated, "Title", "Availability", "Description");
    assertPropertyAbsent(updated, "Name", "Due Date");
    assertPropertyType("multi_select", updated.getProperties().get("Description"));
    assertNotNull(updated.getIcon());
    assertNotNull(updated.getTitle());

    // Step 3: Move to trash, then restore
    DataSource deleted = getNotion().dataSources().delete(ds.getId());
    assertTrue(deleted.getInTrash());

    DataSource restored = getNotion().dataSources().restore(ds.getId());
    assertFalse(restored.getInTrash());
  }

  private void assertPropertyPresent(DataSource dataSource, String... propertyNames) {
    assertNotNull(dataSource);
    assertNotNull(dataSource.getProperties());
    for (String propName : propertyNames) {
      assertNotNull(
          dataSource.getProperties().get(propName),
          "Property '" + propName + "' not found among data source properties");
    }
  }

  private void assertPropertyAbsent(DataSource dataSource, String... propertyNames) {
    assertNotNull(dataSource);
    assertNotNull(dataSource.getProperties());
    for (String propName : propertyNames) {
      assertNull(
          dataSource.getProperties().get(propName),
          "Property '" + propName + "' should not present among data source properties");
    }
  }

  private void assertPropertyType(String expectedType, DataSourcePropertySchema property) {
    assertNotNull(property);
    assertEquals(expectedType, property.getType());
  }
}
