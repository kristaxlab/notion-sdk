package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

/** Represents a Notion date value, including optional end date and timezone. */
@Getter
@Setter
public class DateData {

  private String start;

  private String end;

  private String timeZone;
}
