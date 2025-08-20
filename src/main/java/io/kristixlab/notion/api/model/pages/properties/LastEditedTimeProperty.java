package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedTimeProperty extends PageProperty {
  private final String type = "last_edited_time";
  @JsonProperty("last_edited_time")
  private String lastEditedTime;
}

