package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckboxProperty extends PageProperty {
  private final String type = PagePropertyType.CHECKBOX.type();

  private Boolean checkbox;

  public static CheckboxProperty checked() {
    CheckboxProperty property = new CheckboxProperty();
    property.setCheckbox(true);
    return property;
  }

  public static CheckboxProperty unchecked() {
    CheckboxProperty property = new CheckboxProperty();
    property.setCheckbox(false);
    return property;
  }
}
