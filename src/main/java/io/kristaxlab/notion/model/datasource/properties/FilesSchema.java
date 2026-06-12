package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for files columns. Allows uploading and storing files. */
@Getter
@Setter
public class FilesSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.FILES.type();

  private Object files = new Object();
}
