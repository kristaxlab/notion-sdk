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
}
