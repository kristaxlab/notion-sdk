package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.DateData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class DateProperty extends PageProperty {
  private final String type = "date";

  @JsonProperty("date")
  private DateData date;

  public static DateProperty of(DateData date) {
    DateProperty property = new DateProperty();
    property.setDate(date);
    return property;
  }

  public static DateProperty of(LocalDateTime date) {
    DateData dateData = new DateData();
    dateData.setStart(date.toString());
    return of(dateData);
  }

  public static DateProperty of(LocalDate date) {
    DateData dateData = new DateData();
    dateData.setStart(date.toString());
    return of(dateData);
  }

  public static DateProperty of(String date) {
    DateData dateData = new DateData();
    dateData.setStart(date);
    return of(dateData);
  }

  public static DateProperty of(String start, String end, String timezone) {
    DateData dateData = new DateData();
    dateData.setStart(start);
    dateData.setEnd(end);
    dateData.setTimeZone(timezone);
    return of(dateData);
  }
}
