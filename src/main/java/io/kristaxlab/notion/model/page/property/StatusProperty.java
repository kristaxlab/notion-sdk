package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusProperty extends PagePropertyValue {
  private final String type = PropertyType.STATUS.type();

  private StatusValue status;

  @Getter
  @Setter
  public static class StatusValue {
    private String id;
    private String name;
    private String color;
  }
}
