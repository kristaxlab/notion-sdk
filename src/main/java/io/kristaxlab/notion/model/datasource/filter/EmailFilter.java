package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.TextFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailFilter extends Filter {

  private TextFilterCondition email;
}
