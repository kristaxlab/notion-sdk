package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceProperty extends PageProperty {

  private final String type = "place";

  @JsonProperty("place")
  private Object place;
}
