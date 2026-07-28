package integration.pages;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.NotionTstPageLogExtension;
import integration.extension.NotionTestPage;
import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.fluent.NotionPageViewer;
import io.kristaxlab.notion.fluent.NotionSchema;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.NumberFormatType;
import io.kristaxlab.notion.model.datasource.properties.RollupFunctionType;
import io.kristaxlab.notion.model.page.Page;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IT-6: Pages - Check CRUD for common properties")
public class Pages_IT6_UpdateProperties extends BaseIntegrationTest {

  private static final String TITLE_PROP_NAME = "Name";
  private static final String FILE_PATH = "files/it-7/image.jpg";
  private static final String FILE_NAME = "image.jpg";

  @NotionTestPage
  private static String testPageId;
  private static String firstDataSourceId;
  private static String secondDataSourceId;
  private static String fileUploadId;
  private static String userId;
  private static String anchorPageId;

  @BeforeAll
  public static void setup() {
    fileUploadId = uploadFile(FILE_PATH, FILE_NAME);
    NotionTstPageLogExtension.register(Pages_IT6_UpdateProperties.class, testPageId);

    Database db = createDatabaseWithFirstDataSource();
    firstDataSourceId = db.getDataSources().get(0).getId();
    secondDataSourceId = createSecondDataSource(db.getId());
    userId = IntegrationTestAssisstant.getPrerequisites().getUserId();

    // Create anchor page for relations
    anchorPageId = createAnchorPage(firstDataSourceId);
  }

  private static Database createDatabaseWithFirstDataSource() {
    return getSetupClient()
        .databases()
        .create(
            CreateDatabaseParams.builder()
                .inPage(testPageId)
                .title("Test Database")
                .properties(p -> p.title(TITLE_PROP_NAME))
                .build());
  }

  private static String createSecondDataSource(String databaseId) {
    Map<String, DataSourcePropertySchema> schema =
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
            .relation("Related", firstDataSourceId)
            .rollup("Count", "Related", TITLE_PROP_NAME, RollupFunctionType.UNIQUE)
            .formula("Doubled", "prop(\"Score\") * 2")
            .place("Location")
            .build();

    DataSource ds =
        getSetupClient()
            .dataSources()
            .create(r -> r.inDatabase(databaseId).title("Data Source").properties(schema));
    return ds.getId();
  }

  private static String createAnchorPage(String dataSourceId) {
    return getSetupClient()
        .pages()
        .create(
            p ->
                p.inDataSource(dataSourceId)
                    .properties(props -> props.title(TITLE_PROP_NAME, "Anchor")))
        .getId();
  }

