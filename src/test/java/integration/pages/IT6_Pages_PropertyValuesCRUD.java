package integration.pages;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.extension.NotionTestContext;
import io.kristaxlab.notion.fluent.NotionPageViewer;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionSchema;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.NumberFormatType;
import io.kristaxlab.notion.model.datasource.properties.RollupFunctionType;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.NumberProperty;
import io.kristaxlab.notion.model.page.property.PageProperty;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT6_Pages_PropertyValuesCRUD extends BaseIntegrationTest {

  private static final String TITLE_PROP_NAME = "Name";
  private static final String FILE_PATH = "files/it-7/image.jpg";
  private static final String FILE_NAME = "image.jpg";

  private String firstDataSourceId;
  private String secondDataSourceId;
  private String fileUploadId;
  private String userId;
  private String anchorPageId1;
  private String anchorPageId2;

  @BeforeEach
  public void setup() {
    fileUploadId = uploadFile(FILE_PATH, FILE_NAME);

    String testPageId = getTestPageId();
    Database db = createDatabaseWithFirstDataSource(testPageId);
    firstDataSourceId = db.getDataSources().get(0).getId();
    secondDataSourceId = createSecondDataSource(db.getId());
    userId = NotionTestContext.getInstance().getTestBotUserId();

    // Create anchor page for relations
    anchorPageId1 = createAnchorPage(firstDataSourceId);
    anchorPageId2 = createAnchorPage(firstDataSourceId);
  }

  @Test
  @DisplayName("IT-6: Pages - Full CRUD cycle for all supported property values")
  public void testPropertyValuesCrud() {
    // Phase 1: CREATE with initial values
    Map<String, PageProperty> phase1Props =
        NotionProperties.builder()
            .title("Name", "Initial Title")
            .richText("Notes", "Initial notes")
            .number("Score", 10)
            .select("Category", "Alpha")
            .multiSelect("Tags", "x")
            .status("Status", "Not started")
            .date("Due", LocalDate.of(2026, 1, 1))
            .unchecked("Done")
            .url("Link", "https://initial.com")
            .email("Contact", "initial@test.com")
            .phoneNumber("Phone", "+1-111-1111")
            .people("Assignee", userId)
            .files("Attachments", FileData.builder().fileUpload(fileUploadId).build())
            .relation("Related", anchorPageId1)
            .place("Location", 40.7128, -74.0060)
            .build();

    Page created =
        getNotionClient()
            .pages()
            .create(p -> p.inDataSource(secondDataSourceId).properties(phase1Props));
    assertPropertyValues(phase1Props, created.getProperties());

    Page retrieved = getNotionClient().pages().retrieve(created.getId());
    assertEquals(created.getId(), retrieved.getId(), "Page ID mismatch after creation");
    assertPropertyValues(phase1Props, retrieved.getProperties());

    PageProperty scoreProp = getNotionClient().pages().retrieveProperty(created.getId(), "Score");
    assertNotNull(scoreProp, "Score property should not be null");
    assertInstanceOf(NumberProperty.class, scoreProp, "Score property should be a NumberProperty");
    assertEquals(
        phase1Props.get("Score").as(NumberProperty.class).getNumber(),
        scoreProp.as(NumberProperty.class).getNumber());

    // Phase 2: UPDATE with new values
    Map<String, PageProperty> phase2Props =
        NotionProperties.builder()
            .title("Name", "Updated Title")
            .richText("Notes", "Updated notes")
            .number("Score", 99)
            .select("Category", "Beta")
            .multiSelect("Tags", "y", "z")
            .status("Status", "In progress")
            .date("Due", LocalDate.of(2026, 12, 31))
            .checked("Done")
            .url("Link", "https://updated.com")
            .email("Contact", "updated@test.com")
            .phoneNumber("Phone", "+1-999-9999")
            .people("Assignee")
            .files("Attachments", FileData.builder().fileUpload(fileUploadId).build())
            .relation("Related", anchorPageId1, anchorPageId2)
            .place("Location", 51.5074, -0.1278)
            .build();

    Page updated =
        getNotionClient().pages().update(created.getId(), u -> u.properties(phase2Props));
    assertPropertyValues(phase2Props, updated.getProperties());

    // Phase 3: CLEAR properties (except Status as it may only be set with another value, never
    // empty)
    Map<String, PageProperty> phase3Props =
        NotionProperties.builder()
            .clearTitle("Name")
            .clearRichText("Notes")
            .clearNumber("Score")
            .clearSelect("Category")
            .clearMultiSelect("Tags")
            .clearDate("Due")
            .clearCheckbox("Done")
            .clearUrl("Link")
            .clearEmail("Contact")
            .clearPhoneNumber("Phone")
            .clearPeople("Assignee")
            .clearFiles("Attachments")
            .clearRelation("Related")
            .clearPlace("Location")
            .build();

    Page cleared =
        getNotionClient().pages().update(created.getId(), u -> u.properties(phase3Props));
    phase3Props.put(
        "Status",
        NotionProperties.status(
            "In progress")); // Status property cannot be fully cleared, set to null
    assertPropertyValues(phase3Props, cleared.getProperties());
  }

  private Map<String, Object> notmalizePropertyValues(Map<String, PageProperty> properties) {
    NotionPageViewer viewer = NotionPageViewer.of(properties);
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

  private void assertPropertyValues(
      Map<String, PageProperty> expectedProps, Map<String, PageProperty> actualProps) {
    Map<String, Object> expected = notmalizePropertyValues(expectedProps);
    Map<String, Object> actual = notmalizePropertyValues(actualProps);
    for (Map.Entry<String, Object> entry : expected.entrySet()) {
      String propertyName = entry.getKey();
      Object expectedValue = entry.getValue();
      Object actualValue = actual.get(propertyName);

      assertEquals(
          expectedValue, actualValue, String.format("Property '%s' mismatch", propertyName));
    }
  }

  // Setup methods

  private Database createDatabaseWithFirstDataSource(String testPageId) {
    return getSetupClient()
        .databases()
        .create(
            CreateDatabaseParams.builder()
                .inPage(testPageId)
                .title("Test Database")
                .properties(p -> p.title(TITLE_PROP_NAME))
                .build());
  }

  private String createSecondDataSource(String databaseId) {
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

  private String createAnchorPage(String dataSourceId) {
    return getSetupClient()
        .pages()
        .create(
            p ->
                p.inDataSource(dataSourceId)
                    .properties(props -> props.title(TITLE_PROP_NAME, "Anchor")))
        .getId();
  }
}
