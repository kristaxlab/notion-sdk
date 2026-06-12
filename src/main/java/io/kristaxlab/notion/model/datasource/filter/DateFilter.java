package io.kristaxlab.notion.model.datasource.filter;

import io.kristixlab.notion.api.model.datasources.filter.condition.DateFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateFilter extends Filter {

  private DateFilterCondition date;
}
