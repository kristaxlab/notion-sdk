package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.DateFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateFilter extends Filter {

  private DateFilterCondition date;
}