  @Test
  @DisplayName("IT-6: Pages - Full CRUD cycle for common page properties")
  public void testPropertyCrud() {
    // Phase 1: CREATE with initial values
    Map<String, Object> phase1Props =
        Map.ofEntries(
            Map.entry("Name", "Initial Title"),
            Map.entry("Notes", "Initial notes"),
            Map.entry("Score", "10"),
            Map.entry("Category", "Alpha"),
            Map.entry("Tags", "x"),
            Map.entry("Status", "Not started"),
            Map.entry("Due", "2026-01-01"),
            Map.entry("Done", "true"),
            Map.entry("Link", "https://initial.com"),
            Map.entry("Contact", "initial@test.com"),
            Map.entry("Phone", "+1-111-1111"),
            Map.entry("Assignee_count", 1),
            Map.entry("Attachments_count", 1),
            Map.entry("Related_count", 1));

    Page page =
        getNotionClient()
            .pages()
            .create(
                p ->
                    p.inDataSource(secondDataSourceId)
                        .properties(
                            props ->
                                props
                                    .title("Name", "Initial Title")
                                    .richText("Notes", "Initial notes")
                                    .number("Score", 10)
                                    .select("Category", "Alpha")
                                    .multiSelect("Tags", "x")
                                    .status("Status", "Not started")
                                    .date("Due", LocalDate.of(2026, 1, 1))
                                    .checked("Done")
                                    .url("Link", "https://initial.com")
                                    .email("Contact", "initial@test.com")
                                    .phoneNumber("Phone", "+1-111-1111")
                                    .people("Assignee", userId)
                                    .files(
                                        "Attachments",
                                        FileData.builder().fileUpload(fileUploadId).build())
                                    .relation("Related", anchorPageId)
                                    .place("Location", 40.7128, -74.0060)));

    Map<String, Object> phase1Actual = extractPropertyValues(page);
    assertPropertyValues(phase1Props, phase1Actual);

    // Phase 2: UPDATE with new values
    Map<String, Object> phase2Props =
        Map.ofEntries(
            Map.entry("Name", "Updated Title"),
            Map.entry("Notes", "Updated notes"),
            Map.entry("Score", "99"),
            Map.entry("Category", "Beta"),
            Map.entry("Tags", "y, z"),
            Map.entry("Status", "In progress"),
            Map.entry("Due", "2026-12-31"),
            Map.entry("Done", "false"),
            Map.entry("Link", "https://updated.com"),
            Map.entry("Contact", "updated@test.com"),
            Map.entry("Phone", "+1-999-9999"));

    getNotionClient()
        .pages()
        .update(
            page.getId(),
            u ->
                u.properties(
                    props ->
                        props
                            .title("Name", "Updated Title")
                            .richText("Notes", "Updated notes")
                            .number("Score", 99)
                            .select("Category", "Beta")
                            .multiSelect("Tags", "y", "z")
                            .status("Status", "In progress")
                            .date("Due", LocalDate.of(2026, 12, 31))
                            .unchecked("Done")
                            .url("Link", "https://updated.com")
                            .email("Contact", "updated@test.com")
                            .phoneNumber("Phone", "+1-999-9999")
                            .place("Location", 51.5074, -0.1278)));

    Page updated = getNotionClient().pages().retrieve(page.getId());
    Map<String, Object> phase2Actual = extractPropertyValues(updated);
    assertPropertyValues(phase2Props, phase2Actual);

    // Phase 3: CLEAR properties (set to empty values where supported)
    Map<String, Object> phase3Props =
        Map.ofEntries(
            Map.entry("Name", "Updated Title"), // Cannot be cleared
            Map.entry("Notes", ""),
            Map.entry("Score", "99"), // Cannot be cleared
            Map.entry("Category", "Beta"), // Cannot be cleared
            Map.entry("Tags", ""),
            Map.entry("Done", "false"),
            Map.entry("Link", "https://updated.com"), // Cannot be cleared
            Map.entry("Contact", "updated@test.com"), // Cannot be cleared
            Map.entry("Phone", "+1-999-9999"), // Cannot be cleared
            Map.entry("Assignee_count", 0),
            Map.entry("Attachments_count", 0),
            Map.entry("Related_count", 0));

    getNotionClient()
        .pages()
        .update(
            page.getId(),
            u ->
                u.properties(
                    props ->
                        props
                            .richText("Notes", "")
                            .multiSelect("Tags")
                            .unchecked("Done")
                            .people("Assignee")
                            .files("Attachments")
                            .relation("Related")));

    Page cleared = getNotionClient().pages().retrieve(page.getId());
    Map<String, Object> phase3Actual = extractPropertyValues(cleared);
    assertPropertyValues(phase3Props, phase3Actual);
  }

  private Map<String, Object> extractPropertyValues(Page page) {
    NotionPageViewer viewer = NotionPageViewer.of(page);
    Map<String, Object> values = new java.util.HashMap<>();

    values.put("Name", viewer.propertyAsPlainText("Name"));
    values.put("Notes", viewer.propertyAsPlainText("Notes"));
    values.put("Score", viewer.propertyAsPlainText("Score"));
    values.put("Category", viewer.propertyAsPlainText("Category"));
    values.put("Tags", viewer.propertyAsPlainText("Tags"));
    values.put("Status", viewer.propertyAsPlainText("Status"));
    values.put("Due", viewer.propertyAsPlainText("Due"));
    values.put("Done", viewer.propertyAsPlainText("Done"));
    values.put("Link", viewer.propertyAsPlainText("Link"));
    values.put("Contact", viewer.propertyAsPlainText("Contact"));
    values.put("Phone", viewer.propertyAsPlainText("Phone"));
    values.put("Assignee_count", viewer.people("Assignee").size());
    values.put("Attachments_count", viewer.files("Attachments").size());
    values.put("Related_count", viewer.relation("Related").size());

    return values;
  }

  private void assertPropertyValues(Map<String, Object> expected, Map<String, Object> actual) {
    for (Map.Entry<String, Object> entry : expected.entrySet()) {
      String propertyName = entry.getKey();
      Object expectedValue = entry.getValue();
      Object actualValue = actual.get(propertyName);

      assertEquals(
          expectedValue, actualValue, String.format("Property '%s' mismatch", propertyName));
    }
  }
}
