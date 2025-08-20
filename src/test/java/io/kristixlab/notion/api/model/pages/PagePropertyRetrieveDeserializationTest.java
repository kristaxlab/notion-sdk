package io.kristixlab.notion.api.model.pages;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.pages.properties.NumberProperty;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.properties.PropertyItem;
import io.kristixlab.notion.api.model.pages.properties.list.*;
import io.kristixlab.notion.api.model.users.User;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for property retrieve responses using JSON examples. Tests deserialization of
 * different property types from Notion API responses.
 */
class PagePropertyRetrieveDeserializationTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }

  @Test
  void testNumberPropertyDeserialization() throws IOException {
    String jsonFile = "/notion-json-examples/pages-retrieve-property-number-rs.json";
    PageProperty property = loadPropertyFromJson(jsonFile);

    assertNotNull(property);
    assertEquals("property_item", property.getObject());
    assertEquals("number", property.getType());
    assertEquals("UhW%60", property.getId());
    assertNotNull(property.getRequestId());

    assertTrue(property.isNumber());

    assertTrue(property instanceof NumberProperty);
    NumberProperty numberProperty = property.asNumber();
    assertEquals(3.333333, numberProperty.getNumber(), 0.000001);

    // Test as* method
    NumberProperty asNumber = property.asNumber();
    assertNotNull(asNumber);
    assertEquals(3.333333, asNumber.getNumber(), 0.000001);

    // Test getter method
    assertEquals(3.333333, numberProperty.getNumber(), 0.000001);
  }

  @Test
  void testTitlePropertyDeserialization() throws IOException {
    String jsonFile = "/notion-json-examples/pages-retrieve-property-title-rs.json";
    PageProperty property = loadPagePropertyFromJson(jsonFile);

    assertNotNull(property);
    assertEquals("list", property.getObject());
    assertEquals("property_item", property.getType());
    assertNotNull(property.getRequestId());

    // Test results list
    assertNotNull(property.getResults());
    assertEquals(12, property.getResults().size()); // Based on the JSON file

    // Test first title item
    ListedPageProperty firstTitle = property.getResults().get(0);
    assertEquals("property_item", firstTitle.getObject());
    assertEquals("title", firstTitle.getType()); // Title property returns "title" type items
    assertEquals("title", firstTitle.getId());

    assertTrue(firstTitle.isTitle());
    assertFalse(firstTitle.isRichText());
    assertFalse(firstTitle.isPeople());
    assertFalse(firstTitle.isRelation());

    assertTrue(firstTitle instanceof ListedTitleProperty);
    ListedTitleProperty titleProperty = (ListedTitleProperty) firstTitle;

    RichText firstRichText = titleProperty.getTitle();
    assertNotNull(firstRichText);
    assertEquals("text", firstRichText.getType());
    assertEquals(
        "record with all props we have some text here with equation ",
        firstRichText.getPlainText());
    assertNotNull(firstRichText.getText());
    assertEquals(
        "record with all props we have some text here with equation ",
        firstRichText.getText().getContent());
    assertFalse(firstRichText.getAnnotations().isBold());
    assertFalse(firstRichText.getAnnotations().isCode());
    assertFalse(firstRichText.getAnnotations().isUnderline());
    assertFalse(firstRichText.getAnnotations().isStrikethrough());
    assertFalse(firstRichText.getAnnotations().isItalic());
    assertEquals("default", firstRichText.getAnnotations().getColor());

    // Test first title item
    ListedPageProperty mentionText = property.getResults().get(3).asTitle();
    assertEquals("property_item", mentionText.getObject());
    assertEquals("title", mentionText.getType()); // Title property returns "title" type items
    assertEquals("title", mentionText.getId());
    assertTrue(mentionText.isTitle());

    assertTrue(mentionText instanceof ListedTitleProperty);

    RichText mention = mentionText.asTitle().getTitle();
    assertNotNull(mention);
    assertEquals("mention", mention.getType());
    assertEquals("@userName", mention.getPlainText());
    assertNull(mention.getText());
    assertNull(mention.getHref());
    assertNotNull(mention.getMention());
    assertEquals("user", mention.getMention().getType());

    User user = mention.getMention().getUser();
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", user.getId());
    assertEquals("userName", user.getName());
    assertEquals("person", user.getType());
    assertEquals("https://s3-us-west-2...", user.getAvatarUrl());
    assertNotNull(user.getPerson());
    assertEquals("email1@gmail.com", user.getPerson().getEmail());

    assertNotNull(mention.getAnnotations());
    assertFalse(mention.getAnnotations().isBold());
    assertFalse(mention.getAnnotations().isCode());
    assertFalse(mention.getAnnotations().isUnderline());
    assertFalse(mention.getAnnotations().isStrikethrough());
    assertFalse(mention.getAnnotations().isItalic());
    assertEquals("default", mention.getAnnotations().getColor());
  }

  @Test
  void testRelationPropertyRetrieveDeserialization() throws IOException {
    String jsonFile = "/notion-json-examples/pages-retrieve-property-relation-rs.json";
    PageProperty property = loadPagePropertyFromJson(jsonFile);

    assertNotNull(property);
    assertEquals("list", property.getObject());
    assertEquals("property_item", property.getType());
    assertNotNull(property.getRequestId());
    assertEquals("8e4901fe-076e-4f3c-91f4-981e4952cf4a", property.getRequestId());

    // Test results list
    assertNotNull(property.getResults());
    assertEquals(3, property.getResults().size()); // Based on the JSON file

    // Test first relation item
    ListedPageProperty firstRelation = property.getResults().get(0);
    assertEquals("property_item", firstRelation.getObject());
    assertEquals("relation", firstRelation.getType());
    assertEquals("%3AylC", firstRelation.getId());

    assertTrue(firstRelation.isRelation());
    assertFalse(firstRelation.isTitle());
    assertFalse(firstRelation.isPeople());
    assertFalse(firstRelation.isRichText());

    assertTrue(firstRelation instanceof ListedRelationProperty);

    assertEquals(
        "24cc5b96-8ec4-81b8-b45f-eefc9156c148", firstRelation.asRelation().getRelation().getId());
    assertEquals(
        "24cc5b96-8ec4-80e7-a620-ca389562e0cc",
        property.getResults().get(1).asRelation().getRelation().getId());
    assertEquals(
        "24cc5b96-8ec4-8017-b930-cd188bb39fc1",
        property.getResults().get(2).asRelation().getRelation().getId());
  }

  @Test
  void testPersonPropertyRetrieveDeserialization() throws IOException {
    String jsonFile = "/notion-json-examples/pages-retrieve-property-person-rs.json";
    PageProperty property = loadPagePropertyFromJson(jsonFile);

    assertNotNull(property);
    assertEquals("list", property.getObject());
    assertEquals("property_item", property.getType());
    assertNotNull(property.getRequestId());

    // Test results list
    assertNotNull(property.getResults());
    assertEquals(2, property.getResults().size()); // Based on the JSON file

    // Test first person item
    ListedPageProperty firstPerson = property.getResults().get(0);
    assertEquals("property_item", firstPerson.getObject());
    assertEquals("people", firstPerson.getType());
    assertEquals("x%5Bi_", firstPerson.getId());

    assertTrue(firstPerson.isPeople());
    assertFalse(firstPerson.isTitle());
    assertFalse(firstPerson.isRelation());
    assertFalse(firstPerson.isRichText());

    assertTrue(firstPerson instanceof ListedPeopleProperty);
    ListedPeopleProperty peopleProperty = firstPerson.asPeople();

    User firstUser = peopleProperty.getPeople();
    assertNotNull(firstUser);
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", firstUser.getId());
    assertEquals("userName", firstUser.getName());
    assertEquals("person", firstUser.getType());
    assertEquals("email1@gmail.com", firstUser.getPerson().getEmail());
    assertNotNull(firstUser.getAvatarUrl());
    assertTrue(firstUser.getAvatarUrl().contains("avatartion_1_y.png"));

    // Test second person item
    ListedPageProperty secondPerson = property.getResults().get(1);
    assertEquals("property_item", secondPerson.getObject());
    assertEquals("people", secondPerson.getType());
    assertEquals("x%5Bi_", secondPerson.getId());

    assertTrue(secondPerson instanceof ListedPeopleProperty);
    ListedPeopleProperty secondPeopleProperty = (ListedPeopleProperty) secondPerson;

    User secondUser = secondPeopleProperty.getPeople();
    assertNotNull(secondUser);
    assertEquals("d0519c83-74a0-4f52-b552-7304507a099f", secondUser.getId());
    assertEquals("Ania", secondUser.getName());
    assertEquals("person", secondUser.getType());
    assertEquals("email2@gmail.com", secondUser.getPerson().getEmail());
    assertNotNull(secondUser.getAvatarUrl());
    assertTrue(secondUser.getAvatarUrl().contains("avatartion_2_g.png"));

    // Test as* method
    ListedPeopleProperty asPeople = firstPerson.asPeople();
    assertNotNull(asPeople);
    assertEquals("userName", asPeople.getPeople().getName());

    // Verify property item data
    assertTrue(property.isPropertyItem());
    assertTrue(property instanceof PropertyItem);
    assertNotNull(property.asPropertyItem().getPropertyItem());
    assertEquals("x%5Bi_", property.asPropertyItem().getPropertyItem().getId());
    assertEquals("people", property.asPropertyItem().getPropertyItem().getType());
    assertNull(property.asPropertyItem().getPropertyItem().getNextUrl());

    // Test pagination info
    assertNull(property.getNextCursor());
    assertFalse(property.hasMore());
  }

  @Test
  void testRichTextPropertyRetrieveDeserialization() throws IOException {
    String jsonFile = "/notion-json-examples/pages-retrieve-property-richtext-rs.json";
    PageProperty property = loadPagePropertyFromJson(jsonFile);

    assertNotNull(property);
    assertEquals("list", property.getObject());
    assertEquals("property_item", property.getType());
    assertNotNull(property.getRequestId());

    // Test results list
    assertNotNull(property.getResults());
    assertTrue(property.getResults().size() > 0);

    // Test first rich text item
    ListedPageProperty firstRichText = property.getResults().get(0);
    assertEquals("property_item", firstRichText.getObject());
    assertEquals("rich_text", firstRichText.getType());
    assertEquals("wgMV", firstRichText.getId());

    assertTrue(firstRichText.isRichText());
    assertFalse(firstRichText.isTitle());
    assertFalse(firstRichText.isPeople());
    assertFalse(firstRichText.isTitle());

    assertTrue(firstRichText instanceof ListedRichTextProperty);
    ListedRichTextProperty richTextProperty = firstRichText.asRichText();

    RichText firstRichTextContent = richTextProperty.getRichText();
    assertNotNull(firstRichTextContent);
    assertEquals("text", firstRichTextContent.getType());
    assertEquals("we", firstRichTextContent.getPlainText());
    assertNotNull(firstRichTextContent.getAnnotations());
    assertTrue(firstRichTextContent.getAnnotations().isCode());
    assertFalse(firstRichTextContent.getAnnotations().isBold());
    assertFalse(firstRichTextContent.getAnnotations().isItalic());
    assertEquals("default", firstRichTextContent.getAnnotations().getColor());

    // Test as* method
    ListedRichTextProperty asRichText = firstRichText.asRichText();
    assertNotNull(asRichText);
    assertEquals("we", asRichText.getRichText().getPlainText());

    // Verify property item data
    assertNotNull(property.asPropertyItem().getPropertyItem());
    assertEquals("wgMV", property.asPropertyItem().getPropertyItem().getId());
    assertEquals("rich_text", property.asPropertyItem().getPropertyItem().getType());
    assertNull(property.asPropertyItem().getPropertyItem().getNextUrl());

    // Test pagination info
    assertNull(property.getNextCursor());
    assertFalse(property.hasMore());
  }

  /** /** Helper method to load a PagePropertyItem from JSON file. */
  private PageProperty loadPropertyFromJson(String jsonFile) throws IOException {
    try (InputStream inputStream = getClass().getResourceAsStream(jsonFile)) {
      assertNotNull(inputStream, "JSON file not found: " + jsonFile);
      return objectMapper.readValue(inputStream, PageProperty.class);
    }
  }

  /** Helper method to load a PageProperty from JSON file. */
  private PageProperty loadPagePropertyFromJson(String jsonFile) throws IOException {
    try (InputStream inputStream = getClass().getResourceAsStream(jsonFile)) {
      assertNotNull(inputStream, "JSON file not found: " + jsonFile);
      return objectMapper.readValue(inputStream, PageProperty.class);
    }
  }
}
