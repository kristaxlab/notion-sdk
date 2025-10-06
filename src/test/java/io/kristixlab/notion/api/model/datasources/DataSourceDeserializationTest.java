package io.kristixlab.notion.api.model.datasources;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.datasources.properties.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for DataSource deserialization from JSON. Tests all data source property types and
 * their configurations.
 */
public class DataSourceDeserializationTest extends BaseTest {

  private static DataSource dataSource;

  @BeforeAll
  static void initDataSource() throws Exception {
    BaseTest.init();
    dataSource = loadFromFile("datasources/data-source-retrieve-rs.json", DataSource.class);
  }

  @Test
  void testBasicFields() {
    assertEquals("data_source", dataSource.getObject());
    assertEquals("24cc5b96-8ec4-808c-858f-000b4c20ad62", dataSource.getId());
    assertEquals("2025-08-11T12:37:00.000Z", dataSource.getCreatedTime());
    assertEquals("2025-09-04T19:26:00.000Z", dataSource.getLastEditedTime());
    assertEquals("d55d53c2-ff52-4ee5-95de-578312663346", dataSource.getRequestId());
    assertFalse(dataSource.getArchived());
    assertFalse(dataSource.getIsInline());
    assertEquals("https://www.notion.so/24cc5b968ec4800aa809c7f6508f45f2", dataSource.getUrl());
    assertEquals(
        "https://someurlweb.notion.site/24cc5b968ec4800aa809c7f6508f45f2",
        dataSource.getPublicUrl());
  }

  @Test
  void testTitleAndDescription() {
    // Test title
    assertNotNull(dataSource.getTitle());
    assertEquals(1, dataSource.getTitle().size());
    RichText titleText = dataSource.getTitle().get(0);
    assertEquals("text", titleText.getType());
    assertEquals("notion sdk // test database", titleText.getPlainText());
    assertFalse(titleText.getAnnotations().isBold());

    // Test description
    assertNotNull(dataSource.getDescription());
    assertEquals(3, dataSource.getDescription().size());

    RichText firstDesc = dataSource.getDescription().get(0);
    assertEquals("text", firstDesc.getType());
    assertEquals("database for testing purpose ", firstDesc.getPlainText());

    RichText secondDesc = dataSource.getDescription().get(1);
    assertEquals("mention", secondDesc.getType());
    assertEquals("2025-08-13", secondDesc.getPlainText());
    assertEquals("date", secondDesc.getMention().getType());
    assertEquals("2025-08-13", secondDesc.getMention().getDate().getStart());
  }

  @Test
  void testParent() {
    assertNotNull(dataSource.getParent());
    assertEquals("database_id", dataSource.getParent().getType());
    assertEquals("24cc5b96-8ec4-800a-a809-c7f6508f45f2", dataSource.getParent().getDatabaseId());
    assertNotNull(dataSource.getDatabaseParent());
    assertEquals("page_id", dataSource.getDatabaseParent().getType());
    assertEquals(
        "24cc5b96-8ec4-80d0-b579-f34b9e4d8339", dataSource.getDatabaseParent().getPageId());
  }

  @Test
  void testTitleProperty() {
    DataSourcePropertySchema titleProp = dataSource.getProperties().get("Name");
    assertNotNull(titleProp);
    assertTrue(titleProp.isTitle());
    assertEquals("title", titleProp.getId());
    assertEquals("Name", titleProp.getName());
    assertEquals("title", titleProp.getType());

    TitleDataSourcePropertySchema titleProperty = titleProp.asTitle();
    assertNotNull(titleProperty);
  }

  @Test
  void testRichTextProperty() {
    DataSourcePropertySchema textProp = dataSource.getProperties().get("Text");
    assertNotNull(textProp);
    assertTrue(textProp.isRichText());
    assertEquals("wgMV", textProp.getId());
    assertEquals("Text", textProp.getName());
    assertEquals("rich_text", textProp.getType());

    RichTextDataSourcePropertySchema richTextProperty = textProp.asRichText();
    assertNotNull(richTextProperty);
  }

