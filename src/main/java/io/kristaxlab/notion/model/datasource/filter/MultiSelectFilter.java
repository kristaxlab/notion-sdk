package io.kristaxlab.notion.model.datasource.filter;

import io.kristixlab.notion.api.model.datasources.filter.condition.ContainsEmptyFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiSelectFilter extends Filter {

  private ContainsEmptyFilterCondition multiSelect;

  public static MultiSelectFilter isEmpty() {
    MultiSelectFilter filter = new MultiSelectFilter();
    ContainsEmptyFilterCondition condition = new ContainsEmptyFilterCondition();
    condition.setIsEmpty(true);
    filter.setMultiSelect(condition);
    return filter;
  }

  public static MultiSelectFilter isNotEmpty() {
    MultiSelectFilter filter = new MultiSelectFilter();
    ContainsEmptyFilterCondition condition = new ContainsEmptyFilterCondition();
    condition.setIsNotEmpty(true);
    filter.setMultiSelect(condition);
    return filter;
  }

  public static MultiSelectFilter contains(String value) {
    MultiSelectFilter filter = new MultiSelectFilter();
    ContainsEmptyFilterCondition condition = new ContainsEmptyFilterCondition();
    condition.setContains(value);
    filter.setMultiSelect(condition);
    return filter;
  }

  public static MultiSelectFilter doesNotContain(String value) {
    MultiSelectFilter filter = new MultiSelectFilter();
    ContainsEmptyFilterCondition condition = new ContainsEmptyFilterCondition();
    condition.setDoesNotContain(value);
    filter.setMultiSelect(condition);
    return filter;
  }
}
