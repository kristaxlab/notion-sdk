package io.kristaxlab.notion.model.common;

public enum ParentType {
  BLOCK("block"),
  PAGE("page"),
  DATABASE("database"),
  DATA_SOURCE("data_source"),
  WORKSPACE("workspace");

  private final String type;

  ParentType(String type) {
    this.type = type;
  }

  public String type() {
    return type;
  }

  public static ParentType fromValue(String type) {
    for (ParentType propertyType : ParentType.values()) {
      if (propertyType.type.equals(type)) {
        return propertyType;
      }
    }
    throw new IllegalArgumentException("Unknown object type: " + type);
  }
}
