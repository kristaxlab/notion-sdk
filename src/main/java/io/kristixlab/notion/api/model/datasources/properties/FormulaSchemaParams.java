package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for formula columns. Computes values based on other properties in the database.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormulaSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("formula")
  private FormulaConfig formula = new FormulaConfig();

  @Data
  public static class FormulaConfig {
    @JsonProperty("expression")
    private String expression;
  }

  public static FormulaSchemaParams fromExpression(String expression) {
    FormulaSchemaParams params = new FormulaSchemaParams();
    params.getFormula().setExpression(expression);
    return params;
  }
}