  @Test
  void testNumberProperties() {
    // Test basic number property
    DataSourcePropertySchema numberProp = dataSource.getProperties().get("Number");
    assertNotNull(numberProp);
    assertTrue(numberProp.isNumber());
    assertEquals("UhW%60", numberProp.getId());
    assertEquals("Number", numberProp.getName());
    assertEquals("number", numberProp.getType());

    NumberDataSourcePropertySchema numberProperty = numberProp.asNumber();
    assertNotNull(numberProperty);
    assertNotNull(numberProperty.getNumber());
    assertEquals("number", numberProperty.getNumber().getFormat());

    // Test percentage number property
    DataSourcePropertySchema percentProp = dataSource.getProperties().get("Number %");
    assertNotNull(percentProp);
    assertTrue(percentProp.isNumber());
    NumberDataSourcePropertySchema percentProperty = percentProp.asNumber();
    assertEquals("percent", percentProperty.getNumber().getFormat());
  }

  @Test
  void testSelectProperty() {
    DataSourcePropertySchema selectProp = dataSource.getProperties().get("Select");
    assertNotNull(selectProp);
    assertTrue(selectProp.isSelect());
    assertEquals("Ag%5Bt", selectProp.getId());
    assertEquals("Select", selectProp.getName());
    assertEquals("select", selectProp.getType());

    SelectDataSourcePropertySchema selectProperty = selectProp.asSelect();
    assertNotNull(selectProperty);
    assertNotNull(selectProperty.getSelect());
    assertNotNull(selectProperty.getSelect().getOptions());
    assertEquals(2, selectProperty.getSelect().getOptions().size());

    SelectOption option1 = selectProperty.getSelect().getOptions().get(0);
    assertEquals("4f8830d2-5fa9-40ba-996e-55f460c92ee4", option1.getId());
    assertEquals("option1", option1.getName());
    assertEquals("orange", option1.getColor());

    SelectOption option2 = selectProperty.getSelect().getOptions().get(1);
    assertEquals("04a06ca9-8460-4c65-afa5-4781a8e86396", option2.getId());
    assertEquals("option2", option2.getName());
    assertEquals("red", option2.getColor());
  }

  @Test
  void testMultiSelectProperty() {
    DataSourcePropertySchema multiSelectProp = dataSource.getProperties().get("Multi-select");
    assertNotNull(multiSelectProp);
    assertTrue(multiSelectProp.isMultiSelect());
    assertEquals("y%60nG", multiSelectProp.getId());
    assertEquals("Multi-select", multiSelectProp.getName());
    assertEquals("multi_select", multiSelectProp.getType());

    MultiSelectDataSourcePropertySchema multiSelectProperty = multiSelectProp.asMultiSelect();
    assertNotNull(multiSelectProperty);
    assertNotNull(multiSelectProperty.getMultiSelect());
    assertNotNull(multiSelectProperty.getMultiSelect().getSelectOptions());
    assertEquals(2, multiSelectProperty.getMultiSelect().getSelectOptions().size());

    SelectOption option1 = multiSelectProperty.getMultiSelect().getSelectOptions().get(0);
    assertEquals("f9e31854-e3d3-4f6e-837f-d06e83b32427", option1.getId());
    assertEquals("moption1", option1.getName());
    assertEquals("brown", option1.getColor());
  }

