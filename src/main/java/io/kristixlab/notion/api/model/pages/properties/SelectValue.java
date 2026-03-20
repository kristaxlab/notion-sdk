package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SelectValue {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("color")
  private String color;

  public static SelectValue of(String name) {
    return of(null, name);
  }

  public static SelectValue of(String id, String name) {
    SelectValue selectValue = new SelectValue();
    selectValue.setId(id);
    selectValue.setName(name);
    return selectValue;
  }
}
