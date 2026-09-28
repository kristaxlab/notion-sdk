package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Value object inside a meeting-notes filter condition ({@code type} plus nested {@code value}).
 *
 * <p>Wire fields stay {@link String} / nested objects (ADR 0002). Prefer the static factories.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingNotesFilterValue {

  private String type;

  private Object value;

  private String direction;

  private String unit;

  private Integer count;

  /**
   * Exact text comparison value: {@code {"type":"exact","value":"..."}}.
   *
   * @param text literal text
   * @return filter value
   */
  public static MeetingNotesFilterValue exactText(String text) {
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("exact");
    v.setValue(text);
    return v;
  }

  /**
   * Relative date token: {@code {"type":"relative","value":"today"}} and siblings.
   *
   * @param token relative token ({@code today}, {@code yesterday}, …)
   * @return filter value
   */
  public static MeetingNotesFilterValue relativeDate(String token) {
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("relative");
    v.setValue(token);
    return v;
  }

  /**
   * Exact calendar date: {@code {"type":"exact","value":{"type":"date","start_date":"..."}}}.
   *
   * @param startDate ISO-8601 date
   * @return filter value
   */
  public static MeetingNotesFilterValue exactDate(String startDate) {
    ExactDate date = new ExactDate();
    date.setStartDate(startDate);
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("exact");
    v.setValue(date);
    return v;
  }

  /**
   * Exact datetime: {@code
   * {"type":"exact","value":{"type":"datetime","start_date":"...","start_time":"...","time_zone":"..."}}}.
   *
   * @param startDate ISO-8601 date
   * @param startTime HH:MM time of day
   * @param timeZone IANA time zone
   * @return filter value
   */
  public static MeetingNotesFilterValue exactDateTime(
      String startDate, String startTime, String timeZone) {
    ExactDateTime dateTime = new ExactDateTime();
    dateTime.setStartDate(startDate);
    dateTime.setStartTime(startTime);
    dateTime.setTimeZone(timeZone);
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("exact");
    v.setValue(dateTime);
    return v;
  }

  /**
   * Exact date range: {@code
   * {"type":"exact","value":{"type":"daterange","start_date":"...","end_date":"..."}}}.
   *
   * @param startDate ISO-8601 start date
   * @param endDate ISO-8601 end date; may be {@code null}
   * @return filter value
   */
  public static MeetingNotesFilterValue exactDateRange(String startDate, String endDate) {
    ExactDateRange range = new ExactDateRange();
    range.setStartDate(startDate);
    range.setEndDate(endDate);
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("exact");
    v.setValue(range);
    return v;
  }

  /**
   * Relative custom window for {@code date_is_within} / {@code date_is_relative_to}.
   *
   * @param direction {@code past} or {@code future}
   * @param unit {@code day}, {@code week}, {@code month}, or {@code year}
   * @param count window width
   * @return filter value
   */
  public static MeetingNotesFilterValue relativeCustom(String direction, String unit, int count) {
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("relative");
    v.setValue("custom");
    v.setDirection(direction);
    v.setUnit(unit);
    v.setCount(count);
    return v;
  }

  /**
   * Relative surrounding window for {@code date_is_within}.
   *
   * @param unit {@code day}, {@code week}, {@code month}, or {@code year}
   * @return filter value
   */
  public static MeetingNotesFilterValue relativeSurrounding(String unit) {
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("relative");
    v.setValue("surrounding");
    v.setUnit(unit);
    return v;
  }

  /**
   * Person match for a Notion user id: {@code
   * {"type":"exact","value":{"table":"notion_user","id":"..."}}}.
   *
   * @param userId user UUID (or {@code user://...} form)
   * @return filter value
   */
  public static MeetingNotesFilterValue personUser(String userId) {
    NotionUserRef ref = new NotionUserRef();
    ref.setId(userId);
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("exact");
    v.setValue(ref);
    return v;
  }

  /**
   * Relative person match for the current user: {@code {"type":"relative","value":"me"}}.
   *
   * @return filter value
   */
  public static MeetingNotesFilterValue relativeMe() {
    MeetingNotesFilterValue v = new MeetingNotesFilterValue();
    v.setType("relative");
    v.setValue("me");
    return v;
  }

  /** Nested {@code {"type":"date","start_date":"..."}}. */
  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ExactDate {
    private String type = "date";
    private String startDate;
  }

  /** Nested datetime value. */
  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ExactDateTime {
    private String type = "datetime";
    private String startDate;
    private String startTime;
    private String timeZone;
  }

  /** Nested daterange value. */
  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ExactDateRange {
    private String type = "daterange";
    private String startDate;
    private String endDate;
  }

  /** Nested Notion user pointer. */
  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class NotionUserRef {
    private String table = "notion_user";
    private String id;
  }
}
