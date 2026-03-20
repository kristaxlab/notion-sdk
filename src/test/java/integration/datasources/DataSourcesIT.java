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
import io.kristixlab.notion.api.model.datasources.properties.helper.DataSourceSchemaBuilder;
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

  @Test
  @Tag("examples")
  public void exampleCreateDatabase() {
    String otherDsId = "some-existing-datasource-id";

    // ── Pattern A: standalone DataSourceSchemaBuilder (original) ───────────────
    CreateDatabaseParams dbParamsA =
        CreateDatabaseParams.builder()
            .inPage(currTestPageId)
            .title("Full property DB")
            .description("Database to test all data source properties")
            .isInline(true)
            .properties(
                DataSourceSchemaBuilder.builder()
                    .title("Name")
                    .text("Description")
                    .number("Number", NumberFormatType.ZLOTY)
                    .checkbox("Checkbox")
                    .date("Due Date")
                    .select("Category", "Electronics", "Books", "Clothing")
                    .multiSelect("Tags", "Sale", "New", "Popular")
                    .url("Website")
                    .email("Contact Email")
                    .phone("Contact Phone")
                    .people("People")
                    .uniqueId("UniqueId")
                    .createdBy("Created By")
                    .lastEditedBy("Last Edited By")
                    .createdTime("Created Time")
                    .lastEditedTime("Last Edited Time")
                    .files("Files")
                    .formula("Formula", "\"Title: \" + prop(\"Name\")")
                    .place("Place")
                    .relation("One-sided to First DS", otherDsId)
                    .relation("Dual to First DS", otherDsId, "Link to Second DS")
                    .rollup(
                        "Rollup",
                        RollupSchemaParams.builder()
                            .relation("Dual to First DS")
                            .property("Name")
                            .calculate(RollupFunctionType.COUNT)
                            .build())
                    .status("Status")
                    .build())
            .build();

    // ── Pattern B: propertiesBuilder() step — pure fluent, no wrapping needed ──
    CreateDatabaseParams dbParamsB =
        CreateDatabaseParams.builder()
            .inPage(currTestPageId)
            .title("Full property DB")
            .description("Database to test all data source properties")
            .isInline(true)
            .propertiesBuilder()
            .title("Name")
            .text("Description")
            .number("Number", NumberFormatType.ZLOTY)
            .checkbox("Checkbox")
            .date("Due Date")
            .select("Category", "Electronics", "Books", "Clothing")
            .buildProperties()
            .build();

    // ── Pattern C: consumer lambda — concise, no step-switching needed ──────────
    CreateDatabaseParams dbParamsC =
        CreateDatabaseParams.builder()
            .inPage(currTestPageId)
            .title("Full property DB")
            .isInline(true)
            .properties(
                s ->
                    s.title("Name")
                        .text("Description")
                        .number("Number", NumberFormatType.ZLOTY)
                        .checkbox("Checkbox")
                        .date("Due Date")
                        .select("Category", "Electronics", "Books", "Clothing"))
            .build();

    // ── Pattern D: external producer — inject any factory/supplier ──────────────
    CreateDatabaseParams dbParamsD =
        CreateDatabaseParams.builder()
            .inPage(currTestPageId)
            .title("Full property DB")
            .isInline(true)
            .properties(CatalogSchemaFactory::defaultSchema)
            .build();

    getNotion().databases().create(dbParamsA);
    getNotion().databases().create(dbParamsB);
    getNotion().databases().create(dbParamsC);
    getNotion().databases().create(dbParamsD);
  }

  /** Example of a client-supplied schema factory used in Pattern D above. */
  private static class CatalogSchemaFactory {
    static Map<String, DataSourcePropertySchemaParams> defaultSchema() {
      return DataSourceSchemaBuilder.builder()
          .title("Name")
          .text("Description")
          .number("Number", NumberFormatType.ZLOTY)
          .checkbox("Checkbox")
          .date("Due Date")
          .select("Category", "Electronics", "Books", "Clothing")
          .build();
    }
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
