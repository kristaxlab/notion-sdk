package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceProperty extends PageProperty {

  private final String type = PagePropertyType.PLACE.type();

  @JsonProperty("place")
  private Object place = new Object();
}
