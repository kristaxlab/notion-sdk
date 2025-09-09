package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for checkbox columns. Simple boolean property with no additional configuration.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxDatasourceProperty extends DatasourceProperty {

  @JsonProperty("checkbox")
  private Object checkbox = new Object();
}
