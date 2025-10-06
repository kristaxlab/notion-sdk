package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for email columns. Stores email addresses with validation.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("email")
  private Object email = new Object();
}
