package io.kristaxlab.notion.model.common.richtext;

import lombok.Getter;
import lombok.Setter;

/** Rich text equation payload containing a KaTeX expression string. */
@Getter
@Setter
public class Equation {

  private String expression;
}
