package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import lombok.Data;

@Data
public class SelectOptionParams {

  // TODO replace SelectOption in Prams model?
  @JsonProperty("name")
  private String name;

  @JsonProperty("color")
  private String color;

  @JsonProperty("description")
  private String description;

  public static SelectOptionParams of(String name) {
    SelectOptionParams opt = new SelectOptionParams();
    opt.setName(name);
    return opt;
  }

  public static SelectOptionParams of(String name, Color color) {
    SelectOptionParams opt = new SelectOptionParams();
    opt.setName(name);
    opt.setColor(color.getValue());
    return opt;
  }
}
