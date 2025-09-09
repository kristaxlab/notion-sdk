package io.kristixlab.notion.database.properties;

public enum SortDirectionType {
  ASCENDING("ascending"),
  DESCENDING("descending");

  private final String value;

  SortDirectionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public SortDirectionType fromValue(String value) {
    for (SortDirectionType format : SortDirectionType.values()) {
      if (format.value.equals(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("Unknown number format: " + value);
  }
}
