package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for rollup columns. Aggregates values from a property in a related database.
 */
@Getter
@Setter
public class RollupSchema extends DataSourcePropertySchema {

  public RollupSchema() {
    setType(PropertyType.ROLLUP.type());
    rollup = new RollupConfig();
  }

  private RollupConfig rollup;

  @Getter
  @Setter
  public static class RollupConfig {
    private String relationPropertyName;

    private String relationPropertyId;

    private String rollupPropertyName;

    private String rollupPropertyId;

    private String function;
  }
}