  @Test
  void testStatusProperty() {
    DataSourcePropertySchema statusProp = dataSource.getProperties().get("Status");
    assertNotNull(statusProp);
    assertTrue(statusProp.isStatus());
    assertEquals("%3BpBf", statusProp.getId());
    assertEquals("Status", statusProp.getName());
    assertEquals("status", statusProp.getType());

    StatusDataSourcePropertySchema statusProperty = statusProp.asStatus();
    assertNotNull(statusProperty);
    assertNotNull(statusProperty.getStatus());

    // Test options
    assertNotNull(statusProperty.getStatus().getOptions());
    assertEquals(3, statusProperty.getStatus().getOptions().size());

    StatusDataSourcePropertySchema.StatusOption option1 = statusProperty.getStatus().getOptions().get(0);
    assertEquals("6760d97c-3591-4fae-978a-5c769ad64092", option1.getId());
    assertEquals("Not started", option1.getName());
    assertEquals("default", option1.getColor());

    // Test groups
    assertNotNull(statusProperty.getStatus().getGroups());
    assertEquals(3, statusProperty.getStatus().getGroups().size());

    StatusDataSourcePropertySchema.StatusGroup group1 = statusProperty.getStatus().getGroups().get(0);
    assertEquals("469f4651-6825-41a1-8958-e9b7c5e7bb85", group1.getId());
    assertEquals("To-do", group1.getName());
    assertEquals("gray", group1.getColor());
    assertEquals(1, group1.getOptionIds().size());
    assertEquals("6760d97c-3591-4fae-978a-5c769ad64092", group1.getOptionIds().get(0));
  }

  @Test
  void testRelationProperties() {
    // Test dual property relation
    DataSourcePropertySchema relationProp = dataSource.getProperties().get("Relation");
    assertNotNull(relationProp);
    assertTrue(relationProp.isRelation());
    assertEquals("%3AylC", relationProp.getId());
    assertEquals("Relation", relationProp.getName());
    assertEquals("relation", relationProp.getType());

    RelationDataSourcePropertySchema relationProperty = relationProp.asRelation();
    assertNotNull(relationProperty);
    assertNotNull(relationProperty.getRelation());
    assertEquals(
        "24cc5b96-8ec4-8087-aebc-f14c03858e6a", relationProperty.getRelation().getDatabaseId());
    assertEquals("dual_property", relationProperty.getRelation().getType());
    assertNotNull(relationProperty.getRelation().getDualProperty());
    assertEquals(
        "notion sdk // test database",
        relationProperty.getRelation().getDualProperty().getSyncedPropertyName());
    assertEquals("QaJw", relationProperty.getRelation().getDualProperty().getSyncedPropertyId());

    // Test single property relation
    DataSourcePropertySchema singleRelationProp = dataSource.getProperties().get("one side relation");
    assertNotNull(singleRelationProp);
    RelationDataSourcePropertySchema singleRelationProperty = singleRelationProp.asRelation();
    assertEquals("single_property", singleRelationProperty.getRelation().getType());
    assertNotNull(singleRelationProperty.getRelation().getSingleProperty());
  }

  @Test
  void testFormulaProperties() {
    // Test formula number
    DataSourcePropertySchema formulaNumberProp = dataSource.getProperties().get("Formula number");
    assertNotNull(formulaNumberProp);
    assertTrue(formulaNumberProp.isFormula());
    assertEquals("%3Crwj", formulaNumberProp.getId());
    assertEquals("Formula number", formulaNumberProp.getName());
    assertEquals("formula", formulaNumberProp.getType());

    FormulaDataSourcePropertySchema formulaProperty = formulaNumberProp.asFormula();
    assertNotNull(formulaProperty);
    assertNotNull(formulaProperty.getFormula());
    assertTrue(formulaProperty.getFormula().getExpression().contains("map"));
    assertTrue(formulaProperty.getFormula().getExpression().contains("sum()"));

    // Test formula date
    DataSourcePropertySchema formulaDateProp = dataSource.getProperties().get("Formula date");
    assertNotNull(formulaDateProp);
    FormulaDataSourcePropertySchema formulaDateProperty = formulaDateProp.asFormula();
    assertEquals(
        "dateRange(today(), today().dateAdd(1, \"days\"))",
        formulaDateProperty.getFormula().getExpression());

    // Test formula boolean
    DataSourcePropertySchema formulaBoolProp = dataSource.getProperties().get("Formula boolean");
    assertNotNull(formulaBoolProp);
    FormulaDataSourcePropertySchema formulaBoolProperty = formulaBoolProp.asFormula();
    assertTrue(formulaBoolProperty.getFormula().getExpression().contains("== false"));
  }

