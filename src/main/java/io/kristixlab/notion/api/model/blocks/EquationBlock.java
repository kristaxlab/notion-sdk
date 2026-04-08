package io.kristixlab.notion.api.model.blocks;

import lombok.*;

/**
 * A Notion equation block that renders a KaTeX mathematical expression.
 *
 * <p>Simple construction via {@link #of(String)}.
 */
@Getter
@Setter
public class EquationBlock extends Block {

  private Equation equation;

  public EquationBlock() {
    setType("equation");
    equation = new Equation();
  }

  /** The inner content object of an equation block. */
  @Getter
  @Setter
  public class Equation {

    private String expression;
  }

  /**
   * Creates an equation block with the given KaTeX expression.
   *
   * @param expression the KaTeX expression string
   * @return a new EquationBlock
   */
  public static EquationBlock of(String expression) {
    EquationBlock block = new EquationBlock();
    block.getEquation().setExpression(expression);
    return block;
  }
}
