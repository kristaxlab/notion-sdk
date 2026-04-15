package io.kristaxlab.notion.model.common;

import lombok.Getter;

/**
 * Insertion position when appending block children or when creating a page under a block parent.
 *
 * <p>Use the static factory methods to create instances:
 *
 * <pre>{@code
 * Position.afterBlock("block-id")
 * Position.pageStart()
 * Position.pageEnd()
 * }</pre>
 */
@Getter
public class Position {

  /* after_block | page_start | page_end */
  private String type;

  private AfterBlock afterBlock;

  /** Required by Jackson for deserialization. Prefer static factory methods for construction. */
  public Position() {}

  private Position(String type) {
    this.type = type;
  }

  private Position(String type, String afterBlockId) {
    this.type = type;
    this.afterBlock = new AfterBlock(afterBlockId);
  }

  /**
   * Insert after a specific block.
   *
   * @param blockId the ID of the block to insert after
   * @return a Position targeting the location after the given block
   */
  public static Position afterBlock(String blockId) {
    if (blockId == null || blockId.trim().isEmpty()) {
      throw new IllegalArgumentException("blockId cannot be null or blank");
    }
    return new Position(PositionType.AFTER_BLOCK.getValue(), blockId);
  }

  /**
   * Insert at the start of the page.
   *
   * @return a Position targeting the beginning of the page
   */
  public static Position pageStart() {
    return new Position(PositionType.PAGE_START.getValue());
  }

  /**
   * Insert at the end of the page.
   *
   * @return a Position targeting the end of the page
   */
  public static Position pageEnd() {
    return new Position(PositionType.PAGE_END.getValue());
  }

  @Getter
  public static class AfterBlock {

    private String id;

    /** Required by Jackson for deserialization. */
    public AfterBlock() {}

    private AfterBlock(String id) {
      this.id = id;
    }
  }
}
