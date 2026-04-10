package io.kristixlab.notion.api.model.page.property;

public enum PagePropertyType {
  TITLE("title"),
  CREATED_TIME("created_time"),
  CREATED_BY("created_by"),
  LAST_EDITED_TIME("last_edited_time"),
  LAST_EDITED_BY("last_edited_by"),
  VERIFICATION("verification"),
  UNIQUE_ID("unique_id");

  private final String type;

  PagePropertyType(String type) {
    this.type = type;
  }

  public String type() {
    return type;
  }
}
