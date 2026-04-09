package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RollupProperty extends PageProperty {
  private final String type = PagePropertyType.ROLLUP.type();

  private RollupValue rollup;

  @Getter
  @Setter
  public static class RollupValue {
    private String type;
    private Object array;
    private Double number;
    private String date;
    private String function;
    // Add more fields as needed for rollup result types
  }
}
