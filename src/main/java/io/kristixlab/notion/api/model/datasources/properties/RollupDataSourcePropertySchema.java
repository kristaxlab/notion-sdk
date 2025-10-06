package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for rollup columns. Aggregates values from a property in a related database.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RollupDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("rollup")
  private RollupConfig rollup = new RollupConfig();

  @Data
  public static class RollupConfig {
    @JsonProperty("relation_property_name")
    private String relationPropertyName;

    @JsonProperty("relation_property_id")
    private String relationPropertyId;

    @JsonProperty("rollup_property_name")
    private String rollupPropertyName;

    @JsonProperty("rollup_property_id")
    private String rollupPropertyId;

    @JsonProperty("function")
    private String
            function; // "count", "count_values", "empty", "not_empty", "unique", "show_unique",
    // "percent_empty", "percent_not_empty", "sum", "average", "median", "min", "max",
    // "range", "earliest_date", "latest_date", "date_range", "checked", "unchecked",
    // "percent_checked", "percent_unchecked", "show_original"
  }
}
