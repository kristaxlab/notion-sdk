package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EquationBlock extends Block {
  @JsonProperty("equation")
  private Equation equation;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Equation {
    @JsonProperty("expression")
    private String expression;
  }
}
