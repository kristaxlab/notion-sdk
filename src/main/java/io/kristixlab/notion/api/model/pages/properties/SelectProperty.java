package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectProperty extends PageProperty {
  private final String type = "select";

  @JsonProperty("select")
  private SelectValue select;

  public static SelectProperty of(SelectValue select) {
    SelectProperty property = new SelectProperty();
    property.setSelect(select);
    return property;
  }

  public static SelectProperty of(String id, String name, Color color) {
    return of(SelectValue.of(id, name, color));
  }

  public static SelectProperty of(String name, Color color) {
    return of(null, name, color);
  }
}
