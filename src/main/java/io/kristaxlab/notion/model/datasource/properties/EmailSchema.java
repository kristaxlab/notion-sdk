package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for email columns. Stores email addresses with validation. */
@Getter
@Setter
public class EmailSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.EMAIL.type();

  private Object email = new Object();
}
