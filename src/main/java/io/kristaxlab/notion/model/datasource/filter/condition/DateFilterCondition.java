package io.kristaxlab.notion.model.datasource.filter.condition;

import lombok.Getter;
import lombok.Setter;

/*
 * All the strings are in ISO 8601 format.
 */
@Getter
@Setter
public class DateFilterCondition {

  private String after;

  private String before;

  private String equals;

  private Boolean isEmpty;

  private Boolean isNotEmpty;

  private Object nextMonth;

  private Object nextWeek;

  private Object nextYear;

  private String onOrAfter;

  private String onOrBefore;

  private Object pastMonth;

  private Object pastWeek;

  private Object pastYear;

  private Object thisWeek;
}
