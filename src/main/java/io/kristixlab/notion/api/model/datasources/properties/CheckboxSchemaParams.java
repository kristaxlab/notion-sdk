package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.PropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for checkbox columns. Simple boolean property with no additional configuration.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("checkbox")
  private Object checkbox = new Object();

  @Override
  public String getType() {
    return PropertyType.CHECKBOX.getValue();
  }
}
