package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for date columns. Supports different date formats. */
@Data
@EqualsAndHashCode(callSuper = true)
public class DateSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("date")
  private Object date = new Object();
}
