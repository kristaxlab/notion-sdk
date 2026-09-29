package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for checkbox columns. Simple boolean property with no additional configuration.
 */
@Getter
@Setter
public class CheckboxSchema extends DataSourcePropertySchema {

  public CheckboxSchema() {
    setType(PropertyType.CHECKBOX.type());
    checkbox = new Object();
  }

  private Object checkbox;
}
