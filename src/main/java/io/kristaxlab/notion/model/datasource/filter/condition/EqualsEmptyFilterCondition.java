package io.kristaxlab.notion.model.datasource.filter.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EqualsEmptyFilterCondition {

  private String equals;

  private String doesNotEqual;

  private Boolean isEmpty;

  private Boolean isNotEmpty;
}
