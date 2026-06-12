package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for unique_id columns. Generates unique sequential numbers for each page. */
@Getter
@Setter
public class UniqueIdSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.UNIQUE_ID.type();

  private UniqueIdConfig uniqueId = new UniqueIdConfig();

  @Getter
  @Setter
  public static class UniqueIdConfig {

    private String prefix;
  }
}
