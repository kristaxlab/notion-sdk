package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for last_edited_by columns. Automatically populated with the user who last
 * edited the page.
 */
@Getter
@Setter
public class LastEditedBySchema extends DataSourcePropertySchema {

  private final String type = PropertyType.LAST_EDITED_BY.type();

  private Object lastEditedBy = new Object();
}
