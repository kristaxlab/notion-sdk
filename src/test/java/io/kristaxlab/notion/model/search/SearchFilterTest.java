package io.kristaxlab.notion.model.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchFilterTest {

  @Test
  @DisplayName("pages factory sets object filter for page")
  void pages_setsObjectFilter() {
    SearchFilter filter = SearchFilter.pages();

    assertEquals("object", filter.getProperty());
    assertEquals("page", filter.getValue());
    assertNull(filter.getInTrash());
  }

  @Test
  @DisplayName("dataSources factory sets object filter for data_source")
  void dataSources_setsObjectFilter() {
    SearchFilter filter = SearchFilter.dataSources();

    assertEquals("object", filter.getProperty());
    assertEquals("data_source", filter.getValue());
  }

  @Test
  @DisplayName("inTrash factory sets trash-only filter")
  void inTrash_setsTrashOnly() {
    SearchFilter filter = SearchFilter.inTrash(true);

    assertNull(filter.getProperty());
    assertNull(filter.getValue());
    assertEquals(Boolean.TRUE, filter.getInTrash());
  }

  @Test
  @DisplayName("withInTrash combines object filter and trash flag")
  void withInTrash_combines() {
    SearchFilter filter = SearchFilter.pages().withInTrash(true);

    assertEquals("object", filter.getProperty());
    assertEquals("page", filter.getValue());
    assertEquals(Boolean.TRUE, filter.getInTrash());
  }
}
