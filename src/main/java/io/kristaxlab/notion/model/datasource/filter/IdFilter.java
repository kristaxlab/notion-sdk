package io.kristaxlab.notion.model.datasource.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdFilter extends Filter {

  private IdFilterCondition uniqueId;

  @Getter
  @Setter
  public static class IdFilterCondition {

    private Number equals;

    private Number doesNotEqual;

    private Number greaterThan;

    private Number greaterThanOrEqualTo;

    private Number lessThan;

    private Number lessThanOrEqualTo;
  }
}
