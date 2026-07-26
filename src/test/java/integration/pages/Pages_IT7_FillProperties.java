package integration.pages;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.NotionIntegrationTestsExtension;
import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.fluent.NotionPageViewer;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionSchema;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.NumberFormatType;
import io.kristaxlab.notion.model.datasource.properties.RollupFunctionType;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.property.PlaceProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IT-7: Pages - Check create / update for all the properties supported")
public class Pages_IT7_FillProperties extends BaseIntegrationTest {

  private static final String TITLE_PROP_NAME = "Name";
  private static final String FILE_PATH = "files/it-7/image.jpg";
  private static final String FILE_NAME = "image.jpg";

  private static String testPageId;
  private static String firstDataSourceId;
  private static String secondDataSourceId;
  private static String fileUploadId;
  private static String userId;

  @BeforeAll
  public static void setup() {
    fileUploadId = uploadFile(FILE_PATH, FILE_NAME);

    testPageId = IntegrationTestAssisstant.createPageForTests("Data Sources - Basic");
    NotionIntegrationTestsExtension.register(Pages_IT7_FillProperties.class, testPageId);

    Database db = createDatabaseWithFirstDataSource();
    firstDataSourceId = db.getDataSources().get(0).getId();
    secondDataSourceId = createSecondDataSource(db.getId(), firstDataSourceId);
    userId = IntegrationTestAssisstant.getPrerequisites().getUserId();
  }

  private static Database createDatabaseWithFirstDataSource() {
    Database db =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(testPageId)
                    .title("IT-7 Test Database")
                    .properties(p -> p.title(TITLE_PROP_NAME))
                    .build());
    return db;
  }

  private static String createSecondDataSource(String databaseId, String relatedDataSourceId) {
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
            .relation("Single-Side Related", firstDataSourceId)
            .relation("Dual-Side Related", firstDataSourceId, "A relation")
            .rollup("Rollup", "Dual-Side Related", TITLE_PROP_NAME, RollupFunctionType.UNIQUE)
            .formula("Doubled Score", "prop(\"Score\") * 2")
            .place("Location")
            .build();

    DataSource ds =
        getSetupClient()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(databaseId)
                        .title("DS with Rich Schema")
                        .properties(rqProps)
                        .build());
    return ds.getId();
  }

  @Test
  @DisplayName("IT-7: Pages - Check create / update for all the properties supported")
  public void testDataSourceSchemaCrudOperations() {

    // Create an anchor row in the related data source so we can demonstrate the relation properties
    Page anchorPage =
        getNotionClient()
            .pages()
            .create(
                p ->
                    p.inDataSource(firstDataSourceId)
                        .properties(props -> props.title(TITLE_PROP_NAME, "Anchor")));

    Map<String, PageProperty> pageRqProps =
        NotionProperties.propertiesBuilder()
            .title("Name", "Full Row")
            .richText("Notes", "Integration test notes")
            .number("Score", 99)
            .select("Category", "Alpha")
            .multiSelect("Tags", "x", "y")
            .status("Status", "Not started")
            .date("Due", LocalDate.of(2025, 6, 30))
            .checked("Done")
            .url("Link", "https://example.com")
            .email("Contact", "test@example.com")
            .phoneNumber("Phone", "+1-555-0100")
            .people("Assignee", userId)
            .files("Attachments", FileData.builder().fileUpload(fileUploadId).build())
            .relation("Single-Side Related", anchorPage.getId())
            .relation("Dual-Side Related", anchorPage.getId())
            .place("Location", 50.0647, 19.9450)
            .build();

    Page page =
        getNotionClient()
            .pages()
            .create(
                p ->
                    p.inDataSource(secondDataSourceId)
                        .properties(props -> props.properties(pageRqProps))
                        .children(
                            c ->
                                c.bullet("First item").bullet("Second item").bullet("Third item")));

    // check property values
    NotionPageViewer viewer = NotionPageViewer.of(page);
    assertEquals("[IT-7] Full Row", viewer.propertyAsPlainText("Name"), "Name");
    assertEquals("Integration test notes", viewer.propertyAsPlainText("Notes"), "Notes");
    assertEquals("99", viewer.propertyAsPlainText("Score"), "Score");
    assertEquals("Alpha", viewer.propertyAsPlainText("Category"), "Category");
    assertEquals("x, y", viewer.propertyAsPlainText("Tags"), "Tags");
    assertEquals("Not started", viewer.propertyAsPlainText("Status"), "Status");
    assertEquals("2025-06-30", viewer.propertyAsPlainText("Due"), "Due");
    assertEquals("true", viewer.propertyAsPlainText("Done"), "Done");
    assertEquals("https://example.com", viewer.propertyAsPlainText("Link"), "Link");
    assertEquals("test@example.com", viewer.propertyAsPlainText("Contact"), "Contact");
    assertEquals("+1-555-0100", viewer.propertyAsPlainText("Phone"), "Phone");
    assertEquals(1, viewer.people("Assignee").size(), "Assignee list size mismatch");
    assertEquals(userId, viewer.people("Assignee").get(0).getId(), "Assignee");
    assertEquals(
        anchorPage.getId(),
        viewer.propertyAsPlainText("Single-Side Related"),
        "Single-Side Related");
    assertEquals(
        anchorPage.getId(), viewer.propertyAsPlainText("Dual-Side Related"), "Dual-Side Related");
    assertFalse(viewer.files("Attachments").isEmpty(), "Attachments");
    assertEquals(FILE_NAME, viewer.files("Attachments").get(0).getName());
    assertNotNull(viewer.property("Location", PlaceProperty.class), "Location place");
    assertNotNull(viewer.property("Location", PlaceProperty.class).getPlace(), "Location place");
    assertEquals(
        50.0647,
        viewer.property("Location", PlaceProperty.class).getPlace().getLat(),
        1e-4,
        "Location lat");
    assertEquals(
        19.9450,
        viewer.property("Location", PlaceProperty.class).getPlace().getLon(),
        1e-4,
        "Location lon");

    // check page content
    List<Block> children = getNotionClient().blocks().retrieveChildren(page.getId()).getResults();
    assertEquals(3, children.size(), "Page must have 3 bulleted list items");
    assertTrue(
        children.stream().allMatch(b -> "bulleted_list_item".equals(b.getType())),
        "All children must be bulleted list items");
  }

  @AfterAll
  public static void tearDown() {}
}
