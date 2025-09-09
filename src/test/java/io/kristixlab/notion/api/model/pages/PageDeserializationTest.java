package io.kristixlab.notion.api.model.pages;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristixlab.notion.api.model.pages.properties.*;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PageDeserializationTest {

  private static Page page;

  @BeforeAll
  static void init() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    InputStream is =
        PageDeserializationTest.class
            .getClassLoader()
            .getResourceAsStream("notion-json-examples/pages/pages-retrieve-rs.json");
    assertNotNull(is, "Test JSON file not found");
    page = mapper.readValue(is, Page.class);
    assertNotNull(page);
  }

  @Test
  void testBasicFields() {
    assertEquals("page", page.getObject());
    assertEquals("24cc5b96-8ec4-809c-9809-caa1d493ca04", page.getId());
    assertFalse(page.getArchived());
    assertFalse(page.getInTrash());
    assertNotNull(page.getParent());
    assertEquals("database_id", page.getParent().getType());
    assertEquals("24cc5b96-8ec4-800a-a809-c7f6508f45f2", page.getParent().getDatabaseId());
    assertNotNull(page.getCreatedBy());
    assertNotNull(page.getLastEditedBy());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", page.getCreatedBy().getId());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", page.getLastEditedBy().getId());
    assertEquals("2025-08-11T12:37:00.000Z", page.getCreatedTime());
    assertEquals("2025-08-11T12:51:00.000Z", page.getLastEditedTime());
    assertEquals(
        "https://www.notion.so/record-with-all-props-24cc5b968ec4809c9809caa1d493ca04",
        page.getUrl());
    assertEquals(
        "https://someurlweb.notion.site/record-with-all-props-24cc5b968ec4809c9809caa1d493ca04",
        page.getPublicUrl());
    assertEquals("92b86be9-43d2-40b7-bbb7-47a6d8fb7640", page.getRequestId());
  }

  @Test
  void testIconAndCover() {
    // all icon and cover types are tested in IconDeserializationTest and CoverDeserializationTest
    assertNotNull(page.getIcon());
    assertNotNull(page.getCover());
  }

  @Test
  void testRelationProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Relation"));
    PageProperty prop = page.getProperties().get("Relation");
    assertTrue(prop instanceof RelationProperty);
    RelationProperty relationProp = (RelationProperty) prop;
    assertEquals("%3AylC", relationProp.getId());
    assertEquals("relation", relationProp.getType());
    assertNotNull(relationProp.getRelation());
    assertEquals(3, relationProp.getRelation().size());
    assertEquals("24cc5b96-8ec4-81b8-b45f-eefc9156c148", relationProp.getRelation().get(0).getId());
    assertEquals("24cc5b96-8ec4-80e7-a620-ca389562e0cc", relationProp.getRelation().get(1).getId());
    assertEquals("24cc5b96-8ec4-8017-b930-cd188bb39fc1", relationProp.getRelation().get(2).getId());
  }

  @Test
  void testEmptyRelationProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Google Drive File"));
    PageProperty prop = page.getProperties().get("Google Drive File");
    assertTrue(prop instanceof RelationProperty);
    RelationProperty relationProp = (RelationProperty) prop;
    assertEquals("%3B%3CD%3A", relationProp.getId());
    assertEquals("relation", relationProp.getType());
    assertNotNull(relationProp.getRelation());
    assertEquals(0, relationProp.getRelation().size());
  }

  @Test
  void testStatusProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Status"));
    PageProperty prop = page.getProperties().get("Status");
    assertTrue(prop instanceof StatusProperty);
    StatusProperty statusProp = (StatusProperty) prop;
    assertEquals("%3BpBf", statusProp.getId());
    assertEquals("status", statusProp.getType());
    assertNotNull(statusProp.getStatus());
    assertEquals("20a08d39-867f-4638-9f3f-dec5221ff53e", statusProp.getStatus().getId());
    assertEquals("In progress", statusProp.getStatus().getName());
    assertEquals("blue", statusProp.getStatus().getColor());
  }

  @Test
  void testFormulaNumberProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Formula number"));
    PageProperty prop = page.getProperties().get("Formula number");
    assertTrue(prop instanceof FormulaProperty);
    FormulaProperty formulaProp = (FormulaProperty) prop;
    assertEquals("%3Crwj", formulaProp.getId());
    assertEquals("formula", formulaProp.getType());
    assertNotNull(formulaProp.getFormula());
    assertEquals("number", formulaProp.getFormula().getType());
    assertEquals(91.0, formulaProp.getFormula().getNumber());
  }

  @Test
  void testFormulaDateProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Formula date"));
    PageProperty prop = page.getProperties().get("Formula date");
    assertTrue(prop instanceof FormulaProperty);
    FormulaProperty formulaProp = (FormulaProperty) prop;
    assertEquals("%3Dy%5C%3D", formulaProp.getId());
    assertEquals("formula", formulaProp.getType());
    assertNotNull(formulaProp.getFormula());
    assertEquals("date", formulaProp.getFormula().getType());
    assertEquals("2025-08-11", formulaProp.getFormula().getDate().getStart());
    assertEquals("2025-08-12", formulaProp.getFormula().getDate().getEnd());
  }

  @Test
  void testButtonProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Button"));
    PageProperty prop = page.getProperties().get("Button");
    assertTrue(prop instanceof ButtonProperty);
    ButtonProperty buttonProp = (ButtonProperty) prop;
    assertEquals("%40gtF", buttonProp.getId());
    assertEquals("button", buttonProp.getType());
    assertNotNull(buttonProp.getButton());
  }

  @Test
  void testSelectProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Select"));
    PageProperty prop = page.getProperties().get("Select");
    assertTrue(prop instanceof SelectProperty);
    SelectProperty selectProp = (SelectProperty) prop;
    assertEquals("Ag%5Bt", selectProp.getId());
    assertEquals("select", selectProp.getType());
    assertNotNull(selectProp.getSelect());
    assertEquals("04a06ca9-8460-4c65-afa5-4781a8e86396", selectProp.getSelect().getId());
    assertEquals("option2", selectProp.getSelect().getName());
    assertEquals("red", selectProp.getSelect().getColor());
  }

  @Test
  void testCreatedTimeProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Created time"));
    PageProperty prop = page.getProperties().get("Created time");
    assertTrue(prop instanceof CreatedTimeProperty);
    CreatedTimeProperty createdTimeProp = (CreatedTimeProperty) prop;
    assertEquals("Bl%3FN", createdTimeProp.getId());
    assertEquals("created_time", createdTimeProp.getType());
    assertEquals("2025-08-11T12:37:00.000Z", createdTimeProp.getCreatedTime());
  }

  @Test
  void testUniqueIdProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("ID"));
    PageProperty prop = page.getProperties().get("ID");
    assertTrue(prop instanceof UniqueIdProperty);
    UniqueIdProperty uniqueIdProp = (UniqueIdProperty) prop;
    assertEquals("Bz%3C%5D", uniqueIdProp.getId());
    assertEquals("unique_id", uniqueIdProp.getType());
    assertNotNull(uniqueIdProp.getUniqueId());
    assertEquals("NST", uniqueIdProp.getUniqueId().getPrefix());
    assertEquals(1, uniqueIdProp.getUniqueId().getNumber());
  }

  @Test
  void testLastEditedTimeProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Last edited time"));
    PageProperty prop = page.getProperties().get("Last edited time");
    assertTrue(prop instanceof LastEditedTimeProperty);
    LastEditedTimeProperty lastEditedTimeProp = (LastEditedTimeProperty) prop;
    assertEquals("FlA%5D", lastEditedTimeProp.getId());
    assertEquals("last_edited_time", lastEditedTimeProp.getType());
    assertEquals("2025-08-11T12:51:00.000Z", lastEditedTimeProp.getLastEditedTime());
  }

  @Test
  void testUrlProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("URL"));
    PageProperty prop = page.getProperties().get("URL");
    assertTrue(prop instanceof UrlProperty);
    UrlProperty urlProp = (UrlProperty) prop;
    assertEquals("Nb%3Dg", urlProp.getId());
    assertEquals("url", urlProp.getType());
    assertEquals("https://notion.so", urlProp.getUrl());
  }

  @Test
  void testFormulaBooleanProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Formula boolean"));
    PageProperty prop = page.getProperties().get("Formula boolean");
    assertTrue(prop instanceof FormulaProperty);
    FormulaProperty formulaProp = (FormulaProperty) prop;
    assertEquals("QCl%3E", formulaProp.getId());
    assertEquals("formula", formulaProp.getType());
    assertNotNull(formulaProp.getFormula());
    assertEquals("boolean", formulaProp.getFormula().getType());
    assertTrue(formulaProp.getFormula().getBooleanValue());
  }

  @Test
  void testFormulaStringProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Formula string"));
    PageProperty prop = page.getProperties().get("Formula string");
    assertTrue(prop instanceof FormulaProperty);
    FormulaProperty formulaProp = (FormulaProperty) prop;
    assertEquals("R%5CdF", formulaProp.getId());
    assertEquals("formula", formulaProp.getType());
    assertNotNull(formulaProp.getFormula());
    assertEquals("string", formulaProp.getFormula().getType());
    assertEquals(
        "related rec 1\nrelated rec 2\nrelated rec 3", formulaProp.getFormula().getString());
  }

  @Test
  void testNumberProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Number"));
    PageProperty prop = page.getProperties().get("Number");
    assertTrue(prop instanceof NumberProperty);
    NumberProperty numberProp = (NumberProperty) prop;
    assertEquals("UhW%60", numberProp.getId());
    assertEquals("number", numberProp.getType());
    assertEquals(3.333333, numberProp.getNumber());
  }

  @Test
  void testCreatedByProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Created by"));
    PageProperty prop = page.getProperties().get("Created by");
    assertTrue(prop instanceof CreatedByProperty);
    CreatedByProperty createdByProp = (CreatedByProperty) prop;
    assertEquals("X%3BT%3A", createdByProp.getId());
    assertEquals("created_by", createdByProp.getType());
    assertNotNull(createdByProp.getCreatedBy());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", createdByProp.getCreatedBy().getId());
    assertEquals("userName", createdByProp.getCreatedBy().getName());
  }

  @Test
  void testEmailProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Email"));
    PageProperty prop = page.getProperties().get("Email");
    assertTrue(prop instanceof EmailProperty);
    EmailProperty emailProp = (EmailProperty) prop;
    assertEquals("Xke%7C", emailProp.getId());
    assertEquals("email", emailProp.getType());
    assertEquals("email@email.email", emailProp.getEmail());
  }

  @Test
  void testPhoneNumberProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Phone"));
    PageProperty prop = page.getProperties().get("Phone");
    assertTrue(prop instanceof PhoneNumberProperty);
    PhoneNumberProperty phoneProp = (PhoneNumberProperty) prop;
    assertEquals("YwOu", phoneProp.getId());
    assertEquals("phone_number", phoneProp.getType());
    assertEquals("+484848484848", phoneProp.getPhoneNumber());
  }

  @Test
  void testRollupCountUniqueProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Rollup count unique"));
    PageProperty prop = page.getProperties().get("Rollup count unique");
    assertTrue(prop instanceof RollupProperty);
    RollupProperty rollupProp = (RollupProperty) prop;
    assertEquals("ckez", rollupProp.getId());
    assertEquals("rollup", rollupProp.getType());
    assertNotNull(rollupProp.getRollup());
    assertEquals("number", rollupProp.getRollup().getType());
    assertEquals(3.0, rollupProp.getRollup().getNumber());
    assertEquals("unique", rollupProp.getRollup().getFunction());
  }

  @Test
  void testNumberPercentProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Number %"));
    PageProperty prop = page.getProperties().get("Number %");
    assertTrue(prop instanceof NumberProperty);
    NumberProperty numberProp = (NumberProperty) prop;
    assertEquals("d%3CGL", numberProp.getId());
    assertEquals("number", numberProp.getType());
    assertEquals(0.03, numberProp.getNumber());
  }

  @Test
  void testCheckboxProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Checkbox"));
    PageProperty prop = page.getProperties().get("Checkbox");
    assertTrue(prop instanceof CheckboxProperty);
    CheckboxProperty checkboxProp = (CheckboxProperty) prop;
    assertEquals("njnL", checkboxProp.getId());
    assertEquals("checkbox", checkboxProp.getType());
    assertFalse(checkboxProp.getCheckbox());
  }

  @Test
  void testDateProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Date"));
    PageProperty prop = page.getProperties().get("Date");
    assertTrue(prop instanceof DateProperty);
    DateProperty dateProp = (DateProperty) prop;
    assertEquals("nxRu", dateProp.getId());
    assertEquals("date", dateProp.getType());
    assertNotNull(dateProp.getDate());
    assertEquals("2025-08-11", dateProp.getDate().getStart());
    assertNull(dateProp.getDate().getEnd());
    assertNull(dateProp.getDate().getTimeZone());
  }

  @Test
  void testLastEditedByProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Last edited by"));
    PageProperty prop = page.getProperties().get("Last edited by");
    assertTrue(prop instanceof LastEditedByProperty);
    LastEditedByProperty lastEditedByProp = (LastEditedByProperty) prop;
    assertEquals("v%3CSK", lastEditedByProp.getId());
    assertEquals("last_edited_by", lastEditedByProp.getType());
    assertNotNull(lastEditedByProp.getLastEditedBy());
    assertEquals(
        "aaaaaaa2-cccc-eeee-9999-222222222222", lastEditedByProp.getLastEditedBy().getId());
    assertEquals("userName", lastEditedByProp.getLastEditedBy().getName());
  }

  @Test
  void testRollupShowOriginalProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Rollup show original"));
    PageProperty prop = page.getProperties().get("Rollup show original");
    assertTrue(prop instanceof RollupProperty);
    RollupProperty rollupProp = (RollupProperty) prop;
    assertEquals("vwZ%3A", rollupProp.getId());
    assertEquals("rollup", rollupProp.getType());
    assertNotNull(rollupProp.getRollup());
    assertEquals("array", rollupProp.getRollup().getType());
    assertEquals("show_original", rollupProp.getRollup().getFunction());
    assertNotNull(rollupProp.getRollup().getArray());
  }

  @Test
  void testRichTextProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Text"));
    PageProperty prop = page.getProperties().get("Text");
    assertTrue(prop instanceof RichTextProperty);
    RichTextProperty richTextProp = (RichTextProperty) prop;
    assertEquals("wgMV", richTextProp.getId());
    assertEquals("rich_text", richTextProp.getType());
    assertNotNull(richTextProp.getRichText());
    assertEquals(8, richTextProp.getRichText().size());
    assertEquals("we", richTextProp.getRichText().get(0).getPlainText());
    assertEquals("O(n)", richTextProp.getRichText().get(7).getPlainText());
  }

  @Test
  void testPeopleProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Person"));
    PageProperty prop = page.getProperties().get("Person");
    assertTrue(prop instanceof PeopleProperty);
    PeopleProperty peopleProp = (PeopleProperty) prop;
    assertEquals("x%5Bi_", peopleProp.getId());
    assertEquals("people", peopleProp.getType());
    assertNotNull(peopleProp.getPeople());
    assertEquals(2, peopleProp.getPeople().size());
    assertEquals("userName", peopleProp.getPeople().get(0).getName());
    assertEquals("Ania", peopleProp.getPeople().get(1).getName());
  }

  @Test
  void testMultiSelectProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Multi-select"));
    PageProperty prop = page.getProperties().get("Multi-select");
    assertTrue(prop instanceof MultiSelectProperty);
    MultiSelectProperty multiSelectProp = (MultiSelectProperty) prop;
    assertEquals("y%60nG", multiSelectProp.getId());
    assertEquals("multi_select", multiSelectProp.getType());
    assertNotNull(multiSelectProp.getMultiSelect());
    assertEquals(2, multiSelectProp.getMultiSelect().size());
    assertEquals("moption1", multiSelectProp.getMultiSelect().get(0).getName());
    assertEquals("brown", multiSelectProp.getMultiSelect().get(0).getColor());
    assertEquals("moption2", multiSelectProp.getMultiSelect().get(1).getName());
    assertEquals("red", multiSelectProp.getMultiSelect().get(1).getColor());
  }

  @Test
  void testDateWithReminderProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Date with reminder"));
    PageProperty prop = page.getProperties().get("Date with reminder");
    assertTrue(prop instanceof DateProperty);
    DateProperty dateProp = (DateProperty) prop;
    assertEquals("%7BvcQ", dateProp.getId());
    assertEquals("date", dateProp.getType());
    assertNotNull(dateProp.getDate());
    assertEquals("2025-08-14", dateProp.getDate().getStart());
    assertNull(dateProp.getDate().getEnd());
    assertNull(dateProp.getDate().getTimeZone());
  }

  @Test
  void testFilesProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Files & media"));
    PageProperty prop = page.getProperties().get("Files & media");
    assertTrue(prop instanceof FilesProperty);
    FilesProperty filesProp = (FilesProperty) prop;
    assertEquals("~wGd", filesProp.getId());
    assertEquals("files", filesProp.getType());
    assertNotNull(filesProp.getFiles());
    assertEquals(2, filesProp.getFiles().size());
    assertEquals("weekly_flower_tracker.png", filesProp.getFiles().get(0).getName());
    assertEquals("weekly_flower_tracker.pdf", filesProp.getFiles().get(1).getName());
  }

  @Test
  void testTitleProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Name"));
    PageProperty prop = page.getProperties().get("Name");
    assertTrue(prop instanceof TitleProperty);
    TitleProperty titleProp = (TitleProperty) prop;
    assertEquals("title", titleProp.getId());
    assertEquals("title", titleProp.getType());
    assertNotNull(titleProp.getTitle());
    assertEquals(1, titleProp.getTitle().size());
    assertEquals("record with all props", titleProp.getTitle().get(0).getPlainText());
  }

  @Test
  void testVerificationProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Verification"));
    PageProperty prop = page.getProperties().get("Verification");
    assertTrue(prop instanceof VerificationProperty);
    VerificationProperty verificationProp = (VerificationProperty) prop;
    assertEquals("verification", verificationProp.getId());
    assertEquals("verification", verificationProp.getType());
    assertNotNull(verificationProp.getVerification());
    assertEquals("unverified", verificationProp.getVerification().getState());
    assertNull(verificationProp.getVerification().getVerifiedBy());
    assertNull(verificationProp.getVerification().getDate());
  }

  @Test
  void testOwnerPeopleProperty() {
    assertNotNull(page.getProperties());
    assertTrue(page.getProperties().containsKey("Owner"));
    PageProperty prop = page.getProperties().get("Owner");
    assertTrue(prop instanceof PeopleProperty);
    PeopleProperty ownerProp = (PeopleProperty) prop;
    assertEquals("verification_owner", ownerProp.getId());
    assertEquals("people", ownerProp.getType());
    assertNotNull(ownerProp.getPeople());
    assertEquals(1, ownerProp.getPeople().size());
    assertEquals("userName", ownerProp.getPeople().get(0).getName());
  }
}
