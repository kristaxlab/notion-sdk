package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for created_by columns. Automatically populated with the user who created the
 * page.
 */
@Getter
@Setter
public class CreatedBySchema extends DataSourcePropertySchema {

  public CreatedBySchema() {
    setType(PropertyType.CREATED_BY.type());
    createdBy = new Object();
  }

  private Object createdBy;
}
