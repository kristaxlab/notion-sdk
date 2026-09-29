package io.kristaxlab.notion.fluent;

import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.datasource.DataSource;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.search.SearchList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A read-only view over one search endpoint response that partitions pages and data sources.
 *
 * <p>Wraps the {@code results} of a {@link SearchList} (or any list of {@link NotionObject}). Does
 * not call the API and does not merge pagination metadata — use {@link #merge(SearchList)} when
 * combining manually fetched pages.
 *
 * <pre>{@code
 * NotionSearchViewer view = NotionSearchViewer.of(client.search().search("notes"));
 * List<Page> pages = view.pages();
 * List<DataSource> dataSources = view.dataSources();
 * }</pre>
 *
 * @see SearchList
 */
public final class NotionSearchViewer {

  private static final NotionSearchViewer EMPTY = new NotionSearchViewer(Collections.emptyList());

  private final List<NotionObject> results;

  private NotionSearchViewer(List<NotionObject> results) {
    this.results = results;
  }

  /**
   * Creates a view over the results of a search endpoint response.
   *
   * @param searchList search endpoint response; {@code null} or empty yields an empty view
   * @return a new view
   */
  public static NotionSearchViewer of(SearchList searchList) {
    if (searchList == null
        || searchList.getResults() == null
        || searchList.getResults().isEmpty()) {
      return EMPTY;
    }
    return of(searchList.getResults());
  }

  /**
   * Creates a view over the given objects. The list is defensively copied.
   *
   * @param results search results; {@code null} or empty yields an empty view
   * @return a new view
   */
  public static NotionSearchViewer of(List<? extends NotionObject> results) {
    if (results == null || results.isEmpty()) {
      return EMPTY;
    }
    List<NotionObject> copy = new ArrayList<>(results.size());
    for (NotionObject object : results) {
      if (object != null) {
        copy.add(object);
      }
    }
    return copy.isEmpty() ? EMPTY : new NotionSearchViewer(Collections.unmodifiableList(copy));
  }

  /**
   * Returns the pages in this view, in result order.
   *
   * @return defensive copy of page results; never {@code null}
   */
  public List<Page> pages() {
    return results.stream()
        .filter(Page.class::isInstance)
        .map(Page.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Returns the data sources in this view, in result order.
   *
   * @return defensive copy of data source results; never {@code null}
   */
  public List<DataSource> dataSources() {
    return results.stream()
        .filter(DataSource.class::isInstance)
        .map(DataSource.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Returns all wrapped results in order.
   *
   * @return unmodifiable view of results; never {@code null}
   */
  public List<NotionObject> results() {
    return results;
  }

  /**
   * Returns whether this view has no results.
   *
   * @return {@code true} when empty
   */
  public boolean isEmpty() {
    return results.isEmpty();
  }

  /**
   * Returns the number of wrapped results.
   *
   * @return result count
   */
  public int size() {
    return results.size();
  }

  /**
   * Returns a new view with this view's results followed by the other's results.
   *
   * <p>Does not merge {@code has_more}, {@code next_cursor}, or result completeness — those stay on
   * each {@link SearchList}.
   *
   * @param other another search endpoint response; {@code null} or empty is a no-op
   * @return new viewer with concatenated results
   */
  public NotionSearchViewer merge(SearchList other) {
    if (other == null) {
      return this;
    }
    return merge(other.getResults());
  }

  /**
   * Returns a new view with this view's results followed by the given objects.
   *
   * @param other additional results; {@code null} or empty is a no-op
   * @return new viewer with concatenated results
   */
  public NotionSearchViewer merge(List<? extends NotionObject> other) {
    if (other == null || other.isEmpty()) {
      return this;
    }
    List<NotionObject> combined = new ArrayList<>(results.size() + other.size());
    combined.addAll(results);
    for (NotionObject object : other) {
      if (object != null) {
        combined.add(object);
      }
    }
    if (combined.size() == results.size()) {
      return this;
    }
    return new NotionSearchViewer(Collections.unmodifiableList(combined));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NotionSearchViewer)) {
      return false;
    }
    NotionSearchViewer that = (NotionSearchViewer) o;
    return Objects.equals(results, that.results);
  }

  @Override
  public int hashCode() {
    return Objects.hash(results);
  }
}
