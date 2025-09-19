package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxProperty extends PageProperty {
  private final String type = "checkbox";

  @JsonProperty("checkbox")
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
