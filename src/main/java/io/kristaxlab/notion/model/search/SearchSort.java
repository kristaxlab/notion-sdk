package io.kristaxlab.notion.model.search;

import lombok.Getter;
import lombok.Setter;

/**
 * Search endpoint sort: either relevance or last-edited-time with a direction.
 *
 * <p>Wire fields stay {@link String} so unknown Notion tokens still deserialize (ADR 0002).
 */
@Getter
@Setter
public class SearchSort {

  private String timestamp;

  private String direction;

  private String property;

  /**
   * Sorts by relevance.
   *
   * @return sort with {@code property=relevance}
   */
  public static SearchSort byRelevance() {
    SearchSort sort = new SearchSort();
    sort.setProperty("relevance");
    return sort;
  }

  /**
   * Sorts by last edited time ascending.
   *
   * @return sort with {@code timestamp=last_edited_time} and {@code direction=ascending}
   */
  public static SearchSort byLastEditedTimeAscending() {
    return byLastEditedTime("ascending");
  }

  /**
   * Sorts by last edited time descending.
   *
   * @return sort with {@code timestamp=last_edited_time} and {@code direction=descending}
   */
  public static SearchSort byLastEditedTimeDescending() {
    return byLastEditedTime("descending");
  }

  private static SearchSort byLastEditedTime(String direction) {
    SearchSort sort = new SearchSort();
    sort.setTimestamp("last_edited_time");
    sort.setDirection(direction);
    return sort;
  }
}
