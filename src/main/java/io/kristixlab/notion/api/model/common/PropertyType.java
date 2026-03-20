package io.kristixlab.notion.api.model.common;

public enum PropertyType {
  TITLE("title"),
  RICH_TEXT("rich_text"),
  NUMBER("number"),
  SELECT("select"),
  MULTI_SELECT("multi_select"),
  DATE("date"),
  PEOPLE("people"),
  FILES("files"),
  CHECKBOX("checkbox"),
  URL("url"),
  EMAIL("email"),
  PHONE_NUMBER("phone_number"),
  FORMULA("formula"),
  RELATION("relation"),
  ROLLUP("rollup"),
  BUTTON("button"),
  CREATED_TIME("created_time"),
  CREATED_BY("created_by"),
  LAST_EDITED_TIME("last_edited_time"),
  LAST_EDITED_BY("last_edited_by");

  private final String value;

  PropertyType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public PropertyType fromType(String type) {
    for (PropertyType propertyType : PropertyType.values()) {
      if (propertyType.value.equals(type)) {
        return propertyType;
      }
    }
    throw new IllegalArgumentException("Unknown property type: " + type);
  }
}
