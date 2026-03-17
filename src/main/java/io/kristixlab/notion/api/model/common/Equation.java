package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Equation {

  @JsonProperty("expression")
  private String expression;

  public static Equation fromExpression(String expression) {
    Equation equation = new Equation();
    equation.setExpression(expression);
    return equation;
  }
}
