package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectProperty extends PageProperty {
  private final String type = PagePropertyType.SELECT.type();

  private SelectValue select;

  public static SelectProperty of(SelectValue select) {
    SelectProperty property = new SelectProperty();
    property.setSelect(select);
    return property;
  }

  public static SelectProperty ofId(String id) {
    SelectProperty property = new SelectProperty();
    property.setSelect(SelectValue.ofId(id));
    return property;
  }

  public static SelectProperty ofName(String name) {
    SelectProperty property = new SelectProperty();
    property.setSelect(SelectValue.ofName(name));
    return property;
  }
}
