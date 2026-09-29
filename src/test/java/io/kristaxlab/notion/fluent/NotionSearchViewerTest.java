package io.kristaxlab.notion.fluent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.search.SearchList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NotionSearchViewer")
class NotionSearchViewerTest {

  @Test
  @DisplayName("of SearchList partitions pages and data sources")
  void of_searchList_partitions() {
    Page page = page("p-1");
    DataSource dataSource = dataSource("ds-1");
    SearchList list = searchList(page, dataSource, page("p-2"));

    NotionSearchViewer view = NotionSearchViewer.of(list);

    assertEquals(2, view.pages().size());
    assertEquals("p-1", view.pages().get(0).getId());
    assertEquals("p-2", view.pages().get(1).getId());
    assertEquals(1, view.dataSources().size());
    assertEquals("ds-1", view.dataSources().get(0).getId());
    assertEquals(3, view.size());
  }

  @Test
  @DisplayName("of null or empty yields empty view")
  void of_nullOrEmpty_isEmpty() {
    assertTrue(NotionSearchViewer.of((SearchList) null).isEmpty());
    assertTrue(NotionSearchViewer.of(new SearchList()).isEmpty());
    assertTrue(NotionSearchViewer.of((List<NotionObject>) null).isEmpty());
  }

  @Test
  @DisplayName("pages and dataSources return defensive copies")
  void partitions_areDefensiveCopies() {
    NotionSearchViewer view = NotionSearchViewer.of(searchList(page("p-1"), dataSource("ds-1")));

    view.pages().clear();
    view.dataSources().clear();

    assertEquals(1, view.pages().size());
    assertEquals(1, view.dataSources().size());
  }

  @Test
  @DisplayName("merge SearchList concatenates results without mutating either side")
  void merge_searchList() {
    NotionSearchViewer first = NotionSearchViewer.of(searchList(page("p-1")));
    SearchList second = searchList(dataSource("ds-1"), page("p-2"));

    NotionSearchViewer merged = first.merge(second);

    assertEquals(3, merged.size());
    assertEquals(List.of("p-1", "p-2"), merged.pages().stream().map(Page::getId).toList());
    assertEquals(1, merged.dataSources().size());
    assertEquals(1, first.size());
  }

  @Test
  @DisplayName("merge empty or null SearchList is a no-op")
  void merge_emptySearchList_noop() {
    NotionSearchViewer view = NotionSearchViewer.of(searchList(page("p-1")));

    assertSame(view, view.merge((SearchList) null));
    assertSame(view, view.merge(new SearchList()));
  }

  @Test
  @DisplayName("merge List concatenates results")
  void merge_list() {
    NotionSearchViewer first = NotionSearchViewer.of(searchList(page("p-1")));
    List<NotionObject> more = List.of(dataSource("ds-1"), page("p-2"));

    NotionSearchViewer merged = first.merge(more);

    assertEquals(3, merged.size());
    assertEquals("p-1", merged.pages().get(0).getId());
    assertEquals("p-2", merged.pages().get(1).getId());
    assertEquals("ds-1", merged.dataSources().get(0).getId());
  }

  @Test
  @DisplayName("merge empty or null List is a no-op")
  void merge_emptyList_noop() {
    NotionSearchViewer view = NotionSearchViewer.of(searchList(page("p-1")));

    assertSame(view, view.merge((List<NotionObject>) null));
    assertSame(view, view.merge(List.of()));
  }

  private static SearchList searchList(NotionObject... objects) {
    SearchList list = new SearchList();
    list.setResults(Arrays.asList(objects));
    return list;
  }

  private static Page page(String id) {
    Page page = new Page();
    page.setId(id);
    page.setObject("page");
    return page;
  }

  private static DataSource dataSource(String id) {
    DataSource dataSource = new DataSource();
    dataSource.setId(id);
    dataSource.setObject("data_source");
    return dataSource;
  }
}
