package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.EqualsEmptyFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class SelectFilter extends Filter {

  @JsonProperty("select")
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
