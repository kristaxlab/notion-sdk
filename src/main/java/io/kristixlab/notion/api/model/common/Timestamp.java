package io.kristixlab.notion.api.model.common;

public enum Timestamp {
  CREATED_TIME("created_time"),
  LAST_EDITED_TIME("last_edited_time");

  private final String value;

  Timestamp(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public Timestamp fromValue(String value) {
    for (Timestamp format : Timestamp.values()) {
      if (format.value.equals(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("Unknown number format: " + value);
  }
}
