package io.kristixlab.notion.api.model.common.richtext;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Equation {

  private String expression;

  public static Equation of(String expression) {
    Equation equation = new Equation();
    equation.setExpression(expression);
    return equation;
  }
}
