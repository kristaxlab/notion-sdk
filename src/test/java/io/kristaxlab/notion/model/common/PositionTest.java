package io.kristaxlab.notion.model.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionTest {

  // afterBlock

  @Test
  @DisplayName("after block sets type and block id")
  void afterBlock_setsTypeAndBlockId() {
    Position pos = Position.afterBlock("block-123");

    assertEquals("after_block", pos.getType());
    assertNotNull(pos.getAfterBlock());
    assertEquals("block-123", pos.getAfterBlock().getId());
  }

  @Test
  @DisplayName("after block null throws illegal argument")
  void afterBlock_null_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Position.afterBlock(null));
  }

  @Test
  @DisplayName("after block blank throws illegal argument")
  void afterBlock_blank_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Position.afterBlock("   "));
  }

  @Test
  @DisplayName("after block empty throws illegal argument")
  void afterBlock_empty_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> Position.afterBlock(""));
  }

  // pageStart

  @Test
  @DisplayName("page start sets type")
  void pageStart_setsType() {
    Position pos = Position.pageStart();

    assertEquals("page_start", pos.getType());
    assertNull(pos.getAfterBlock());
  }

  // pageEnd

  @Test
  @DisplayName("page end sets type")
  void pageEnd_setsType() {
    Position pos = Position.pageEnd();

    assertEquals("page_end", pos.getType());
    assertNull(pos.getAfterBlock());
  }

  // No-arg constructor (for Jackson)

  @Test
  @DisplayName("no arg constructor all fields null")
  void noArgConstructor_allFieldsNull() {
    Position pos = new Position();

    assertNull(pos.getType());
    assertNull(pos.getAfterBlock());
  }

  // AfterBlock inner class

  @Test
  @DisplayName("after block no arg constructor")
  void afterBlock_noArgConstructor() {
    Position.AfterBlock ab = new Position.AfterBlock();

    assertNull(ab.getId());
  }
}
