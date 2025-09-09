package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.database.properties.SortDirectionType;
import io.kristixlab.notion.database.properties.TimestampType;
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

  public static Sort by(String property, SortDirectionType direction) {
    Sort sort = new Sort();
    sort.setProperty(property);
    sort.setDirection(direction.getValue());
    return sort;
  }

  public static Sort by(TimestampType timestamp, SortDirectionType direction) {
    Sort sort = new Sort();
    sort.setTimestamp(timestamp.getValue());
    sort.setDirection(direction.getValue());
    return sort;
  }
}
