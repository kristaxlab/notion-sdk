package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ButtonProperty extends PageProperty {

  private final String type = "button";

  @JsonProperty("button")
  private Object button; // Empty object according to Notion API
}
