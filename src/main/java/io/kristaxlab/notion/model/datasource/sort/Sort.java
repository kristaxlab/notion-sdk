package io.kristaxlab.notion.model.datasource.sort;

import lombok.Getter;
import lombok.Setter;

/** Sort configuration for database queries. */
@Getter
@Setter
public class Sort {

  private String property;

  private String direction; // "ascending" or "descending"

  private String timestamp;

  public static Sort by(String property, SortDirection direction) {
    Sort sort = new Sort();
    sort.setProperty(property);
    sort.setDirection(direction.getValue());
    return sort;
  }

  public static Sort by(Timestamp timestamp, SortDirection direction) {
    Sort sort = new Sort();
    sort.setTimestamp(timestamp.getValue());
    sort.setDirection(direction.getValue());
    return sort;
  }
}
