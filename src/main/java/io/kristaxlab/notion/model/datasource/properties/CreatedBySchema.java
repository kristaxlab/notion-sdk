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

  private final String type = PropertyType.CREATED_BY.type();

  private Object createdBy = new Object();
}
