package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for date columns. Supports different date formats. */
@Getter
@Setter
public class DateSchema extends DataSourcePropertySchema {

  public DateSchema() {
    setType(PropertyType.DATE.type());
    date = new Object();
  }

  private Object date;
}
