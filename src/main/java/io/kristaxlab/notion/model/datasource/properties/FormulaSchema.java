package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for formula columns. Computes values based on other properties in the database.
 */
@Getter
@Setter
public class FormulaSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.FORMULA.type();

  private FormulaConfig formula = new FormulaConfig();

  @Getter
  @Setter
  public static class FormulaConfig {
    private String expression;
  }
}
