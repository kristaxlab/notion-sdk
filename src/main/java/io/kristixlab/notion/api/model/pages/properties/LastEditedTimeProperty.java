package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedTimeProperty extends PageProperty {
  private final String type = PagePropertyType.LAST_EDITED_TIME.type();

  @JsonProperty("last_edited_time")
  private String lastEditedTime;

  public static LastEditedTimeProperty of(String lastEditedTime) {
    LastEditedTimeProperty property = new LastEditedTimeProperty();
    property.setLastEditedTime(lastEditedTime);
    return property;
  }
}
