package io.kristixlab.notion.api.model.datasources.properties;

public enum RollupFunctionType {

  COUNT("count"),
  COUNT_VALUES("count_values"),
  EMPTY("empty"),
  NOT_EMPTY("not_empty"),
  UNIQUE("unique"),
  SHOW_UNIQUE("show_unique"),
  PERCENT_EMPTY("percent_empty"),
  PERCENT_NOT_EMPTY("percent_not_empty"),
  SUM("sum"),
  AVERAGE("average"),
  MEDIAN("median"),
  MIN("min"),
  MAX("max"),
  RANGE("range"),
  EARLIEST_DATE("earliest_date"),
  LATEST_DATE("latest_date"),
  DATE_RANGE("date_range"),
  CHECKED("checked"),
  UNCHECKED("unchecked"),
  PERCENT_CHECKED("percent_checked"),
  PERCENT_UNCHECKED("percent_unchecked"),
  SHOW_ORIGINAL("show_original");

  private final String value;

  RollupFunctionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
