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

  public FormulaSchema() {
    setType(PropertyType.FORMULA.type());
    formula = new FormulaConfig();
  }

  private FormulaConfig formula;

  @Getter
  @Setter
  public static class FormulaConfig {
    private String expression;
  }
}
