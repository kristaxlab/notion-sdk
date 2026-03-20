package integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.blocks.ParagraphBlock;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.databases.CreateDatabaseParams;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.databases.InitialDatasource;
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
    CreateDatabaseParams dbParams = new CreateDatabaseParams();
    dbParams.setParent(Parent.pageParent(currTestPageId));
    dbParams.setTitle(toRichTextList("Parent DB"));
    dbParams.setIsInline(true);

    dbParams.setInitialDataSource(new InitialDatasource());
    dbParams
        .getInitialDataSource()
        .setProperties(
            DataSourceSchemaBuilder.builder()
                .title("Name")
                .text("Description")
                .number("Price", NumberFormatType.ZLOTY)
                .date("Due Date")
                .build());

    Database db = getNotion().databases().create(dbParams);
    DataSource ds = getNotion().dataSources().retrieve(db.getDataSources().get(0).getId());
    assertPropertyPresent(ds, "Name", "Description", "Price", "Due Date");
    assertPropertyAbsent(ds, "Availability");
    assertPropertyType("rich_text", ds.getProperties().get("Description"));

    // Step 2: Update icon, title, number format (→ dollar), delete due date prop, etc.
    String emoji = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();

    Map<String, DataSourcePropertySchemaParams> updatedProps =
        DataSourceSchemaBuilder.builder()
            .rename("Name", "Title")
            .delete("Due Date")
            .number("Price", NumberFormatType.BAHT)
            .multiSelect("Description", "Book", "Magazine")
            .select("Availability", "In Stock", "Out of Stock")
            .build();

    UpdateDataSourceParams updateRequest = new UpdateDataSourceParams();
    updateRequest.setTitle(toRichTextList("Updated Product catalog"));
    updateRequest.setIcon(IconParams.fromEmoji(emoji));
    updateRequest.setProperties(updatedProps);

    DataSource updated = getNotion().dataSources().update(ds.getId(), updateRequest);
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
  @DisplayName(
      "[IT-53]: Data sources - Create a data source with all the supported properties and add pages to it")
  public void createDataSourceWithAllPropertiesAndAddPages() {

    DataSource ds = getNotion().dataSources().retrieve("956b5836-2c26-470a-a181-12351a81baab");

    // Setup: create a parent database; use its default data source
    CreateDatabaseParams dbParams = new CreateDatabaseParams();
    dbParams.setParent(Parent.pageParent(currTestPageId));
    dbParams.setTitle(toRichTextList("Full property DB"));
    dbParams.setIsInline(true);
    Database db = getNotion().databases().create(dbParams);
    String initialDsId = db.getDataSources().get(0).getId();

    // Step 1: Configure the data source with all supported user-editable properties

    CreateDataSourceParams secondDatasource = new CreateDataSourceParams();
    secondDatasource.setTitle(toRichTextList("Product catalog"));
    secondDatasource.setParent(Parent.databaseParent(db.getId()));

    // Step 1 — create with options only.
    // Notion ignores groups on creation and places all options in "To-do" automatically.
    // To reorganise options across groups, retrieve the data source after creation and
    // use StatusSchemaParams.editor(existingStatusSchema).group(...).option(...).build()
    secondDatasource.setProperties(
        DataSourceSchemaBuilder.builder()
            .title("Name")
            //            .text("Description")
            //            .number("Number", NumberFormatType.SINGAPORE_DOLLAR)
            //            .checkbox("Checkbox")
            //            .date("Due Date")
            //            .select("Category", "Electronics", "Books", "Clothing")
            //            .multiSelect("Tags", "Sale", "New", "Popular")
            //            .url("Website")
            //            .email("Contact Email")
            //            .phone("Contact Phone")
            //            .people("People")
            //            .uniqueId("UniqueId")
            //            .createdBy("Created By")
            //            .lastEditedBy("Last Edited By")
            //            .createdTime("Created Time")
            //            .lastEditedTime("Last Edited Time")
            //            .files("Files")
            //            .formula("Formula", "\"Title: \" + prop(\"Name\")")
            //            .place("Place")
            //            .relation("One-sided to First DS", initialDsId)
            .relation("Dual to First DS", initialDsId, "Link to Second DS")
            .rollup(
                "Rollup",
                RollupSchemaParams.builder()
                    .relation("Dual to First DS")
                    .property("Name")
                    .calculate(RollupFunctionType.COUNT)
                    .build())
            .status(
                "Status",
                StatusSchemaParams.builder()
                    .option("Idea",         Color.DEFAULT)
                    .option("Research",     Color.BLUE)
                    .option("On hold",      Color.GRAY)
                    .option("In progress",  Color.YELLOW)
                    .option("Verification", Color.PURPLE)
                    .option("Completed",    Color.GREEN)
                    .option("Cancelled",    Color.RED)
                    .build())
            .build());
    // DataSourcePropertySchema title = initialDatasource.getProperties().get("Name");
    // RollupSchemaParams.builder().rell(title.getId()).rollupName(title.getName())build();
    //    body.properties.Button.status should be defined, instead was `undefined`.
    //    body.properties.Button.relation should be defined, instead was `undefined`.
    //    body.properties.Button.rollup should be defined, instead was `undefined`.
    //    body.properties.Button.name should be defined, instead was `undefined`., Request ID:
    //
    DataSource dataSource = getNotion().dataSources().create(secondDatasource);
    assertNotNull(dataSource.getId());
    assertNotNull(dataSource.getProperties());

    // Step 2: Add an empty page (only title set)
    CreatePageParams emptyPageParams = new CreatePageParams();
    emptyPageParams.setParent(Parent.datasourceParent(initialDsId));
    emptyPageParams.setProperties(Map.of(TitleProperty.NAME, TitleProperty.of("Empty entry")));

    Page emptyPage = getNotion().pages().create(emptyPageParams);
    assertNotNull(emptyPage.getId());

    // Step 3: Add a page with all properties filled and with content
    Map<String, PageProperty> fullProps = new HashMap<>();
    fullProps.put(TitleProperty.NAME, TitleProperty.of("Full entry"));
    fullProps.put("Description", RichTextProperty.of("A detailed description"));
    fullProps.put("Number", NumberProperty.of(49.99));
    fullProps.put("Category", SelectProperty.of("Electronics", Color.BLUE));
    fullProps.put(
        "Tags",
        MultiSelectProperty.of(
            SelectValue.of("Sale", Color.RED), SelectValue.of("New", Color.GREEN)));
    fullProps.put("Due Date", DateProperty.of("2026-04-01"));
    fullProps.put("Checkbox", CheckboxProperty.checked());
    fullProps.put("Contact Email", EmailProperty.of("contact@example.com"));
    fullProps.put("Contact Phone", PhoneNumberProperty.of("+1234567890"));
    fullProps.put("Website", UrlProperty.of("https://example.com"));

    CreatePageParams fullPageParams = new CreatePageParams();
    fullPageParams.setParent(Parent.datasourceParent(initialDsId));
    fullPageParams.setProperties(fullProps);
    fullPageParams.setChildren(
        List.of(ParagraphBlock.of("First paragraph"), ParagraphBlock.of("Second paragraph")));

    Page fullPage = getNotion().pages().create(fullPageParams);
    assertNotNull(fullPage.getId());

    // Step 4: Update all properties and content for the second page
    Map<String, PageProperty> updatedProps = new HashMap<>();
    updatedProps.put(TitleProperty.NAME, TitleProperty.of("Updated entry"));
    updatedProps.put("Number", NumberProperty.of(59.99));
    updatedProps.put("Category", SelectProperty.of("Books", Color.YELLOW));
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

    // Step 5: Delete the second page
    Page deletedPage = getNotion().pages().delete(fullPage.getId());
    assertTrue(deletedPage.getInTrash());
  }

  private List<RichText> toRichTextList(String text) {
    RichText richText = RichText.builder().fromText(text).build();
    return List.of(richText);
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
}
