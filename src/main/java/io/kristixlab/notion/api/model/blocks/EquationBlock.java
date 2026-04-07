package io.kristixlab.notion.api.model.blocks;

import lombok.*;

@Getter
@Setter
public class EquationBlock extends Block {

  private Equation equation;

  public EquationBlock() {
    setType("equation");
    equation = new Equation();
  }

  @Getter
  @Setter
  public class Equation {

    private String expression;
  }
}
