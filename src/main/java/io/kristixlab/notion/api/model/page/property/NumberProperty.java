package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberProperty extends PageProperty {
  private final String type = PagePropertyType.NUMBER.type();

  private Number number;

  public static NumberProperty of(Number number) {
    NumberProperty property = new NumberProperty();
    property.setNumber(number);
    return property;
  }
}
