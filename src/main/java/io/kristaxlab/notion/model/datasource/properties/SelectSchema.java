package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for select columns. Allows users to select one option from a predefined list.
 */
@Getter
@Setter
public class SelectSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.SELECT.type();

  private SelectConfig select = new SelectConfig();

  @Getter
  @Setter
  public static class SelectConfig {

    private List<SelectOption> options;
  }
}
