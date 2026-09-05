package io.kristaxlab.notion.model.datasource.filter;

import io.kristaxlab.notion.model.datasource.filter.condition.DateFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedTimeFilter extends Filter {

  private String timestamp = "created_time";

  private DateFilterCondition createdTime;

  public static CreatedTimeFilter equals(String value) {
    CreatedTimeFilter filter = new CreatedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setEquals(value);
    filter.setCreatedTime(condition);
    return filter;
  }

  public static CreatedTimeFilter before(String value) {
    CreatedTimeFilter filter = new CreatedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setBefore(value);
    filter.setCreatedTime(condition);
    return filter;
  }

  public static CreatedTimeFilter after(String value) {
    CreatedTimeFilter filter = new CreatedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setAfter(value);
    filter.setCreatedTime(condition);
    return filter;
  }

  public static CreatedTimeFilter onOrBefore(String value) {
    CreatedTimeFilter filter = new CreatedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setOnOrBefore(value);
    filter.setCreatedTime(condition);
    return filter;
  }

  public static CreatedTimeFilter onOrAfter(String value) {
    CreatedTimeFilter filter = new CreatedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setOnOrAfter(value);
    filter.setCreatedTime(condition);
    return filter;
  }
}
