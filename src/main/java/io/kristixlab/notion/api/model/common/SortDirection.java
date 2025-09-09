package io.kristixlab.notion.api.model.common;

public enum SortDirection {
  ASCENDING("ascending"),
  DESCENDING("descending");

  private final String value;

  SortDirection(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public SortDirection fromValue(String value) {
    for (SortDirection format : SortDirection.values()) {
      if (format.value.equals(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("Unknown number format: " + value);
  }
}
