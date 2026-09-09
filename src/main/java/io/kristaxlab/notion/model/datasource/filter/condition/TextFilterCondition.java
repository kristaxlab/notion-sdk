package io.kristaxlab.notion.model.datasource.filter.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextFilterCondition {

  private String contains;

  private String doesNotContain;

  private String equals;

  private String doesNotEqual;

  private String startsWith;

  private String endsWith;

  private Boolean isEmpty;

  private Boolean isNotEmpty;
}
