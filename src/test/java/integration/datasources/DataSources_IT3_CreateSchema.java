package integration.datasources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import integration.BaseIntegrationTest;
import io.kristaxlab.notion.fluent.NotionSchema;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.NumberFormatType;
import io.kristaxlab.notion.model.datasource.properties.RollupFunctionType;
import java.util.Map;
import org.junit.jupiter.api.*;

public class DataSources_IT3_CreateSchema extends BaseIntegrationTest {

  private static final String TITLE_PROP_NAME = "Name";

  private static String parentDatabaseId;
  private static String initialDataSourceId;

  @BeforeEach
  public void setup() {
    Database db = createDatabase();
    parentDatabaseId = db.getId();
    initialDataSourceId = db.getDataSources().get(0).getId();
  }

  private Database createDatabase() {
    Database db =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Test Database")
                    .properties(p -> p.title(TITLE_PROP_NAME))
                    .build());
    if (db.getDataSources() == null || db.getDataSources().size() != 1) {
      throw new IllegalStateException(
          "After creating a database expected exactly one initial data source in the parent database");
    }
    return db;
  }

  @Test
  @DisplayName("IT-3: Data sources - Check create / update for all the properties supported")
  public void testDataSourceSchemaCrudOperations() {
    Map<String, DataSourcePropertySchema> rqProps =
        NotionSchema.schemaBuilder()
            .title("Name")
            .richText("Notes")
            .number("Score", NumberFormatType.NUMBER)
            .select("Category", "Alpha", "Beta", "Gamma")
            .multiSelect("Tags", "x", "y", "z")
            .status("Status")
            .date("Due")
            .checkbox("Done")
            .url("Link")
            .email("Contact")
            .phoneNumber("Phone")
            .people("Assignee")
            .files("Attachments")
            .relation("Single-Side Related", initialDataSourceId)
            .relation("Dual-Side Related", initialDataSourceId, "A relation")
            .rollup("Rollup", "Dual-Side Related", TITLE_PROP_NAME, RollupFunctionType.UNIQUE)
            .formula("Doubled Score", "prop(\"Score\") * 2")
            .place("Location")
            .createdTime("Created At")
            .createdBy("Created By")
            .lastEditedTime("Last Edited At")
            .lastEditedBy("Last Edited By")
            .uniqueId("ID")
            .build();

    DataSource ds =
        getNotionClient()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(parentDatabaseId)
                        .title("DS with Rich Schema")
                        .properties(rqProps)
                        .build());

    Map<String, DataSourcePropertySchema> rsProps = ds.getProperties();
    assertEquals(rqProps.size(), rsProps.size(), "Number of properties");

    // Check every property presence and type
    rqProps.forEach(
        (name, expected) -> {
          DataSourcePropertySchema actual = rsProps.get(name);
          assertNotNull(
              actual, name + " (" + expected.getType() + ") property is missing in response");
          assertEquals(expected.getType(), actual.getType(), name + " type mismatch");
        });

    DataSource initialDataSource = getNotionClient().dataSources().retrieve(initialDataSourceId);
    assertNotNull(
        initialDataSource.getProperties().get("A relation"),
        "A column for dual side relation is either missing or has name mismatch");
  }

  @AfterAll
  public static void tearDown() {}
}
