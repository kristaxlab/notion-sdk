package io.kristixlab.notion.api.model.common;

public enum ObjectType {
  PAGE("page"),
  DATABASE("database"),
  DATA_SOURCE("data_source"),
  COMMENT("comment"),
  BLOCK("block"),

  USER("user"),
  LIST("list"),
  PROPERTY_ITEM("property_item");

  private final String value;

  ObjectType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static ObjectType fromValue(String type) {
    for (ObjectType propertyType : ObjectType.values()) {
      if (propertyType.value.equals(type)) {
        return propertyType;
      }
    }
    throw new IllegalArgumentException("Unknown object type: " + type);
  }
}
