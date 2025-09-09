package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.ContainsEmptyFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectFilter extends Filter {

  @JsonProperty("multi_select")
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
