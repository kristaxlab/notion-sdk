package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.ContainsEmptyFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationFilter extends Filter {

  private ContainsEmptyFilterCondition relation;
}