  @Test
  void testRollupProperties() {
    // Test rollup count unique
    DataSourcePropertySchema rollupProp = dataSource.getProperties().get("Rollup count unique");
    assertNotNull(rollupProp);
    assertTrue(rollupProp.isRollup());
    assertEquals("ckez", rollupProp.getId());
    assertEquals("Rollup count unique", rollupProp.getName());
    assertEquals("rollup", rollupProp.getType());

    RollupDataSourcePropertySchema rollupProperty = rollupProp.asRollup();
    assertNotNull(rollupProperty);
    assertNotNull(rollupProperty.getRollup());
    assertEquals("Relation", rollupProperty.getRollup().getRelationPropertyName());
    assertEquals(":ylC", rollupProperty.getRollup().getRelationPropertyId());
    assertEquals("Email", rollupProperty.getRollup().getRollupPropertyName());
    assertEquals("Xke|", rollupProperty.getRollup().getRollupPropertyId());
    assertEquals("unique", rollupProperty.getRollup().getFunction());

    // Test rollup show original
    DataSourcePropertySchema rollupShowProp = dataSource.getProperties().get("Rollup show original");
    assertNotNull(rollupShowProp);
    RollupDataSourcePropertySchema rollupShowProperty = rollupShowProp.asRollup();
    assertEquals("show_original", rollupShowProperty.getRollup().getFunction());
  }

  @Test
  void testSimpleProperties() {
    // Test checkbox
    DataSourcePropertySchema checkboxProp = dataSource.getProperties().get("Checkbox");
    assertNotNull(checkboxProp);
    assertTrue(checkboxProp.isCheckbox());
    assertEquals("njnL", checkboxProp.getId());
    assertEquals("Checkbox", checkboxProp.getName());
    assertEquals("checkbox", checkboxProp.getType());

    // Test email
    DataSourcePropertySchema emailProp = dataSource.getProperties().get("Email");
    assertNotNull(emailProp);
    assertTrue(emailProp.isEmail());
    assertEquals("Xke%7C", emailProp.getId());
    assertEquals("Email", emailProp.getName());
    assertEquals("email", emailProp.getType());

    // Test URL
    DataSourcePropertySchema urlProp = dataSource.getProperties().get("URL");
    assertNotNull(urlProp);
    assertTrue(urlProp.isUrl());
    assertEquals("Nb%3Dg", urlProp.getId());
    assertEquals("URL", urlProp.getName());
    assertEquals("url", urlProp.getType());

    // Test phone number
    DataSourcePropertySchema phoneProp = dataSource.getProperties().get("Phone");
    assertNotNull(phoneProp);
    assertTrue(phoneProp.isPhoneNumber());
    assertEquals("YwOu", phoneProp.getId());
    assertEquals("Phone", phoneProp.getName());
    assertEquals("phone_number", phoneProp.getType());

    // Test people
    DataSourcePropertySchema peopleProp = dataSource.getProperties().get("Person");
    assertNotNull(peopleProp);
    assertTrue(peopleProp.isPeople());
    assertEquals("x%5Bi_", peopleProp.getId());
    assertEquals("Person", peopleProp.getName());
    assertEquals("people", peopleProp.getType());

    // Test files
    DataSourcePropertySchema filesProp = dataSource.getProperties().get("Files & media");
    assertNotNull(filesProp);
    assertTrue(filesProp.isFiles());
    assertEquals("~wGd", filesProp.getId());
    assertEquals("Files & media", filesProp.getName());
    assertEquals("files", filesProp.getType());
  }

