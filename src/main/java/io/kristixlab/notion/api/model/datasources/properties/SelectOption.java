package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SelectOption {
  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("color")
  private String color;

  public static SelectOption of(String name) {
    SelectOption opt = new SelectOption();
    opt.setName(name);
    return opt;
  }

  public static SelectOption of(String name, Color color) {
    SelectOption opt = new SelectOption();
    opt.setName(name);
    opt.setColor(color.getValue());
    return opt;
  }
}
