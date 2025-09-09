package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.DateFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class CreatedTimeFilter extends Filter {

  @JsonProperty("timestamp")
  private String timestamp = "created_time";

  @JsonProperty("created_time")
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
