package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import lombok.Data;

@Data
public class SelectValue {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("color")
  private String color;

  public static SelectValue of(String name, Color color) {
    return of(null, name, color);
  }

  public static SelectValue of(String id, String name, Color color) {
    SelectValue selectValue = new SelectValue();
    selectValue.setId(id);
    selectValue.setName(name);
    selectValue.setColor(color.getValue());
    return selectValue;
  }
}
