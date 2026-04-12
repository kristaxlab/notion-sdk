package io.kristixlab.notion.api.model.block;

import lombok.*;

/** A Notion equation block that renders a KaTeX mathematical expression. */
@Getter
@Setter
public class EquationBlock extends Block {

  private Equation equation;

  public EquationBlock() {
    setType(BlockType.EQUATION.getValue());
    equation = new Equation();
  }

  /** The inner content object of an equation block. */
  @Getter
  @Setter
  public class Equation {

    private String expression;
  }
}
