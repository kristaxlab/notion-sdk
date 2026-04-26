package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

/* readonly */
@Getter
@Setter
public class UniqueIdProperty extends PageProperty {
  private final String type = PagePropertyType.UNIQUE_ID.type();

  private UniqueIdValue uniqueId;

  @Getter
  @Setter
  public static class UniqueIdValue {
    private Integer number;
    private String prefix;
  }
}
