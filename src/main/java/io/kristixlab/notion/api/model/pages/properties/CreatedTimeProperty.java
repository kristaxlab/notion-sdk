package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedTimeProperty extends PageProperty {
  private final String type = "created_time";

  @JsonProperty("created_time")
  private String createdTime;

  public static CreatedTimeProperty of(String createdTime) {
    CreatedTimeProperty property = new CreatedTimeProperty();
    property.setCreatedTime(createdTime);
    return property;
  }
}
