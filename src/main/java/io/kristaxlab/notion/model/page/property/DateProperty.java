package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.kristaxlab.notion.model.common.DateData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateProperty extends PageProperty {
  private final String type = PropertyType.DATE.type();

  // Always included is needed to support clearing a property value
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private DateData date;
}
