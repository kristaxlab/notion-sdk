package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for URL columns. Stores web URLs with validation. */
@Getter
@Setter
public class UrlSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.URL.type();

  private Object url = new Object();
}
