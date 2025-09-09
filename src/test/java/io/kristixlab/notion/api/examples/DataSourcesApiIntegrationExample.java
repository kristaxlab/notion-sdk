package io.kristixlab.notion.api.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristixlab.notion.api.DatasourcesApi;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.datasources.CreateDataSourceRequest;
import io.kristixlab.notion.api.model.datasources.Datasource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.kristixlab.notion.api.model.datasources.properties.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for DataSourcesApi that calls actual Notion API endpoints. Stores all responses
 * to files for inspection.
 */
public class DataSourcesApiIntegrationExample extends IntegrationTest {

  private static final String DATABASE_ID = "24cc5b96-8ec4-800a-a809-c7f6508f45f2";
  private static final String DATA_SOURCE_ID = "24cc5b96-8ec4-808c-858f-000b4c20ad62";

  private DatasourcesApi dataSourcesApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    dataSourcesApi = new DatasourcesApi(getTransport());
  }

  @Test
  void retrieveDataSource() throws IOException {
    Datasource dataSource = dataSourcesApi.retrieve(DATA_SOURCE_ID);
    saveToFile(dataSource, "datasource-retrieve-response.json");
  }

  @Test
  void createDataSource() throws IOException {
    // Create a new data source
    CreateDataSourceRequest newDataSource = createDataSourceRequest();

    Datasource createdDatasource = dataSourcesApi.create(newDataSource);
    saveToFile(createdDatasource, "datasource-create-response.json");

    // Validate the created data source
    assertNotNull(createdDatasource);
    assertNotNull(createdDatasource.getId());
    assertEquals("data_source", createdDatasource.getObject());
    assertEquals("Test SDK Data Source", createdDatasource.getTitle().get(0).getPlainText());
    assertNotNull(createdDatasource.getParent());
    assertEquals("database_id", createdDatasource.getParent().getType());
    assertEquals(DATABASE_ID, createdDatasource.getParent().getDatabaseId());

    saveToFile(createdDatasource, "datasource-create-rs.json");
  }

  @Test
  void updateDataSource() throws IOException {
    Datasource request = new Datasource();
    Map<String, DatasourceProperty> properties = new HashMap<>();
    request.setProperties(properties);

    // Update a number property to use Singapore dollar format
    NumberDatasourceProperty numberProperty = new NumberDatasourceProperty();
    numberProperty.setId("%60T%5D%5B");
    numberProperty.setNumber(new NumberDatasourceProperty.NumberFormat());
    numberProperty.getNumber().setFormat("singapore_dollar");
    properties.put("Priority", numberProperty);

    saveToFile(request, "datasource-update-request.json");
    Datasource response = dataSourcesApi.update(DATA_SOURCE_ID, request);
    saveToFile(response, "datasource-update-response.json");
  }

  /**
   * Test archiving and restoring a data source. This will delete the data source and then restore
   * it. The responses are saved to files for inspection.
   */
  @Test
  void testArchiveAndRestoreDataSource() throws IOException {
    Datasource archivedDatasource = dataSourcesApi.delete(DATA_SOURCE_ID);
    saveToFile(archivedDatasource, "datasource-delete-response.json");

    Datasource unarchivedDatasource = dataSourcesApi.restore(DATA_SOURCE_ID);
    saveToFile(unarchivedDatasource, "datasource-restore-response.json");
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
    properties.put("Completed", checkboxProp);

    // Select property with options
    SelectDatasourceProperty selectProp = new SelectDatasourceProperty();
    selectProp.setName("Status");
    selectProp.setType("select");
    SelectDatasourceProperty.SelectConfig selectConfig =
        new SelectDatasourceProperty.SelectConfig();

    selectConfig.setOptions(
        List.of(selectOption("option1", "red"), selectOption("option2", "blue")));
    selectProp.setSelect(selectConfig);
    properties.put("Status", selectProp);

    // Multi-select property
    MultiSelectDatasourceProperty multiSelectProp = new MultiSelectDatasourceProperty();
    multiSelectProp.setName("Tags");
    multiSelectProp.setType("multi_select");
    MultiSelectDatasourceProperty.MultiSelectConfig multiSelectConfig =
        new MultiSelectDatasourceProperty.MultiSelectConfig();
    multiSelectConfig.setSelectOptions(
        List.of(selectOption("Not Started", "red"), selectOption("In Progress", "yellow")));

    multiSelectProp.setMultiSelect(multiSelectConfig);
    properties.put("Tags", multiSelectProp);

    // Email property
    EmailDatasourceProperty emailProp = new EmailDatasourceProperty();
    emailProp.setName("Contact Email");
    emailProp.setType("email");
    emailProp.setEmail(new Object());
    properties.put("Contact Email", emailProp);

    // URL property
    UrlDatasourceProperty urlProp = new UrlDatasourceProperty();
    urlProp.setName("Website");
    urlProp.setType("url");
    properties.put("Website", urlProp);

    // Phone number property
    PhoneNumberDatasourceProperty phoneProp = new PhoneNumberDatasourceProperty();
    phoneProp.setName("Phone Number");
    phoneProp.setType("phone_number");
    properties.put("Phone Number", phoneProp);

    // Date property
    DateDatasourceProperty dateProp = new DateDatasourceProperty();
    dateProp.setName("Due Date");
    dateProp.setType("date");
    properties.put("Due Date", dateProp);

    // People property
    PeopleDatasourceProperty peopleProp = new PeopleDatasourceProperty();
    peopleProp.setName("Assigned To");
    peopleProp.setType("people");
    peopleProp.setPeople(new Object());
    properties.put("Assigned To", peopleProp);

    // Files property
    FilesDatasourceProperty filesProp = new FilesDatasourceProperty();
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
