package integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.properties.NumberFormatType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import notion.api.v1.NotionClient;
import notion.api.v1.model.common.RichTextType;
import notion.api.v1.model.databases.DatabaseParent;
import notion.api.v1.model.databases.DatabaseProperty;
import notion.api.v1.model.databases.DatabasePropertySchema;
import notion.api.v1.model.databases.DatePropertySchema;
import notion.api.v1.model.databases.MultiSelectPropertySchema;
import notion.api.v1.model.databases.NumberPropertySchema;
import notion.api.v1.model.databases.RichTextPropertySchema;
import notion.api.v1.model.databases.SelectOptionSchema;
import notion.api.v1.model.databases.SelectPropertySchema;
import notion.api.v1.model.databases.TitlePropertySchema;
import notion.api.v1.request.databases.CreateDatabaseRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * Side-by-side comparison of creating a Notion database using the community seratch/notion-sdk-jvm
 * library versus this SDK.
 *
 * <p>Both test pairs exercise the exact same API scenario so that the code-level difference is
 * immediately visible.
 */
@Tag("integration")
@DisplayName("SDK Comparison: seratch/notion-sdk-jvm vs kristix-notion-sdk")
public class SdkComparisonIT extends BaseIntegrationTest {

  private static String comparisonPageId;
  private String currTestPageId;

  @BeforeAll
  static void setup() {
    comparisonPageId = IntegrationTestAssisstant.createPageForTests("SDK Comparison");
  }

  @BeforeEach
  void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(info.getDisplayName(), comparisonPageId);
  }

  // ── COMP-1: Create a database with core property types ──────────────────

  /**
   * Creates a database with six properties using seratch/notion-sdk-jvm.
   *
   * <p>Observations:
   *
   * <ul>
   *   <li>Every plain string title requires a three-level wrapper chain: {@code
   *       DatabaseProperty.RichText → RichTextType → RichText.Text}.
   *   <li>Each property type needs an explicit schema object whose relationship to its inner config
   *       is non-obvious (e.g. {@code NumberPropertySchema(Number("zloty"))}).
   *   <li>Select/multi-select options cannot be specified as varargs; each option must be wrapped
   *       in {@code new SelectOptionSchema(name)}.
   *   <li>There is no fluent builder for the properties map; insertion order requires {@code
   *       LinkedHashMap} manually.
   * </ul>
   */
  @Test
  @DisplayName("[COMP-1A]: Create a database with core properties — seratch/notion-sdk-jvm")
  void createDatabase_seratch() {

    try (NotionClient notion = new NotionClient(System.getenv("TESTING_BOT_TOKEN"))) {

      // Title: every plain string needs a 3-level wrapper
      List<DatabaseProperty.RichText> title =
          List.of(
              new DatabaseProperty.RichText(
                  RichTextType.Text, new DatabaseProperty.RichText.Text("Full property DB")));
      Map<String, DatabasePropertySchema> properties = new LinkedHashMap<>();
      properties.put("Name", new TitlePropertySchema());
      properties.put("Description", new RichTextPropertySchema());
      properties.put("Price", new NumberPropertySchema(new NumberPropertySchema.Number("zloty")));
      properties.put("Due Date", new DatePropertySchema());
      properties.put(
          "Category",
          new SelectPropertySchema(
              List.of(
                  new SelectOptionSchema("Electronics"),
                  new SelectOptionSchema("Books"),
                  new SelectOptionSchema("Clothing"))));
      properties.put(
          "Tags",
          new MultiSelectPropertySchema(
              List.of(new SelectOptionSchema("Sale"), new SelectOptionSchema("New"))));

      notion.api.v1.model.databases.Database db =
          notion.createDatabase(
              new CreateDatabaseRequest(DatabaseParent.page(currTestPageId), title, properties));

      assertNotNull(db.getId());
      assertNotNull(db.getProperties().get("Name"));
      assertNotNull(db.getProperties().get("Description"));
      assertNotNull(db.getProperties().get("Price"));
      assertNotNull(db.getProperties().get("Due Date"));
      assertNotNull(db.getProperties().get("Category"));
      assertNotNull(db.getProperties().get("Tags"));
    }
  }

  /**
   * Creates the same database using this SDK.
   *
   * <p>Observations:
   *
   * <ul>
   *   <li>Plain strings are accepted everywhere — no wrapper builders.
   *   <li>Number format is expressed via a type-safe enum.
   *   <li>Select/multi-select options are plain varargs strings.
   *   <li>The schema builder is embedded directly in the request builder chain via {@code
   *       propertiesBuilder() … buildProperties()}.
   *   <li><strong>Design difference:</strong> this SDK separates {@link Database} from {@link
   *       DataSource}. Property schemas live on {@code DataSource}, not on {@code Database}, so one
   *       extra retrieval call is needed to inspect them. In seratch the database object itself
   *       carries its schema (matching Notion's raw API shape directly).
   * </ul>
   */
  @Test
  @DisplayName("[COMP-1B]: Create a database with core properties — kristix-notion-sdk")
  void createDatabase_kristixSdk() {

    Database db =
        getNotion()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .parentPage(currTestPageId)
                    .title("Full property DB")
                    .properties(
                        s ->
                            s.title("Name")
                                .text("Description")
                                .number("Price", NumberFormatType.ZLOTY)
                                .date("Due Date")
                                .select("Category", "Electronics", "Books", "Clothing")
                                .multiSelect("Tags", "Sale", "New"))
                    .build());

    // In this SDK the property schema lives on DataSource, not on Database.
    // One extra retrieval is needed — the trade-off for the explicit separation of concerns.
    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());

    assertNotNull(db.getId());
    assertNotNull(ds.getProperties().get("Name"));
    assertNotNull(ds.getProperties().get("Description"));
    assertNotNull(ds.getProperties().get("Price"));
    assertNotNull(ds.getProperties().get("Due Date"));
    assertNotNull(ds.getProperties().get("Category"));
    assertNotNull(ds.getProperties().get("Tags"));
  }
}
