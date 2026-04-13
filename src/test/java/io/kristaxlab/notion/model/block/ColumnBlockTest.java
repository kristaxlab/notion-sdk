package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ColumnBlockTest {

  // ColumnBlock constructors

  @Test
  void constructor_setsTypeAndInitializesColumn() {
    ColumnBlock block = new ColumnBlock();

    assertEquals("column", block.getType());
    assertNotNull(block.getColumn());
    assertNotNull(block.getColumn().getChildren());
    assertTrue(block.getColumn().getChildren().isEmpty());
  }

  // Column inner class

  @Test
  void column_getterSetter() {
    ColumnBlock.Column column = new ColumnBlock.Column();

    assertNull(column.getWidthRatio());
    column.setWidthRatio(0.6);
    assertEquals(0.6, column.getWidthRatio());
  }

  // ColumnListBlock constructors

  @Test
  void columnList_constructor_setsTypeAndInitializes() {
    ColumnListBlock block = new ColumnListBlock();

    assertEquals("column_list", block.getType());
    assertNotNull(block.getColumnList());
  }
}
