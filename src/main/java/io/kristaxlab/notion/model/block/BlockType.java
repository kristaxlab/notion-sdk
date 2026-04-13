package io.kristaxlab.notion.model.block;

/**
 * Enumeration of all known Notion block type identifiers.
 *
 * <p>Each constant corresponds to the {@code type} string that the Notion API uses to discriminate
 * block objects. The raw API value is accessible via {@link #getValue()}.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * block.setType(BlockType.PARAGRAPH.getValue());
 *
 * if (BlockType.PARAGRAPH.getValue().equals(block.getType())) { ... }
 * }</pre>
 */
public enum BlockType {
  AUDIO("audio"),
  BOOKMARK("bookmark"),
  BREADCRUMB("breadcrumb"),
  BULLETED_LIST_ITEM("bulleted_list_item"),
  CALLOUT("callout"),
  CHILD_DATABASE("child_database"),
  CHILD_PAGE("child_page"),
  CODE("code"),
  COLUMN("column"),
  COLUMN_LIST("column_list"),
  DIVIDER("divider"),
  EMBED("embed"),
  EQUATION("equation"),
  FILE("file"),
  HEADING_1("heading_1"),
  HEADING_2("heading_2"),
  HEADING_3("heading_3"),
  HEADING_4("heading_4"),
  IMAGE("image"),
  LINK_PREVIEW("link_preview"),
  LINK_TO_PAGE("link_to_page"),
  NUMBERED_LIST_ITEM("numbered_list_item"),
  PARAGRAPH("paragraph"),
  PDF("pdf"),
  QUOTE("quote"),
  SYNCED_BLOCK("synced_block"),
  TABLE("table"),
  TABLE_OF_CONTENTS("table_of_contents"),
  TABLE_ROW("table_row"),
  TEMPLATE("template"),
  TO_DO("to_do"),
  TOGGLE("toggle"),
  UNSUPPORTED("unsupported"),
  VIDEO("video"),

  /** Sentinel type used by {@link BlockList} to identify lists of block objects. */
  BLOCK("block");

  private final String value;

  BlockType(String value) {
    this.value = value;
  }

  /**
   * Returns the raw Notion API type string for this block type.
   *
   * @return the API type string (e.g., {@code "paragraph"}, {@code "heading_1"})
   */
  public String getValue() {
    return value;
  }
}
