package io.kristaxlab.notion.model.block;

/**
 * Property names accepted by the meeting-notes query filter and sort.
 *
 * <p>Model fields stay {@link String}; use {@link #getValue()} when building requests (ADR 0002).
 */
public enum MeetingNotesProperty {
  /** Meeting notes title. */
  TITLE("title"),
  /** Calendar-event attendees. */
  ATTENDEES("attendees"),
  /** Block created time. */
  CREATED_TIME("created_time"),
  /** Block created by. */
  CREATED_BY("created_by"),
  /** Block last edited time. */
  LAST_EDITED_TIME("last_edited_time"),
  /** Block last edited by. */
  LAST_EDITED_BY("last_edited_by");

  private final String value;

  MeetingNotesProperty(String value) {
    this.value = value;
  }

  /**
   * Returns the API property token.
   *
   * @return Notion property name (e.g. {@code "title"})
   */
  public String getValue() {
    return value;
  }
}
