package io.kristixlab.notion.api.model.page.property;

import io.kristixlab.notion.api.model.common.DateData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateProperty extends PageProperty {
  private final String type = PagePropertyType.DATE.type();

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
