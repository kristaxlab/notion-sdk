package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.CreateDataSourceParams;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.MultiSelectSchema;
import io.kristaxlab.notion.model.datasource.properties.NumberFormatType;
import io.kristaxlab.notion.model.datasource.properties.RelationSchema;
import io.kristaxlab.notion.model.datasource.properties.SelectSchema;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@link io.kristaxlab.notion.endpoints.DataSourcesEndpoint} — create group
 * (DS-1 through DS-7).
 *
 * <p>Each test is fully self-contained: it creates all data sources it needs inside {@link
 * #testDatabaseId}, which is provisioned once per test class in {@link #setup()}.
 *
 * <p><b>Test coverage in this class:</b>
 *
 * <ul>
 *   <li>DS-1 — Create with minimal schema (title column only)
 *   <li>DS-2+DS-6 — Create with rich multi-type schema, including all computed / read-only columns
 *       (DS-2 and DS-6 merged into one test)
 *   <li>DS-3 — Create with an emoji icon
 *   <li>DS-4 — Lambda {@code create(Consumer)} overload produces the same structure as the
 *       explicit-params overload
 *   <li>DS-7 — Create with a relation column pointing to another data source
 * </ul>
 */
public class DataSourcesIT extends BaseIntegrationTest {

  /** Database that acts as the parent container for all data sources created by this test class. */
  private static String testDatabaseId;

  @BeforeAll
  public static void setup() {
    String testRootPageId = IntegrationTestAssisstant.createPageForTests("Data Sources");
    Database db =
        IntegrationTestAssisstant.getNotion()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(testRootPageId)
                    .title("DS Test Container")
                    .isInline(true)
                    .build());
    testDatabaseId = db.getId();
  }

  // ===========================================================================
  // DS-1 — Create with minimal schema
  // ===========================================================================

  @Test
  @DisplayName("[DS-1]: DataSources - Create with minimal schema (title column only)")
  public void testCreateMinimalSchema() {
    DataSource ds =
        getNotion()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .title("[DS-1] Minimal")
                        .properties(s -> s.title("Name")));

    assertNotNull(ds.getId());
    assertEquals("data_source", ds.getObject());
    assertNotNull(ds.getDatabaseParent());
    assertEquals("database_id", ds.getDatabaseParent().getType());
    assertEquals(
        testDatabaseId.replace("-", ""), ds.getDatabaseParent().getDatabaseId().replace("-", ""));

    assertFalse(ds.getTitle().isEmpty(), "Title list must not be empty");
    assertEquals("[DS-1] Minimal", ds.getTitle().get(0).getPlainText());

    Map<String, DataSourcePropertySchema> props = ds.getProperties();
    assertTrue(props.containsKey("Name"), "Name column must be present");
    assertTrue("title".equals(props.get("Name").getType()), "Name column must be of type title");
  }

  // ===========================================================================
  // DS-2 + DS-6 — Rich multi-type schema (regular + computed/read-only columns)
  // ===========================================================================

  @Test
  @DisplayName(
      "[DS-2+DS-6]: DataSources - Create with rich multi-type schema including all computed and"
          + " read-only columns")
  public void testCreateRichSchema() {
    DataSource ds =
        getNotion()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .title("[DS-2+DS-6] Rich Schema")
                        .properties(
                            s ->
                                s
                                    // DS-2: standard / interactive columns
                                    .title("Name")
                                    .richText("Notes")
                                    .number("Score", NumberFormatType.NUMBER)
                                    .select("Category", "Alpha", "Beta", "Gamma")
                                    .multiSelect("Tags", "x", "y", "z")
                                    .status("State")
                                    .date("Due")
                                    .checkbox("Done")
                                    .url("Link")
                                    .email("Contact")
                                    .phoneNumber("Phone")
                                    .people("Assignee")
                                    .files("Attachments")
                                    // DS-6: computed / read-only columns
                                    .createdTime("Created At")
                                    .createdBy("Created By")
                                    .lastEditedTime("Last Edited At")
                                    .lastEditedBy("Last Edited By")
                                    .formula("Doubled Score", "prop(\"Score\") * 2")
                                    .uniqueId("ID", "DS")));

    Map<String, DataSourcePropertySchema> props = ds.getProperties();

    // --- DS-2: standard columns ---
    assertTrue("title".equals(props.get("Name").getType()), "Name must be title");
    assertTrue("rich_text".equals(props.get("Notes").getType()), "Notes must be rich_text");
    assertTrue("number".equals(props.get("Score").getType()), "Score must be number");

    assertTrue("select".equals(props.get("Category").getType()), "Category must be select");
    List<SelectSchema.SelectConfig> catConfig =
        List.of(props.get("Category").asSelect().getSelect());
    assertNotNull(catConfig.get(0).getOptions(), "Select options must be present");
    assertEquals(3, catConfig.get(0).getOptions().size(), "Category must have 3 options");
    assertTrue(
        catConfig.get(0).getOptions().stream().anyMatch(o -> "Alpha".equals(o.getName())),
        "Category must contain option Alpha");
    assertTrue(
        catConfig.get(0).getOptions().stream().anyMatch(o -> "Beta".equals(o.getName())),
        "Category must contain option Beta");
    assertTrue(
        catConfig.get(0).getOptions().stream().anyMatch(o -> "Gamma".equals(o.getName())),
        "Category must contain option Gamma");

    assertTrue("multi_select".equals(props.get("Tags").getType()), "Tags must be multi_select");
    MultiSelectSchema.MultiSelectConfig tagsConfig =
        props.get("Tags").asMultiSelect().getMultiSelect();
    assertNotNull(tagsConfig.getOptions(), "Multi-select options must be present");
    assertEquals(3, tagsConfig.getOptions().size(), "Tags must have 3 options");
    assertTrue(
        tagsConfig.getOptions().stream().anyMatch(o -> "x".equals(o.getName())),
        "Tags must contain option x");

    assertTrue("status".equals(props.get("State").getType()), "State must be status");
    assertTrue("date".equals(props.get("Due").getType()), "Due must be date");
    assertTrue("checkbox".equals(props.get("Done").getType()), "Done must be checkbox");
    assertTrue("url".equals(props.get("Link").getType()), "Link must be url");
    assertTrue("email".equals(props.get("Contact").getType()), "Contact must be email");
    assertTrue("phone_number".equals(props.get("Phone").getType()), "Phone must be phone_number");
    assertTrue("people".equals(props.get("Assignee").getType()), "Assignee must be people");
    assertTrue("files".equals(props.get("Attachments").getType()), "Attachments must be files");

    // --- DS-6: computed / read-only columns ---
    assertTrue(
        "created_time".equals(props.get("Created At").getType()),
        "Created At must be created_time");
    assertTrue(
        "created_by".equals(props.get("Created By").getType()), "Created By must be created_by");
    assertTrue(
        "last_edited_time".equals(props.get("Last Edited At").getType()),
        "Last Edited At must be last_edited_time");
    assertTrue(
        "last_edited_by".equals(props.get("Last Edited By").getType()),
        "Last Edited By must be last_edited_by");

    assertTrue(
        "formula".equals(props.get("Doubled Score").getType()), "Doubled Score must be formula");
    assertEquals(
        "prop(\"Score\") * 2",
        props.get("Doubled Score").asFormula().getFormula().getExpression(),
        "Formula expression must round-trip");

    assertTrue("unique_id".equals(props.get("ID").getType()), "ID must be unique_id");
    assertEquals(
        "DS",
        props.get("ID").asUniqueId().getUniqueId().getPrefix(),
        "unique_id prefix must be DS");
  }

  // ===========================================================================
  // DS-3 — Create with emoji icon
  // ===========================================================================

  @Test
  @DisplayName("[DS-3]: DataSources - Create with emoji icon")
  public void testCreateWithEmojiIcon() {
    String emoji = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();

    DataSource ds =
        getNotion()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .title("[DS-3] Icon")
                        .properties(s -> s.title("Name"))
                        .icon(Icon.emoji(emoji)));

    assertNotNull(ds.getIcon(), "Icon must not be null");
    assertEquals("emoji", ds.getIcon().getType(), "Icon type must be emoji");
    assertEquals(emoji, ds.getIcon().getEmoji(), "Emoji value must round-trip");
  }

  // ===========================================================================
  // DS-4 — Lambda overload vs. explicit-params overload
  // ===========================================================================

  @Test
  @DisplayName(
      "[DS-4]: DataSources - Lambda create(Consumer) overload produces same structure as explicit"
          + " params overload")
  public void testCreateLambdaOverloadMatchesExplicitParams() {
    DataSource fromParams =
        getNotion()
            .dataSources()
            .create(
                CreateDataSourceParams.builder()
                    .inDatabase(testDatabaseId)
                    .title("[DS-4] Explicit")
                    .properties(s -> s.title("Task").richText("Description"))
                    .build());

    DataSource fromLambda =
        getNotion()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .title("[DS-4] Lambda")
                        .properties(s -> s.title("Task").richText("Description")));

    // Both must produce valid, distinct data sources
    assertNotNull(fromParams.getId());
    assertNotNull(fromLambda.getId());
    assertNotEquals(
        fromParams.getId(), fromLambda.getId(), "Each call must yield a distinct data source");

    // Schema shape must be identical
    for (DataSource ds : List.of(fromParams, fromLambda)) {
      Map<String, DataSourcePropertySchema> props = ds.getProperties();
      assertTrue(props.containsKey("Task"), "Task column must be present");
      assertTrue("title".equals(props.get("Task").getType()), "Task column must be title type");
      assertTrue(props.containsKey("Description"), "Description column must be present");
      assertTrue(
          "rich_text".equals(props.get("Description").getType()),
          "Description column must be rich_text type");
    }
  }

  // ===========================================================================
  // DS-7 — Create with a relation column pointing to another data source
  // ===========================================================================

  @Test
  @DisplayName(
      "[DS-7]: DataSources - Create with a relation column referencing another data source")
  public void testCreateWithRelationColumn() {
    DataSource target =
        getNotion()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .title("[DS-7] Relation Target")
                        .properties(s -> s.title("Name")));

    DataSource source =
        getNotion()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .title("[DS-7] Relation Source")
                        .properties(s -> s.title("Name").relation("Related To", target.getId())));

    assertNotNull(source.getId());

    Map<String, DataSourcePropertySchema> props = source.getProperties();
    assertTrue(props.containsKey("Related To"), "Relation column must be present");
    assertTrue(
        "relation".equals(props.get("Related To").getType()), "Column type must be relation");

    RelationSchema rel = props.get("Related To").asRelation();
    assertNotNull(rel.getRelation(), "Relation config must not be null");
    assertEquals(
        target.getId().replace("-", ""),
        rel.getRelation().getDataSourceId().replace("-", ""),
        "Relation must reference the target data source id");
  }
}
