package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for date columns. Supports different date formats.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DateDatasourceProperty extends DatasourceProperty {

  @JsonProperty("date")
  private DateConfig date = new DateConfig();

  @Data
  public static class DateConfig {
    @JsonProperty("format")
    private String format; // "ISO", "US", "EU", "relative"
  }
}
