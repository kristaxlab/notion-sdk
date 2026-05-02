package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusProperty extends PageProperty {
  private final String type = PagePropertyType.STATUS.type();

  private StatusValue status;

  @Getter
  @Setter
  public static class StatusValue {
    private String id;
    private String name;
    private String color;
  }
}
