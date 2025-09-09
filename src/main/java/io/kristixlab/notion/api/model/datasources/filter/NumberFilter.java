package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.NumberFilterCondition;
import lombok.Data;

@Data
public class NumberFilter extends Filter {

  @JsonProperty("number")
  private NumberFilterCondition number;

  public static NumberFilter isEmpty() {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setIsEmpty(true);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter isNotEmpty() {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setIsNotEmpty(true);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter equals(Double value) {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setEquals(value);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter doesNotEqual(Double value) {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setDoesNotEqual(value);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter greaterThan(Double value) {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setGreaterThan(value);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter lessThan(Double value) {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setLessThan(value);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter greaterThanOrEqualTo(Double value) {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setGreaterThanOrEqualTo(value);
    filter.setNumber(condition);
    return filter;
  }

  public static NumberFilter lessThanOrEqualTo(Double value) {
    NumberFilter filter = new NumberFilter();
    NumberFilterCondition condition = new NumberFilterCondition();
    condition.setLessThanOrEqualTo(value);
    filter.setNumber(condition);
    return filter;
  }
}
