package io.kristixlab.notion.api.model.common.richtext;

public enum RichTextType {
  TEXT("text"),
  EQUATION("equation"),
  MENTION("mention");

  private final String value;

  RichTextType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
