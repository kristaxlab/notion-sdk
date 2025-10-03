package io.kristixlab.notion.api.examples;

import io.kristixlab.notion.api.endpoints.impl.DatabasesEndpointImpl;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.databases.CreateDatabaseRequest;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.databases.InitialDatasource;
import io.kristixlab.notion.api.model.databases.UpdateDatabaseRequest;
import io.kristixlab.notion.api.model.datasources.properties.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for PagesApi that calls actual Notion API endpoints. Stores all responses to
 * files for inspection.
 */
public class DatabasesEndpointImplIntegrationExample extends IntegrationTest {

  private static final String PAGE_ID = "24cc5b96-8ec4-80d0-b579-f34b9e4d8339";
  private static final String DATABASE_ID = "24cc5b96-8ec4-800a-a809-c7f6508f45f2";

  private DatabasesEndpointImpl databasesEndpointImpl;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    databasesEndpointImpl = new DatabasesEndpointImpl(getTransport());
  }

  @Test
  void retrieveDatabase() throws IOException {
    Database database = databasesEndpointImpl.retrieve(DATABASE_ID);
    saveToFile(database, "database-retrieve-response.json");
  }

  @Test
  void createAndUpdateDatabase() throws IOException {
    // Create a new database with PAGE_ID as parent
    CreateDatabaseRequest newDatabase = createDatabaseRequest();
    saveToFile(newDatabase, "database-create-request.json");

    Database createdDatabase = databasesEndpointImpl.create(newDatabase);
    saveToFile(createdDatabase, "database-create-response.json");

    saveToFile(createdDatabase, "database-create-rs.json");

    UpdateDatabaseRequest updateRq = new UpdateDatabaseRequest();
    updateRq.setId(createdDatabase.getId());
    updateRq.setIsInline(false);

    Database updatedDatabase = databasesEndpointImpl.update(updateRq);
    saveToFile(updatedDatabase, "database-update-rs.json");
  }

  /**
   * Test archiving and restoring a page. This will delete the page and then restore it. The
   * responses are saved to files for inspection.
   */
  @Test
  void testArchiveAndRestorePage() throws IOException {
    Database archivedPage = databasesEndpointImpl.delete("24ec5b96-8ec4-8160-acb6-cd86d9585b58");
    saveToFile(archivedPage, "database-delete-response.json");

    Database unarchivedPage = databasesEndpointImpl.restore("24ec5b96-8ec4-8160-acb6-cd86d9585b58");
    saveToFile(unarchivedPage, "database-restore-response.json");
  }

  /** Creates a database request object for testing. */
  private CreateDatabaseRequest createDatabaseRequest() {
    CreateDatabaseRequest database = new CreateDatabaseRequest();

    database.setParent(Parent.pageParent(PAGE_ID));

    database.setTitle(RichText.asList("Test SDK Database with initial props"));
    database.setDescription(RichText.asList("Database created via SDK for testing purposes"));

    database.setInitialDataSource(InitialDatasource.of(properties()));
    database.setIsInline(true);
    return database;
  }

  private Map<String, DatasourceProperty> properties() {
    // Create properties
    Map<String, DatasourceProperty> properties = new HashMap<>();

    // Title property (required)
    TitleDatasourceProperty titleProp = new TitleDatasourceProperty();
    titleProp.setName("Name");
    titleProp.setType("title");
    properties.put("Name", titleProp);

    // Rich text property
    RichTextDatasourceProperty textProp = new RichTextDatasourceProperty();
    textProp.setName("Description");
    textProp.setType("rich_text");
    // Note: RichTextDatabaseProperty doesn't need additional configuration
    properties.put("Description", textProp);

    // Number property
    NumberDatasourceProperty numberProp = new NumberDatasourceProperty();
    numberProp.setType("number");
    NumberDatasourceProperty.NumberFormat numberFormat =
        new NumberDatasourceProperty.NumberFormat();
    numberFormat.setFormat("number");
    numberProp.setNumber(numberFormat);
    properties.put("Priority", numberProp);

    // Checkbox property
    CheckboxDatasourceProperty checkboxProp = new CheckboxDatasourceProperty();
    checkboxProp.setName("Completed");
    checkboxProp.setType("checkbox");
    // Note: CheckboxDatabaseProperty doesn't need additional configuration
    properties.put("Completed", checkboxProp);

    // Select property
    SelectDatasourceProperty selectProp = new SelectDatasourceProperty();
    selectProp.setType("select");

    List<SelectOption> options =
        List.of(
            selectOption("Not Started", "red"),
            selectOption("In Progress", "yellow"),
            selectOption("Done", "green"));

    selectProp.getSelect().setOptions(options);
    properties.put("Select", selectProp);
    return properties;
  }

  private SelectOption selectOption(String option, String color) {
    SelectOption selectOption = new SelectOption();
    selectOption.setName(option);
    selectOption.setColor(color);
    return selectOption;
  }
}
