package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatusProperty extends PageProperty {
  private final String type = PagePropertyType.STATUS.type();

  @JsonProperty("status")
  private StatusValue status;

  /** Creates a property selecting the option with the given name. */
  public static StatusProperty of(String name) {
    StatusProperty property = new StatusProperty();
    StatusValue value = new StatusValue();
    value.setName(name);
    property.setStatus(value);
    return property;
  }

  @Data
  public static class StatusValue {
    private String id;
    private String name;
    private String color;
  }
}
