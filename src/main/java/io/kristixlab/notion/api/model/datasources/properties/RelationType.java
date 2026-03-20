package io.kristixlab.notion.api.model.datasources.properties;

public enum RelationType {
  SINGLE_PROPERTY("single_property"),
  DUAL_PROPERTY("dual_property");

  private final String value;

  RelationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
