package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for created_time columns. Automatically populated with the timestamp when the
 * page was created.
 */
@Getter
@Setter
public class CreatedTimeSchema extends DataSourcePropertySchema {

  public CreatedTimeSchema() {
    setType(PropertyType.CREATED_TIME.type());
    createdTime = new Object();
  }

  private Object createdTime;
}
