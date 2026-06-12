package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for title columns. Every database must have exactly one title property. */
@Getter
@Setter
public class TitleSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.TITLE.type();

  private Object title = new Object();
}
