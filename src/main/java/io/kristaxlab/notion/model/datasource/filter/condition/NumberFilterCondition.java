package io.kristaxlab.notion.model.datasource.filter.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberFilterCondition {
  private Number equals;

  private Number doesNotEqual;

  private Boolean isEmpty;

  private Boolean isNotEmpty;

  private Number greaterThan;

  private Number greaterThanOrEqualTo;

  private Number lessThan;

  private Number lessThanOrEqualTo;
}
