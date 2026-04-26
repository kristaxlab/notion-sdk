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
  /** Audio block. */
  AUDIO("audio"),
  /** Bookmark block. */
  BOOKMARK("bookmark"),
  /** Breadcrumb block. */
  BREADCRUMB("breadcrumb"),
  /** Bulleted list item block. */
  BULLETED_LIST_ITEM("bulleted_list_item"),
  /** Callout block. */
  CALLOUT("callout"),
  /** Child database block. */
  CHILD_DATABASE("child_database"),
  /** Child page block. */
  CHILD_PAGE("child_page"),
  /** Code block. */
  CODE("code"),
  /** Column block. */
  COLUMN("column"),
  /** Column list block. */
  COLUMN_LIST("column_list"),
  /** Divider block. */
  DIVIDER("divider"),
  /** Embed block. */
  EMBED("embed"),
  /** Equation block. */
  EQUATION("equation"),
  /** Generic file block. */
  FILE("file"),
  /** Heading level 1 block. */
  HEADING_1("heading_1"),
  /** Heading level 2 block. */
  HEADING_2("heading_2"),
  /** Heading level 3 block. */
  HEADING_3("heading_3"),
  /** Heading level 4 block. */
  HEADING_4("heading_4"),
  /** Image block. */
  IMAGE("image"),
  /** Link preview block. */
  LINK_PREVIEW("link_preview"),
  /** Link-to-page block. */
  LINK_TO_PAGE("link_to_page"),
  /** Numbered list item block. */
  NUMBERED_LIST_ITEM("numbered_list_item"),
  /** Paragraph block. */
  PARAGRAPH("paragraph"),
  /** PDF block. */
  PDF("pdf"),
  /** Quote block. */
  QUOTE("quote"),
  /** Synced block. */
  SYNCED_BLOCK("synced_block"),
  /** Table block. */
  TABLE("table"),
  /** Table of contents block. */
  TABLE_OF_CONTENTS("table_of_contents"),
  /** Table row block. */
  TABLE_ROW("table_row"),
  /** Template block. */
  TEMPLATE("template"),
  /** To-do block. */
  TO_DO("to_do"),
  /** Toggle block. */
  TOGGLE("toggle"),
  /** Unsupported block. */
  UNSUPPORTED("unsupported"),
  /** Video block. */
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
