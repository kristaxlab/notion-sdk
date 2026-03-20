package integration.datasources;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.blocks.ParagraphBlock;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.UpdateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.properties.*;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageParams;
import io.kristixlab.notion.api.model.pages.properties.*;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;

public class DataSourcesPropertiesIT extends BaseIntegrationTest {

  private static String dataSourcePropertiesTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    dataSourcePropertiesTestsPageId =
        IntegrationTestAssisstant.createPageForTests("Data Source Properties");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(
            info.getDisplayName(), dataSourcePropertiesTestsPageId);
  }

  @Test
  @DisplayName(
      "[IT-53]: Data sources - Create a data source with all the supported properties and add pages to it")
  public void createDataSourceWithAllPropertiesAndAddPages() {
    // Setup: create a parent database; use its default data source
    Database db =
        getNotion()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(currTestPageId)
                    .title("Full property DB")
                    .description("Database to test all data source properties")
                    .isInline(true)
                    .build());
    String initialDsId = db.getDataSources().get(0).getId();

    // Step 1: Configure the data source with all supported user-editable properties
    DataSource dataSource =
        getNotion()
            .dataSources()
            .create(
                CreateDataSourceParams.builder()
                    .inDatabase(db.getId())
                    .title("Product catalog")
                    .properties(
                        schema ->
                            schema
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
                                .status("Status")
                                .relation("One-sided to First DS", initialDsId)
                                .relation("Dual to First DS", initialDsId, "Link to Second DS")
                                .rollup(
                                    "Rollup",
                                    RollupSchemaParams.builder()
                                        .relation("Dual to First DS")
                                        .property("Name")
                                        .calculate(RollupFunctionType.COUNT)
                                        .build()))
                    .build());

    assertNotNull(dataSource.getId());
    assertNotNull(dataSource.getProperties());

    Page pageInFirstDs =
        getNotion()
            .pages()
            .create(
                CreatePageParams.builder()
                    .inDatasource(initialDsId)
                    .title("Minimal entry")
                    .build());

    // Step 2: Add a page with all properties filled and with content
    Page fullPage =
        getNotion()
            .pages()
            .create(
                CreatePageParams.builder()
                    .inDatasource(dataSource.getId())
                    .title("Full entry")
                    .text("Description", "A detailed description")
                    .number("Number", 49.99)
                    .checkbox("Checkbox", true)
                    .date("Due Date", "2026-04-01")
                    .select("Category", "Electronics")
                    .multiSelect("Tags", SelectValue.of("Sale"), SelectValue.of("New"))
                    .url("Website", "https://example.com")
                    .email("Contact Email", "contact@example.com")
                    .phone("Contact Phone", "+1234567890")
                    .people("People", IntegrationTestAssisstant.getPrerequisites().getUserId())
                    .status("Status", "In progress")
                    .relation("Dual to First DS", pageInFirstDs.getId())
                    .files(
                        "Files",
                        FileData.fromExternalUrl(
                            "external-file",
                            IntegrationTestAssisstant.getPrerequisites().getExternalImageUrl()),
                        FileData.fromFileUpload(
                            IntegrationTestAssisstant.getPrerequisites().getImageFileUploadId()))
                    .children(
                        ParagraphBlock.of("First paragraph"), ParagraphBlock.of("Second paragraph"))
                    .build());
    
    assertNotNull(fullPage.getId());

    // Step 3: Update selected properties
    Page updatedPage =
        getNotion()
            .pages()
            .update(
                fullPage.getId(),
                UpdatePageParams.builder()
                    .title("Updated entry")
                    .number("Number", 59.99)
                    .select("Category", "Books")
                    .checkbox("Checkbox", false)
                    .email("Contact Email", "updated@example.com")
                    .url("Website", "https://updated.example.com")
                    .build());
    assertNotNull(updatedPage.getId());

    getNotion()
        .blocks()
        .appendChildren(
            fullPage.getId(),
            AppendBlockChildrenParams.of(List.of(ParagraphBlock.of("New paragraph added"))));
  }

  @Test
  @DisplayName("[IT-54]: Data sources - Check multiselect property consistency for existing pages")
  public void checkMultiselectConsistencyForExistingPages() {
    // Step 1: Create a datasource with Tags multi-select having 2 initial options
    Database db =
        getNotion()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(currTestPageId)
                    .title("Tag Consistency DB")
                    .isInline(true)
                    .properties(s -> s.title("Name").multiSelect("Tags", "Sale", "New"))
                    .build());
    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());
    String dsId = ds.getId();

    // Step 2: Add a page with "Sale" tag set
    Page page =
        getNotion()
            .pages()
            .create(
                CreatePageParams.builder()
                    .inDatasource(dsId)
                    .title("Test entry")
                    .multiSelect("Tags", SelectValue.of("Sale"))
                    .build());
    assertTagsExact(page, "Tags", "Sale");

    // Step 3: Add "Popular" and "Limited" to the Tags schema while preserving existing options
    MultiSelectSchema tagsSchema = (MultiSelectSchema) ds.getProperties().get("Tags");
    List<SelectOption> expandedOptions = new ArrayList<>(tagsSchema.getMultiSelect().getOptions());
    expandedOptions.add(SelectOption.of("Popular", Color.PURPLE));
    expandedOptions.add(SelectOption.of("Limited", Color.ORANGE));

    DataSource afterExpansion =
        getNotion()
            .dataSources()
            .update(
                dsId,
                UpdateDataSourceParams.fromProperty(
                    "Tags", MultiSelectSchemaParams.of(expandedOptions)));
    assertEquals(
        4,
        ((MultiSelectSchema) afterExpansion.getProperties().get("Tags"))
            .getMultiSelect()
            .getOptions()
            .size());

    // Step 4: Verify the page has not lost its original tag after the schema expansion
    Page pageAfterExpansion = getNotion().pages().retrieve(page.getId());
    assertTagsExact(pageAfterExpansion, "Tags", "Sale");

    // Step 5: Add "Popular" to the page; verify both "Sale" and "Popular" are present
    Page pageWithTwoTags =
        getNotion()
            .pages()
            .update(
                page.getId(),
                UpdatePageParams.builder()
                    .multiSelect("Tags", SelectValue.of("Sale"), SelectValue.of("Popular"))
                    .build());
    assertTagsExact(pageWithTwoTags, "Tags", "Sale", "Popular");

    // Step 6: Remove "Popular" from the Tags schema
    DataSource currentDs = getNotion().dataSources().retrieve(dsId);
    MultiSelectSchema currentTagsSchema = (MultiSelectSchema) currentDs.getProperties().get("Tags");
    List<SelectOption> optionsWithoutPopular =
        currentTagsSchema.getMultiSelect().getOptions().stream()
            .filter(o -> !"Popular".equals(o.getName()))
            .toList();

    UpdateDataSourceParams removeTagParams =
        UpdateDataSourceParams.fromProperty(
            "Tags", MultiSelectSchemaParams.of(optionsWithoutPopular));
    getNotion().dataSources().update(dsId, removeTagParams);

    // Step 7: Verify "Popular" is gone from the page; the original "Sale" tag is unaffected
    Page finalPage = getNotion().pages().retrieve(page.getId());
    assertTagsExact(finalPage, "Tags", "Sale");
  }

  @Test
  @DisplayName("[IT-55]: Data sources - Check status option add/remove via update")
  public void checkStatusUpdateCapabilities() {
    // Step 1: Create a datasource with a status property and a defined set of options.
    // Notion ignores group configuration on creation; all options land in the default "To-do"
    // group. Groups are read-only via the API and cannot be changed programmatically.
    var rq =
        CreateDatabaseParams.builder()
            .inPage(currTestPageId)
            .title("Status Test DB")
            .isInline(true)
            .properties(
                schema ->
                    schema
                        .title("Name")
                        .status(
                            "Workflow",
                            status ->
                                status
                                    .option("Backlog", Color.DEFAULT)
                                    .option("In Progress", Color.YELLOW)
                                    .option("Completed", Color.GREEN)));

    Database db = getNotion().databases().create(rq.build());

    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());
    StatusSchema statusSchema = ds.getProperties().get("Workflow").asStatus();
    assertNotNull(statusSchema, "Workflow status property should be present");
    assertNotNull(statusSchema.getStatus().getOptions(), "Notion should expose the options");
    assertStatusOptionsExact(statusSchema, "Backlog", "In Progress", "Completed");

    // Step 2: Add a new option by sending the full desired list (existing options + new one).
    // Notion keeps options that appear in the list and deletes any that are omitted.
    DataSource afterAdd =
        getNotion()
            .dataSources()
            .update(
                ds.getId(),
                UpdateDataSourceParams.builder()
                    .propertiesBuilder()
                    .status(
                        "Workflow",
                        StatusSchemaParams.builder(statusSchema.getStatus().getOptions())
                            .remove("Backlog")
                            .option("Cancelled", Color.RED) // new
                            .build())
                    .buildProperties()
                    .build());

    StatusSchema afterAddSchema = (StatusSchema) afterAdd.getProperties().get("Workflow");
    assertNotNull(afterAddSchema);
    assertStatusOptionsExact(afterAddSchema, "In Progress", "Completed", "Cancelled");
  }

  // ── Helpers ─────────────────────────────────────────────────────────────────

  private void assertTagsExact(Page page, String propertyName, String... tagNames) {
    MultiSelectProperty property = (MultiSelectProperty) page.getProperties().get(propertyName);
    assertNotNull(property, "Property '" + propertyName + "' not found on page");
    assertNotNull(
        property.getMultiSelect(), "Multi-select values are null for '" + propertyName + "'");
    Set<String> expected = new HashSet<>(Arrays.asList(tagNames));
    Set<String> actual =
        property.getMultiSelect().stream().map(SelectValue::getName).collect(Collectors.toSet());
    assertEquals(
        expected, actual, "Tags mismatch — expected exactly " + expected + " but got " + actual);
  }

  private void assertStatusOptionsExact(StatusSchema schema, String... optionNames) {
    List<StatusOption> options = schema.getStatus().getOptions();
    assertNotNull(options, "Status options are null");
    Set<String> expected = new HashSet<>(Arrays.asList(optionNames));
    Set<String> actual = options.stream().map(StatusOption::getName).collect(Collectors.toSet());
    assertEquals(
        expected,
        actual,
        "Status options mismatch — expected exactly " + expected + " but got " + actual);
  }
}
