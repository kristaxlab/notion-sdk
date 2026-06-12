package io.kristaxlab.notion.model.datasource.filter;

import io.kristixlab.notion.api.model.datasources.filter.condition.ContainsEmptyFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationFilter extends Filter {

  private ContainsEmptyFilterCondition relation;
}
