package io.kristaxlab.notion.model.datasource.filter.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainsEmptyFilterCondition {

  private String contains;

  private String doesNotContain;

  private Boolean isEmpty;

  private Boolean isNotEmpty;
}
