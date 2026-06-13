package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.EqualsEmptyFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectFilter extends Filter {

  private EqualsEmptyFilterCondition select;

  public static SelectFilter isEmpty() {
    SelectFilter filter = new SelectFilter();
    EqualsEmptyFilterCondition condition = new EqualsEmptyFilterCondition();
    condition.setIsEmpty(true);
    return filter;
  }

  public static SelectFilter isNotEmpty() {
    SelectFilter filter = new SelectFilter();
    EqualsEmptyFilterCondition condition = new EqualsEmptyFilterCondition();
    condition.setIsNotEmpty(true);
    return filter;
  }

  public static SelectFilter equals(String value) {
    SelectFilter filter = new SelectFilter();
    EqualsEmptyFilterCondition condition = new EqualsEmptyFilterCondition();
    condition.setEquals(value);
    filter.setSelect(condition);
    return filter;
  }

  public static SelectFilter doesNotEqual(String value) {
    SelectFilter filter = new SelectFilter();
    EqualsEmptyFilterCondition condition = new EqualsEmptyFilterCondition();
    condition.setDoesNotEqual(value);
    filter.setSelect(condition);
    return filter;
  }
}
