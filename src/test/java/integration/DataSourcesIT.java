package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.database.Database;
import io.kristaxlab.notion.model.datasource.CreateDataSourceParams;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.RelationSchema;
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
  // DS-3 — Create with emoji icon
  // ===========================================================================

  @Test
  @DisplayName("[DS-3]: DataSources - Create with emoji icon")
  public void testCreateWithEmojiIcon() {
    String emoji = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();

    DataSource ds =
        getNotionClient()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .dataSourceTitle("[DS-3] Icon")
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
        getNotionClient()
            .dataSources()
            .create(
                CreateDataSourceParams.builder()
                    .inDatabase(testDatabaseId)
                    .dataSourceTitle("[DS-4] Explicit")
                    .properties(s -> s.title("Task").richText("Description"))
                    .build());

    DataSource fromLambda =
        getNotionClient()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .dataSourceTitle("[DS-4] Lambda")
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
        getNotionClient()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .dataSourceTitle("[DS-7] Relation Target")
                        .properties(s -> s.title("Name")));

    DataSource source =
        getNotionClient()
            .dataSources()
            .create(
                r ->
                    r.inDatabase(testDatabaseId)
                        .dataSourceTitle("[DS-7] Relation Source")
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
