package io.kristaxlab.notion.model.page.property;

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

  public static SelectProperty of(String name) {
    return of(null, name);
  }

  public static SelectProperty of(String id, String name) {
    return of(SelectValue.of(id, name));
  }
}
