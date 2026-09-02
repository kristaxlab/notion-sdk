package io.kristaxlab.notion.model.page.property;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RetrievePropertyDeserializeTest {

  private static String fixture(String name) throws IOException {
    try (var stream = RetrievePropertyDeserializeTest.class.getResourceAsStream("/json/" + name)) {
      assertNotNull(stream, "Missing fixture: " + name);
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Test
  @DisplayName("retrieveProperty deserializes flat checkbox as PageProperty")
  void deserializesFlatCheckbox() throws IOException {
    RetrievedProperty property =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-flat-checkbox.json"), RetrievedProperty.class);

    assertInstanceOf(CheckboxProperty.class, property);
    CheckboxProperty checkbox = property.asValue(CheckboxProperty.class);
    assertEquals("checkbox", checkbox.getType());
    assertEquals("Done", checkbox.getId());
    assertEquals(false, checkbox.getCheckbox());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes full relation list response")
  void deserializesPaginatedRelation() throws IOException {
    RetrievedProperty list =
        JacksonSerializer.withDefaults()
            .toObject(
                fixture("retrieve-property-paginated-relation.json"), RetrievedProperty.class);

    RelationPropertyList relation = list.asList(RelationPropertyList.class);
    assertEquals("property_item", relation.getType());
    assertNotNull(relation.getPropertyItem());
    assertEquals("relation", relation.getPropertyItem().getType());
    assertNotNull(relation.getResults());
    assertTrue(relation.getResults().isEmpty());
    assertEquals(false, relation.getHasMore());
  }

  @Test
  @DisplayName("retrievePaginatedProperty deserializes title list with result items")
  void deserializesPaginatedTitle() throws IOException {
    PagePropertyList list =
        JacksonSerializer.withDefaults()
            .toObject(fixture("retrieve-property-paginated-title.json"), PagePropertyList.class);

    assertEquals("title", list.getPropertyItem().getType());
    assertEquals(1, list.getResults().size());
    assertInstanceOf(ListedRichText.class, list.getResults().get(0));
  }
}
