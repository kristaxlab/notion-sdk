package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for created_time columns. Automatically populated with the timestamp when the
 * page was created.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedTimeDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("created_time")
  private Object createdTime = new Object();
}
