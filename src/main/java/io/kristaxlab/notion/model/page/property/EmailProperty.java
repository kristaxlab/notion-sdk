package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailProperty extends PageProperty {
  private final String type = PropertyType.EMAIL.type();

  // Always included is needed to support clearing a property value
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private String email;
}
