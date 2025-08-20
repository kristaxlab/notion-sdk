package io.kristixlab.notion.api.model.databases.filter.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * All the strings are in ISO 8601 format.
 */
@Data
public class DateFilterCondition {

    @JsonProperty("after")
    private String after;

    @JsonProperty("before")
    private String before;

    @JsonProperty("equals")
    private String equals;

    @JsonProperty("is_empty")
    private Boolean isEmpty;

    @JsonProperty("is_not_empty")
    private Boolean isNotEmpty;

    @JsonProperty("next_month")
    private Object nextMonth;

    @JsonProperty("next_week")
    private Object nextWeek;

    @JsonProperty("next_year")
    private Object nextYear;

    @JsonProperty("on_or_after")
    private String onOrAfter;

    @JsonProperty("on_or_before")
    private String onOrBefore;

    @JsonProperty("past_month")
    private Object pastMonth;

    @JsonProperty("past_week")
    private Object pastWeek;

    @JsonProperty("past_year")
    private Object pastYear;

    @JsonProperty("this_year")
    private Object thisWeek;
}
