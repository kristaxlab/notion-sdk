package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckboxProperty extends PageProperty {
  private final String type = PagePropertyType.CHECKBOX.type();

  private Boolean checkbox;
}
