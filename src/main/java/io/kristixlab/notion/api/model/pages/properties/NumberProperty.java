package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NumberProperty extends PageProperty {
  private final String type = PagePropertyType.NUMBER.type();

  @JsonProperty("number")
  private Number number;

  public static NumberProperty of(Number number) {
    NumberProperty property = new NumberProperty();
    property.setNumber(number);
    return property;
  }
}
