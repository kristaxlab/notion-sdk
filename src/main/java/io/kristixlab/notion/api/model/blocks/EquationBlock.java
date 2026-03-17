package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Equation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EquationBlock extends Block {
  @JsonProperty("equation")
  private Equation equation;

  public EquationBlock() {
    setType("equation");
    equation = new Equation();
  }
}
