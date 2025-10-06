package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for unique_id columns. Generates unique sequential numbers for each page.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniqueIdDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("unique_id")
  private UniqueIdConfig uniqueId = new UniqueIdConfig();

  @Data
  public static class UniqueIdConfig {
    @JsonProperty("prefix")
    private String prefix;
  }
}
