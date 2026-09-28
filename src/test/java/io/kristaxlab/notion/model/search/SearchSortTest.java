package io.kristaxlab.notion.model.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchSortTest {

  @Test
  @DisplayName("byRelevance sets property only")
  void byRelevance() {
    SearchSort sort = SearchSort.byRelevance();

    assertEquals("relevance", sort.getProperty());
    assertNull(sort.getTimestamp());
    assertNull(sort.getDirection());
  }

  @Test
  @DisplayName("byLastEditedTimeAscending sets timestamp and direction")
  void byLastEditedTimeAscending() {
    SearchSort sort = SearchSort.byLastEditedTimeAscending();

    assertEquals("last_edited_time", sort.getTimestamp());
    assertEquals("ascending", sort.getDirection());
    assertNull(sort.getProperty());
  }

  @Test
  @DisplayName("byLastEditedTimeDescending sets timestamp and direction")
  void byLastEditedTimeDescending() {
    SearchSort sort = SearchSort.byLastEditedTimeDescending();

    assertEquals("last_edited_time", sort.getTimestamp());
    assertEquals("descending", sort.getDirection());
  }
}
