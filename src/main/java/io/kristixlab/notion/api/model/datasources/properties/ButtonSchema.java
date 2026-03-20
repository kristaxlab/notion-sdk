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
public class ButtonSchema extends DataSourcePropertySchema {

  @JsonProperty("button")
  private Object button = new Object();

  public ButtonSchema() {
    setType(PropertyType.BUTTON.getValue());
  }

  public ButtonSchema(String name) {
    super(name);
    setType(PropertyType.BUTTON.getValue());
  }
}
