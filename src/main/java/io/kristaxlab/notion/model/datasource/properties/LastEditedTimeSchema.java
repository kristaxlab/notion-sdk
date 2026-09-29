package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for last_edited_time columns. Automatically populated with the timestamp when
 * the page was last edited.
 */
@Getter
@Setter
public class LastEditedTimeSchema extends DataSourcePropertySchema {

  public LastEditedTimeSchema() {
    setType(PropertyType.LAST_EDITED_TIME.type());
    lastEditedTime = new Object();
  }

  private Object lastEditedTime;
}
