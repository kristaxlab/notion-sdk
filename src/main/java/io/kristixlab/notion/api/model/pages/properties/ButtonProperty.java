package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ButtonProperty extends PageProperty {

  private final String type = PagePropertyType.BUTTON.type();

  @JsonProperty("button")
  private Object button = new Object();
}
