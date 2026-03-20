package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SelectProperty extends PageProperty {
  private final String type = PagePropertyType.SELECT.type();

  @JsonProperty("select")
  private SelectValue select;

  public static SelectProperty of(SelectValue select) {
    SelectProperty property = new SelectProperty();
    property.setSelect(select);
    return property;
  }

  public static SelectProperty of(String name) {
    return of(null, name, null);
  }

  public static SelectProperty of(String id, String name, Color color) {
    return of(SelectValue.of(id, name, color));
  }

  public static SelectProperty of(String name, Color color) {
    return of(null, name, color);
  }
}
