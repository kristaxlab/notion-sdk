package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RollupProperty extends PageProperty {
  private final String type = "rollup";
  @JsonProperty("rollup")
  private RollupValue rollup;

  @Data
  public static class RollupValue {
    private String type;
    private Object array;
    private Double number;
    private String date;
    private String function;
    // Add more fields as needed for rollup result types
  }
}

