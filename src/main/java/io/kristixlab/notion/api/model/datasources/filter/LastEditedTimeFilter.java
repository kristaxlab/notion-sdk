package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.condition.DateFilterCondition;
import lombok.Data;

@Data
public class LastEditedTimeFilter extends Filter {

  @JsonProperty("timestamp")
  private String timestamp = "last_edited_time";

  @JsonProperty("last_edited_time")
  private DateFilterCondition lastEditedTime;

  public static LastEditedTimeFilter equals(String value) {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setEquals(value);
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter before(String value) {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setBefore(value);
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter after(String value) {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setAfter(value);
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter onOrBefore(String value) {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setOnOrBefore(value);
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter onOrAfter(String value) {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setOnOrAfter(value);
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter thisWeek() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setThisWeek(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter nextMonth() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setNextMonth(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter nextWeek() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setNextWeek(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter nextYear() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setNextYear(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter pastMonth() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setPastMonth(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter pastWeek() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setPastWeek(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }

  public static LastEditedTimeFilter pastYear() {
    LastEditedTimeFilter filter = new LastEditedTimeFilter();
    DateFilterCondition condition = new DateFilterCondition();
    condition.setPastYear(new Object());
    filter.setLastEditedTime(condition);
    return filter;
  }
}
