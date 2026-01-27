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

  private final String type;

  ObjectType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public ObjectType fromType(String type) {
    for (ObjectType propertyType : ObjectType.values()) {
      if (propertyType.type.equals(type)) {
        return propertyType;
      }
    }
    throw new IllegalArgumentException("Unknown object type: " + type);
  }
}
