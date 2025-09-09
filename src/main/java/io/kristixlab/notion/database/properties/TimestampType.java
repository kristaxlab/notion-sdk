package io.kristixlab.notion.database.properties;

public enum TimestampType {
  CREATED_TIME("created_time"),
  LAST_EDITED_TIME("last_edited_time");

  private final String value;

  TimestampType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public TimestampType fromValue(String value) {
    for (TimestampType format : TimestampType.values()) {
      if (format.value.equals(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("Unknown number format: " + value);
  }
}
