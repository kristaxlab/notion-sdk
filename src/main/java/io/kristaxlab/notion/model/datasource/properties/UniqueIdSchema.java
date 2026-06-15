package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for unique_id columns. Generates unique sequential numbers for each page. */
@Getter
@Setter
public class UniqueIdSchema extends DataSourcePropertySchema {

  public UniqueIdSchema() {
    setType(PropertyType.UNIQUE_ID.type());
    uniqueId = new UniqueIdConfig();
  }

  private UniqueIdConfig uniqueId;

  @Getter
  @Setter
  public static class UniqueIdConfig {

    private String prefix;
  }
}
