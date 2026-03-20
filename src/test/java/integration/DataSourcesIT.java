package integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.blocks.ParagraphBlock;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.UpdateDataSourceParams;
import io.kristixlab.notion.api.model.datasources.properties.*;
import io.kristixlab.notion.api.model.datasources.properties.NumberFormatType;
import io.kristixlab.notion.api.model.datasources.properties.helper.DataSourceSchemaBuilder;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.UpdatePageParams;
import io.kristixlab.notion.api.model.pages.properties.*;
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
                    .parentPage(currTestPageId)
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
                    .title("Updated Product catalog")
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
  public void exampleCreateDatabase() {
    String otherDsId = "some-existing-datasource-id";

    // ── Pattern A: standalone DataSourceSchemaBuilder (original) ───────────────
    CreateDatabaseParams dbParamsA =
        CreateDatabaseParams.builder()
            .parentPage(currTestPageId)
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
            .parentPage(currTestPageId)
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
            .parentPage(currTestPageId)
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
            .parentPage(currTestPageId)
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
                    .parentPage(currTestPageId)
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
                    .parentDatabase(db.getId())
                    .title("Product catalog")
                    .properties(
                        s ->
                            s.title("Name")
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
                                .relation("One-sided to First DS", initialDsId)
                                .relation("Dual to First DS", initialDsId, "Link to Second DS")
                                .rollup(
                                    "Rollup",
                                    RollupSchemaParams.builder()
                                        .relation("Dual to First DS")
                                        .property("Name")
                                        .calculate(RollupFunctionType.COUNT)
                                        .build())
                                .status("Status"))
                    .build());

    assertNotNull(dataSource.getId());
    assertNotNull(dataSource.getProperties());

    // Step 2: Add a page with all properties filled and with content
    Map<String, PageProperty> fullProps = new HashMap<>();
    fullProps.put(TitleProperty.NAME, TitleProperty.of("Full entry"));
    fullProps.put("Description", RichTextProperty.of("A detailed description"));
    fullProps.put("Number", NumberProperty.of(49.99));
    fullProps.put("Category", SelectProperty.of("Electronics"));
    fullProps.put("Tags", MultiSelectProperty.of(SelectValue.of("Sale"), SelectValue.of("New")));
    fullProps.put("Due Date", DateProperty.of("2026-04-01"));
    fullProps.put("Checkbox", CheckboxProperty.checked());
    fullProps.put("Contact Email", EmailProperty.of("contact@example.com"));
    fullProps.put("Contact Phone", PhoneNumberProperty.of("+1234567890"));
    fullProps.put("Website", UrlProperty.of("https://example.com"));

    CreatePageParams fullPageParams = new CreatePageParams();
    fullPageParams.setParent(Parent.datasourceParent(dataSource.getId()));
    fullPageParams.setProperties(fullProps);
    fullPageParams.setChildren(
        List.of(ParagraphBlock.of("First paragraph"), ParagraphBlock.of("Second paragraph")));

    Page fullPage = getNotion().pages().create(fullPageParams);
    assertNotNull(fullPage.getId());

    // Step 3: Update selected properties
    Map<String, PageProperty> updatedProps = new HashMap<>();
    updatedProps.put(TitleProperty.NAME, TitleProperty.of("Updated entry"));
    updatedProps.put("Number", NumberProperty.of(59.99));
    updatedProps.put("Category", SelectProperty.of("Books"));
    updatedProps.put("Checkbox", CheckboxProperty.unchecked());
    updatedProps.put("Contact Email", EmailProperty.of("updated@example.com"));
    updatedProps.put("Website", UrlProperty.of("https://updated.example.com"));

    UpdatePageParams updatePageParams = new UpdatePageParams();
    updatePageParams.setProperties(updatedProps);

    Page updatedPage = getNotion().pages().update(fullPage.getId(), updatePageParams);
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
                    .parentPage(currTestPageId)
                    .title("Tag Consistency DB")
                    .isInline(true)
                    .propertiesBuilder()
                    .title("Name")
                    .multiSelect("Tags", "Sale", "New")
                    .buildProperties()
                    .build());
    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());
    String dsId = ds.getId();

    // Step 2: Add a page with "Sale" tag set
    Map<String, PageProperty> createProps = new HashMap<>();
    createProps.put(TitleProperty.NAME, TitleProperty.of("Test entry"));
    createProps.put("Tags", MultiSelectProperty.of(SelectValue.of("Sale", Color.RED)));
    CreatePageParams pageParams = new CreatePageParams();
    pageParams.setParent(Parent.datasourceParent(dsId));
    pageParams.setProperties(createProps);
    Page page = getNotion().pages().create(pageParams);
    assertTagPresent(page, "Tags", "Sale");

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
                UpdateDataSourceParams.builder()
                    .properties(
                        s -> s.multiSelect("Tags", MultiSelectSchemaParams.of(expandedOptions)))
                    .build());
    assertEquals(
        4,
        ((MultiSelectSchema) afterExpansion.getProperties().get("Tags"))
            .getMultiSelect()
            .getOptions()
            .size());

    // Step 4: Verify the page has not lost its original tag after the schema expansion
    Page pageAfterExpansion = getNotion().pages().retrieve(page.getId());
    assertTagPresent(pageAfterExpansion, "Tags", "Sale");

    // Step 5: Add "Popular" to the page; verify both "Sale" and "Popular" are present
    Map<String, PageProperty> updateProps = new HashMap<>();
    updateProps.put(
        "Tags",
        MultiSelectProperty.of(
            SelectValue.of("Sale", Color.RED), SelectValue.of("Popular", Color.PURPLE)));
    UpdatePageParams updatePageParams = new UpdatePageParams();
    updatePageParams.setProperties(updateProps);
    Page pageWithTwoTags = getNotion().pages().update(page.getId(), updatePageParams);
    assertTagPresent(pageWithTwoTags, "Tags", "Sale");
    assertTagPresent(pageWithTwoTags, "Tags", "Popular");

    // Step 6: Remove "Popular" from the Tags schema
    DataSource currentDs = getNotion().dataSources().retrieve(dsId);
    MultiSelectSchema currentTagsSchema = (MultiSelectSchema) currentDs.getProperties().get("Tags");
    List<SelectOption> optionsWithoutPopular =
        currentTagsSchema.getMultiSelect().getOptions().stream()
            .filter(o -> !"Popular".equals(o.getName()))
            .toList();
    getNotion()
        .dataSources()
        .update(
            dsId,
            UpdateDataSourceParams.builder()
                .properties(
                    s -> s.multiSelect("Tags", MultiSelectSchemaParams.of(optionsWithoutPopular)))
                .build());

    // Step 7: Verify "Popular" is gone from the page; the original "Sale" tag is unaffected
    Page finalPage = getNotion().pages().retrieve(page.getId());
    assertTagAbsent(finalPage, "Tags", "Popular");
    assertTagPresent(finalPage, "Tags", "Sale");
  }

  @Test
  @DisplayName("[IT-55]: Data sources - Check status update capabilities")
  public void checkStatusUpdateCapabilities() {
    // Step 1: Create a datasource with preset status options with different colors
    Database db =
        getNotion()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .parentPage(currTestPageId)
                    .title("Status Test DB")
                    .isInline(true)
                    .propertiesBuilder()
                    .title("Name")
                    .status(
                        "Workflow",
                        StatusSchemaParams.builder()
                            .option("Backlog", Color.DEFAULT)
                            .option("Planning", Color.BLUE)
                            .option("In Progress", Color.YELLOW)
                            .option("In Review", Color.PURPLE)
                            .option("Completed", Color.GREEN)
                            .option("Cancelled", Color.RED)
                            .build())
                    .buildProperties()
                    .build());

    // The creation response already contains Notion-assigned IDs for options and groups
    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());

    StatusSchema statusSchema = (StatusSchema) ds.getProperties().get("Workflow");
    assertNotNull(statusSchema, "Workflow status property should be present");
    assertNotNull(
        statusSchema.getStatus().getGroups(), "Status groups should be present after creation");

    // Step 2: Assign options to their corresponding groups using the editor
    // (group IDs are now available from the live schema retrieved above)
    StatusSchemaParams reorganised =
        StatusSchemaParams.editor(statusSchema)
            .toDo()
            .option("Backlog")
            .option("Planning")
            .inProgress()
            .option("In Progress")
            .option("In Review")
            .completed()
            .option("Completed")
            .option("Cancelled")
            .build();

    DataSource updated =
        getNotion()
            .dataSources()
            .update(
                ds.getId(),
                UpdateDataSourceParams.builder()
                    .properties(s -> s.status("Workflow", reorganised))
                    .build());

    StatusSchema updatedSchema = (StatusSchema) updated.getProperties().get("Workflow");
    assertNotNull(updatedSchema, "Updated Workflow status property should be present");
    assertStatusOptionInGroup(updatedSchema, StatusSchemaParams.Editor.GROUP_TODO, "Backlog");
    assertStatusOptionInGroup(updatedSchema, StatusSchemaParams.Editor.GROUP_TODO, "Planning");
    assertStatusOptionInGroup(
        updatedSchema, StatusSchemaParams.Editor.GROUP_IN_PROGRESS, "In Progress");
    assertStatusOptionInGroup(
        updatedSchema, StatusSchemaParams.Editor.GROUP_IN_PROGRESS, "In Review");
    assertStatusOptionInGroup(
        updatedSchema, StatusSchemaParams.Editor.GROUP_COMPLETED, "Completed");
    assertStatusOptionInGroup(
        updatedSchema, StatusSchemaParams.Editor.GROUP_COMPLETED, "Cancelled");
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

  private void assertTagPresent(Page page, String propertyName, String tagName) {
    MultiSelectProperty property = (MultiSelectProperty) page.getProperties().get(propertyName);
    assertNotNull(property, "Property '" + propertyName + "' not found on page");
    assertNotNull(
        property.getMultiSelect(), "Multi-select values are null for '" + propertyName + "'");
    assertTrue(
        property.getMultiSelect().stream().anyMatch(v -> tagName.equals(v.getName())),
        "Tag '" + tagName + "' not found in '" + propertyName + "'");
  }

  private void assertTagAbsent(Page page, String propertyName, String tagName) {
    MultiSelectProperty property = (MultiSelectProperty) page.getProperties().get(propertyName);
    if (property == null || property.getMultiSelect() == null) return;
    assertFalse(
        property.getMultiSelect().stream().anyMatch(v -> tagName.equals(v.getName())),
        "Tag '" + tagName + "' should not be present in '" + propertyName + "'");
  }

  private void assertStatusOptionInGroup(StatusSchema schema, String groupName, String optionName) {
    List<StatusGroup> groups = schema.getStatus().getGroups();
    assertNotNull(groups, "Status groups are null");

    Optional<StatusGroup> group =
        groups.stream().filter(g -> groupName.equals(g.getName())).findFirst();
    assertTrue(group.isPresent(), "Group '" + groupName + "' not found in status schema");

    List<StatusOption> options = schema.getStatus().getOptions();
    assertNotNull(options, "Status options are null");

    Optional<StatusOption> option =
        options.stream().filter(o -> optionName.equals(o.getName())).findFirst();
    assertTrue(option.isPresent(), "Option '" + optionName + "' not found in status schema");

    List<String> optionIds = group.get().getOptionIds();
    assertNotNull(optionIds, "Group '" + groupName + "' has null optionIds");
    assertTrue(
        optionIds.contains(option.get().getId()),
        "Option '" + optionName + "' is not assigned to group '" + groupName + "'");
  }
}
