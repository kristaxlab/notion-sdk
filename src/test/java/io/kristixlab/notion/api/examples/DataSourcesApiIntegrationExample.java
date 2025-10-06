package io.kristixlab.notion.api.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristixlab.notion.api.endpoints.impl.DataSourcesEndpointImpl;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceRequest;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.datasources.properties.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for DataSourcesApi that calls actual Notion API endpoints. Stores all responses
 * to files for inspection.
 */
public class DataSourcesApiIntegrationExample extends IntegrationTest {

  private static final String DATABASE_ID = "24cc5b96-8ec4-800a-a809-c7f6508f45f2";
  private static final String DATA_SOURCE_ID = "24cc5b96-8ec4-808c-858f-000b4c20ad62";

  private DataSourcesEndpointImpl dataSourcesApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    dataSourcesApi = new DataSourcesEndpointImpl(getTransport());
  }

  @Test
  void retrieveDataSource() throws IOException {
    DataSource dataSource = dataSourcesApi.retrieve(DATA_SOURCE_ID);
    saveToFile(dataSource, "datasource-retrieve-response.json");
  }

  @Test
  void createDataSource() throws IOException {
    // Create a new data source
    CreateDataSourceRequest newDataSource = createDataSourceRequest();

    DataSource createdDataSource = dataSourcesApi.create(newDataSource);
    saveToFile(createdDataSource, "datasource-create-response.json");

    // Validate the created data source
    assertNotNull(createdDataSource);
    assertNotNull(createdDataSource.getId());
    assertEquals("data_source", createdDataSource.getObject());
    assertEquals("Test SDK Data Source", createdDataSource.getTitle().get(0).getPlainText());
    assertNotNull(createdDataSource.getParent());
    assertEquals("database_id", createdDataSource.getParent().getType());
    assertEquals(DATABASE_ID, createdDataSource.getParent().getDatabaseId());

    saveToFile(createdDataSource, "datasource-create-rs.json");
  }

  @Test
  void updateDataSource() throws IOException {
    DataSource request = new DataSource();
    Map<String, DataSourcePropertySchema> properties = new HashMap<>();
    request.setProperties(properties);

    // Update a number property to use Singapore dollar format
    NumberDataSourcePropertySchema numberProperty = new NumberDataSourcePropertySchema();
    numberProperty.setId("%60T%5D%5B");
    numberProperty.setNumber(new NumberDataSourcePropertySchema.NumberFormat());
    numberProperty.getNumber().setFormat("singapore_dollar");
    properties.put("Priority", numberProperty);

    saveToFile(request, "datasource-update-request.json");
    DataSource response = dataSourcesApi.update(DATA_SOURCE_ID, request);
    saveToFile(response, "datasource-update-response.json");
  }

  /**
   * Test archiving and restoring a data source. This will delete the data source and then restore
   * it. The responses are saved to files for inspection.
   */
  @Test
  void testArchiveAndRestoreDataSource() throws IOException {
    DataSource archivedDataSource = dataSourcesApi.delete(DATA_SOURCE_ID);
    saveToFile(archivedDataSource, "datasource-delete-response.json");

    DataSource unarchivedDataSource = dataSourcesApi.restore(DATA_SOURCE_ID);
    saveToFile(unarchivedDataSource, "datasource-restore-response.json");
  }

  /** Creates a data source request object for testing. */
  private CreateDataSourceRequest createDataSourceRequest() {
    CreateDataSourceRequest dataSource = new CreateDataSourceRequest();

    // Set parent to the specified database
    Parent parent = new Parent();
    parent.setType("database_id");
    parent.setDatabaseId(DATABASE_ID);
    dataSource.setParent(parent);

    // Set title
    RichText titleText = new RichText();
    titleText.setType("text");
    titleText.setPlainText("Test SDK Data Source");
    RichText.Text textContent = new RichText.Text();
    textContent.setContent("Test SDK Data Source");
    titleText.setText(textContent);

    dataSource.setTitle(List.of(titleText));

    // Create properties
    Map<String, DataSourcePropertySchema> properties = new HashMap<>();

    // Title property (required)
    TitleDataSourcePropertySchema titleProp = new TitleDataSourcePropertySchema();
    titleProp.setName("Name");
    titleProp.setType("title");
    properties.put("Name", titleProp);

    // Rich text property
    RichTextDataSourcePropertySchema textProp = new RichTextDataSourcePropertySchema();
    textProp.setName("Description");
    textProp.setType("rich_text");
    properties.put("Description", textProp);

    // Number property
    NumberDataSourcePropertySchema numberProp = new NumberDataSourcePropertySchema();
    numberProp.setType("number");
    NumberDataSourcePropertySchema.NumberFormat numberFormat =
        new NumberDataSourcePropertySchema.NumberFormat();
    numberFormat.setFormat("number");
    numberProp.setNumber(numberFormat);
    properties.put("Priority", numberProp);

    // Checkbox property
    CheckboxDataSourcePropertySchema checkboxProp = new CheckboxDataSourcePropertySchema();
    checkboxProp.setName("Completed");
    checkboxProp.setType("checkbox");
    properties.put("Completed", checkboxProp);

    // Select property with options
    SelectDataSourcePropertySchema selectProp = new SelectDataSourcePropertySchema();
    selectProp.setName("Status");
    selectProp.setType("select");
    SelectDataSourcePropertySchema.SelectConfig selectConfig =
        new SelectDataSourcePropertySchema.SelectConfig();

    selectConfig.setOptions(
        List.of(selectOption("option1", "red"), selectOption("option2", "blue")));
    selectProp.setSelect(selectConfig);
    properties.put("Status", selectProp);

    // Multi-select property
    MultiSelectDataSourcePropertySchema multiSelectProp = new MultiSelectDataSourcePropertySchema();
    multiSelectProp.setName("Tags");
    multiSelectProp.setType("multi_select");
    MultiSelectDataSourcePropertySchema.MultiSelectConfig multiSelectConfig =
        new MultiSelectDataSourcePropertySchema.MultiSelectConfig();
    multiSelectConfig.setSelectOptions(
        List.of(selectOption("Not Started", "red"), selectOption("In Progress", "yellow")));

    multiSelectProp.setMultiSelect(multiSelectConfig);
    properties.put("Tags", multiSelectProp);

    // Email property
    EmailDataSourcePropertySchema emailProp = new EmailDataSourcePropertySchema();
    emailProp.setName("Contact Email");
    emailProp.setType("email");
    emailProp.setEmail(new Object());
    properties.put("Contact Email", emailProp);

    // URL property
    UrlDataSourcePropertySchema urlProp = new UrlDataSourcePropertySchema();
    urlProp.setName("Website");
    urlProp.setType("url");
    properties.put("Website", urlProp);

    // Phone number property
    PhoneNumberDataSourcePropertySchema phoneProp = new PhoneNumberDataSourcePropertySchema();
    phoneProp.setName("Phone Number");
    phoneProp.setType("phone_number");
    properties.put("Phone Number", phoneProp);

    // Date property
    DateDataSourcePropertySchema dateProp = new DateDataSourcePropertySchema();
    dateProp.setName("Due Date");
    dateProp.setType("date");
    properties.put("Due Date", dateProp);

    // People property
    PeopleDataSourcePropertySchema peopleProp = new PeopleDataSourcePropertySchema();
    peopleProp.setName("Assigned To");
    peopleProp.setType("people");
    peopleProp.setPeople(new Object());
    properties.put("Assigned To", peopleProp);

    // Files property
    FilesDataSourcePropertySchema filesProp = new FilesDataSourcePropertySchema();
    filesProp.setName("Attachments");
    filesProp.setType("files");
    properties.put("Attachments", filesProp);

    dataSource.setProperties(properties);

    return dataSource;
  }

  private SelectOption selectOption(String option, String color) {
    SelectOption selectOption = new SelectOption();
    selectOption.setName(option);
    selectOption.setColor(color);
    return selectOption;
  }
}