  @Test
  void testAutoGeneratedProperties() {
    // Test created time
    DataSourcePropertySchema createdTimeProp = dataSource.getProperties().get("Created time");
    assertNotNull(createdTimeProp);
    assertTrue(createdTimeProp.isCreatedTime());
    assertEquals("Bl%3FN", createdTimeProp.getId());
    assertEquals("Created time", createdTimeProp.getName());
    assertEquals("created_time", createdTimeProp.getType());

    // Test last edited time
    DataSourcePropertySchema lastEditedTimeProp = dataSource.getProperties().get("Last edited time");
    assertNotNull(lastEditedTimeProp);
    assertTrue(lastEditedTimeProp.isLastEditedTime());
    assertEquals("FlA%5D", lastEditedTimeProp.getId());
    assertEquals("Last edited time", lastEditedTimeProp.getName());
    assertEquals("last_edited_time", lastEditedTimeProp.getType());

    // Test created by
    DataSourcePropertySchema createdByProp = dataSource.getProperties().get("Created by");
    assertNotNull(createdByProp);
    assertTrue(createdByProp.isCreatedBy());
    assertEquals("X%3BT%3A", createdByProp.getId());
    assertEquals("Created by", createdByProp.getName());
    assertEquals("created_by", createdByProp.getType());

    // Test last edited by
    DataSourcePropertySchema lastEditedByProp = dataSource.getProperties().get("Last edited by");
    assertNotNull(lastEditedByProp);
    assertTrue(lastEditedByProp.isLastEditedBy());
    assertEquals("v%3CSK", lastEditedByProp.getId());
    assertEquals("Last edited by", lastEditedByProp.getName());
    assertEquals("last_edited_by", lastEditedByProp.getType());
  }

  @Test
  void testSpecialProperties() {
    // Test unique ID
    DataSourcePropertySchema uniqueIdProp = dataSource.getProperties().get("ID");
    assertNotNull(uniqueIdProp);
    assertTrue(uniqueIdProp.isUniqueId());
    assertEquals("Bz%3C%5D", uniqueIdProp.getId());
    assertEquals("ID", uniqueIdProp.getName());
    assertEquals("unique_id", uniqueIdProp.getType());

    UniqueIdDataSourcePropertySchema uniqueIdProperty = uniqueIdProp.asUniqueId();
    assertNotNull(uniqueIdProperty);
    assertNotNull(uniqueIdProperty.getUniqueId());
    assertEquals("NST", uniqueIdProperty.getUniqueId().getPrefix());

    // Test verification
    DataSourcePropertySchema verificationProp = dataSource.getProperties().get("Verification");
    assertNotNull(verificationProp);
    assertTrue(verificationProp.isVerification());
    assertEquals("verification", verificationProp.getId());
    assertEquals("Verification", verificationProp.getName());
    assertEquals("verification", verificationProp.getType());
  }

  @Test
  void testDateProperties() {
    // Test basic date
    DataSourcePropertySchema dateProp = dataSource.getProperties().get("Date");
    assertNotNull(dateProp);
    assertTrue(dateProp.isDate());
    assertEquals("nxRu", dateProp.getId());
    assertEquals("Date", dateProp.getName());
    assertEquals("date", dateProp.getType());

    DateDataSourcePropertySchema dateProperty = dateProp.asDate();
    assertNotNull(dateProperty);
    assertNotNull(dateProperty.getDate());

    // Test date with reminder
    DataSourcePropertySchema dateReminderProp = dataSource.getProperties().get("Date with reminder");
    assertNotNull(dateReminderProp);
    assertTrue(dateReminderProp.isDate());
    assertEquals("%7BvcQ", dateReminderProp.getId());
  }

  @Test
  void testPropertyCount() {
    // Verify we have all expected properties
    assertNotNull(dataSource.getProperties());
    assertEquals(36, dataSource.getProperties().size());
  }
}
