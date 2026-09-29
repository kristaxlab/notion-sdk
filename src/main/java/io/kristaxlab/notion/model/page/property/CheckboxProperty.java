package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckboxProperty extends PagePropertyValue {
  private final String type = PropertyType.CHECKBOX.type();

  private Boolean checkbox;
}
