package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for multi-select columns. Allows users to select multiple options from a
 * predefined list.
 */
@Getter
@Setter
public class MultiSelectSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.MULTI_SELECT.type();

  private MultiSelectConfig multiSelect = new MultiSelectConfig();

  @Data
  public static class MultiSelectConfig {

    private List<SelectOption> options;
  }
}
