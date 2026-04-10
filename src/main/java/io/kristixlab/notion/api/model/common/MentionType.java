package io.kristixlab.notion.api.model.common;

public enum MentionType {
  PAGE("page"),
  DATABASE("database"),
  USER("user"),
  DATE("date"),
  CUSTOM_EMOJI("custom_emoji"),
  LINK("link_mention"),
  LINK_PREVIEW("link_preview"),
  TEMPLATE_MENTION("template_mention");

  private final String value;

  MentionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
