package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusProperty extends PageProperty {
  private final String type = PagePropertyType.STATUS.type();

  private StatusValue status;

  /** Creates a property selecting the option with the given id. */
  public static StatusProperty ofId(String id) {
    StatusProperty property = new StatusProperty();
    StatusValue value = new StatusValue();
    value.setId(id);
    property.setStatus(value);
    return property;
  }

  /** Creates a property selecting the option with the given name. */
  public static StatusProperty ofName(String name) {
    StatusProperty property = new StatusProperty();
    StatusValue value = new StatusValue();
    value.setName(name);
    property.setStatus(value);
    return property;
  }

  @Getter
  @Setter
  public static class StatusValue {
    private String id;
    private String name;
    private String color;
  }
}
