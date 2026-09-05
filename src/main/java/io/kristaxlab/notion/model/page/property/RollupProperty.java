package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.DateData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RollupProperty extends PagePropertyValue {
  private final String type = PropertyType.ROLLUP.type();

  private RollupValue rollup;

  @Getter
  @Setter
  public static class RollupValue {
    private String type;
    private String function;
    private Object array;
    private Double number;
    private DateData date;
    private Object unsupported;
    private Object incomplete;
  }
}
