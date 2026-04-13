package io.kristaxlab.notion.model.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PositionTest {

  // afterBlock

  @Test
  void afterBlock_setsTypeAndBlockId() {
    Position pos = Position.afterBlock("block-123");

    assertEquals("after_block", pos.getType());
    assertNotNull(pos.getAfterBlock());
    assertEquals("block-123", pos.getAfterBlock().getId());
  }

  @Test
  void afterBlock_null_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Position.afterBlock(null));
  }

  @Test
  void afterBlock_blank_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Position.afterBlock("   "));
  }

  @Test
  void afterBlock_empty_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Position.afterBlock(""));
  }

  // pageStart

  @Test
  void pageStart_setsType() {
    Position pos = Position.pageStart();

    assertEquals("page_start", pos.getType());
    assertNull(pos.getAfterBlock());
  }

  // pageEnd

  @Test
  void pageEnd_setsType() {
    Position pos = Position.pageEnd();

    assertEquals("page_end", pos.getType());
    assertNull(pos.getAfterBlock());
  }

  // No-arg constructor (for Jackson)

  @Test
  void noArgConstructor_allFieldsNull() {
    Position pos = new Position();

    assertNull(pos.getType());
    assertNull(pos.getAfterBlock());
  }

  // AfterBlock inner class

  @Test
  void afterBlock_noArgConstructor() {
    Position.AfterBlock ab = new Position.AfterBlock();

    assertNull(ab.getId());
  }
}
