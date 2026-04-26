package io.kristixlab.notion.api.model.common;

public enum PositionType {
  PAGE_START("page_start"),
  PAGE_END("page_end"),
  AFTER_BLOCK("after_block");

  private final String value;

  PositionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
