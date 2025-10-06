package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.SortDirection;
import io.kristixlab.notion.api.model.common.Timestamp;
import lombok.Data;

/** Sort configuration for database queries. */
@Data
public class Sort {
  @JsonProperty("property")
  private String property;

  @JsonProperty("direction")
  private String direction; // "ascending" or "descending"

  @JsonProperty("timestamp")
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
