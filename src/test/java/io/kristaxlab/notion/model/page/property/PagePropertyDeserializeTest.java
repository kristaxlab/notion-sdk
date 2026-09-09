package io.kristaxlab.notion.model.page.property;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PagePropertyDeserializeTest {

  private static String fixture(String name) throws IOException {
    try (var stream = PagePropertyDeserializeTest.class.getResourceAsStream("/json/" + name)) {
      assertNotNull(stream, "Missing fixture: " + name);
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Test
  @DisplayName("retrieveProperty deserializes checkbox as PagePropertyValue")
  void deserializesCheckboxValue() throws IOException {
    PageProperty property =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-value-checkbox.json"), PageProperty.class);

    assertInstanceOf(CheckboxProperty.class, property);
    CheckboxProperty checkbox = property.asValue(CheckboxProperty.class);
    assertEquals("checkbox", checkbox.getType());
    assertEquals("Done", checkbox.getId());
    assertEquals(false, checkbox.getCheckbox());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes full relation list response")
  void deserializesPaginatedRelation() throws IOException {
    PageProperty list =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-list-relation.json"), PageProperty.class);

    RelationPropertyList relation = list.asList(RelationPropertyList.class);
    assertEquals("property_item", relation.getType());
    assertNotNull(relation.getPropertyItem());
    assertEquals("relation", relation.getPropertyItem().getType());
    assertNotNull(relation.getResults());
    assertEquals(1, relation.getResults().size());
    assertEquals(
        "535c3fb2-95e6-4b37-a696-036e5eac5cf6", relation.getResults().get(0).getRelation().getId());
    assertEquals(false, relation.getHasMore());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes title list with result items")
  void deserializesPaginatedTitle() throws IOException {
    TitlePropertyList list =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-list-title.json"), PagePropertyList.class)
            .asTitleList();

    assertEquals("title", list.getPropertyItem().getType());
    assertEquals(1, list.getResults().size());
    assertInstanceOf(ListedRichText.class, list.getResults().get(0));
    assertEquals("Parent", list.getResults().get(0).getRichText().getPlainText());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes rollup list with relation results")
  void deserializesPaginatedRollup() throws IOException {
    PageProperty list =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-list-rollup.json"), PageProperty.class);

    RollupPropertyList rollup = list.asList(RollupPropertyList.class);
    assertEquals("property_item", rollup.getType());
    assertNotNull(rollup.getPropertyItem());
    assertEquals("rollup", rollup.getPropertyItem().getType());
    assertEquals("count", rollup.getPropertyItem().getRollup().getFunction());
    assertEquals(1.0, rollup.getPropertyItem().getRollup().getNumber());
    assertEquals(1, rollup.getResults().size());
    assertInstanceOf(ListedRelation.class, rollup.getResults().get(0));
    assertEquals(
        "535c3fb2-95e6-4b37-a696-036e5eac5cf6",
        ((ListedRelation) rollup.getResults().get(0)).getRelation().getId());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes rollup show_original as title listed items")
  void deserializesPaginatedRollupShowOriginal() throws IOException {
    RollupPropertyList rollup =
        JacksonSerializer.withDefaults()
            .toObject(
                fixture("retrieve-property-list-rollup-show-original.json"), PageProperty.class)
            .asList(RollupPropertyList.class);

    assertEquals("show_original", rollup.getPropertyItem().getRollup().getFunction());
    assertEquals(1, rollup.getResults().size());
    assertInstanceOf(ListedRichText.class, rollup.getResults().get(0));
    assertEquals("title", rollup.getResults().get(0).getType());
    assertEquals(
        "Child 0", ((ListedRichText) rollup.getResults().get(0)).getRichText().getPlainText());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes rollup median as number listed items")
  void deserializesPaginatedRollupNumber() throws IOException {
    RollupPropertyList rollup =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-list-rollup-number.json"), PageProperty.class)
            .asList(RollupPropertyList.class);

    assertEquals("median", rollup.getPropertyItem().getRollup().getFunction());
    assertEquals(1, rollup.getResults().size());
    assertInstanceOf(ListedNumber.class, rollup.getResults().get(0));
    assertEquals("number", rollup.getResults().get(0).getType());
    assertEquals(12.5, ((ListedNumber) rollup.getResults().get(0)).getNumber());
  }
}
