package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for URL columns. Stores web URLs with validation.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UrlDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("url")
  private Object url = new Object();
}
