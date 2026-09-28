package io.kristaxlab.notion.model.datasource.sort;

import io.kristaxlab.notion.model.common.SortDirection;
import lombok.Getter;
import lombok.Setter;

/** Sort configuration for database queries. */
@Getter
@Setter
public class QuerySort {

  private String property;

  private String direction; // "ascending" or "descending"

  private String timestamp;

  public static QuerySort by(String property, SortDirection direction) {
    QuerySort sort = new QuerySort();
    sort.setProperty(property);
    sort.setDirection(direction.getValue());
    return sort;
  }

  public static QuerySort by(Timestamp timestamp, SortDirection direction) {
    QuerySort sort = new QuerySort();
    sort.setTimestamp(timestamp.getValue());
    sort.setDirection(direction.getValue());
    return sort;
  }
}
