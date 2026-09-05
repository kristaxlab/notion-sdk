package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectProperty extends PagePropertyValue {
  private final String type = PropertyType.SELECT.type();

  // Always included is needed to support clearing a property value
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private SelectValue select;
}
