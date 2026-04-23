package io.kristaxlab.notion.model.common;

/** Enumerates mention type tokens used in Notion rich text payloads. */
public enum MentionType {
  /** Page mention. */
  PAGE("page"),
  /** Database mention. */
  DATABASE("database"),
  /** User mention. */
  USER("user"),
  /** Date mention. */
  DATE("date"),
  /** Custom emoji mention. */
  CUSTOM_EMOJI("custom_emoji"),
  /** Link mention. */
  LINK("link_mention"),
  /** Link preview mention. */
  LINK_PREVIEW("link_preview"),
  /** Template mention. */
  TEMPLATE_MENTION("template_mention");

  private final String value;

  MentionType(String value) {
    this.value = value;
  }

  /**
   * Returns the raw API token for this mention type.
   *
   * @return mention type token
   */
  public String getValue() {
    return value;
  }
}
