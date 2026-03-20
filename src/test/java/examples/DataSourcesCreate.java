package examples;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.datasources.properties.*;
import io.kristixlab.notion.api.model.datasources.properties.NumberFormatType;
import io.kristixlab.notion.api.model.datasources.properties.helper.DataSourceSchemaBuilder;
import java.util.*;
import org.junit.jupiter.api.*;

@Tag("examples")
public class DataSourcesCreate extends BaseIntegrationTest {
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
}
