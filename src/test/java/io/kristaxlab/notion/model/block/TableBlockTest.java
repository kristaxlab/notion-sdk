package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableBlockTest {

  // TableBlock constructors

  @Test
  @DisplayName("constructor sets type and initializes table")
  void constructor_setsTypeAndInitializesTable() {
    TableBlock block = new TableBlock();

    assertEquals("table", block.getType());
    assertNotNull(block.getTable());
  }

  // TableBlock.Builder

  @Test
  @DisplayName("builder with table width")
  void builder_withTableWidth() {
    TableBlock block = TableBlock.builder().tableWidth(3).build();

    assertEquals(3, block.getTable().getTableWidth());
  }

  @Test
  @DisplayName("builder with column header")
  void builder_withColumnHeader() {
    TableBlock block = TableBlock.builder().tableWidth(2).hasColumnHeader(true).build();

    assertTrue(block.getTable().isHasColumnHeader());
  }

  @Test
  @DisplayName("builder with row header")
  void builder_withRowHeader() {
    TableBlock block = TableBlock.builder().tableWidth(2).hasRowHeader(true).build();

    assertTrue(block.getTable().isHasRowHeader());
  }

  @Test
  @DisplayName("builder with children consumer")
  void builder_withChildrenConsumer() {
    TableBlock block =
        TableBlock.builder()
            .tableWidth(3)
            .hasColumnHeader(true)
            .rows(
                rows ->
                    rows.row().cell("A").cell("B").cell("C").row().cell("1").cell("2").cell("3"))
            .build();

    assertEquals("table", block.getType());
    assertEquals(3, block.getTable().getTableWidth());
    assertTrue(block.getTable().isHasColumnHeader());
    assertNotNull(block.getTable().getChildren());
    assertEquals(2, block.getTable().getChildren().size());

    TableRowBlock headerRow = block.getTable().getChildren().get(0);
    assertEquals(3, headerRow.getTableRow().getCells().size());
    assertEquals("A", headerRow.getTableRow().getCells().get(0).get(0).getPlainText());
    assertEquals("B", headerRow.getTableRow().getCells().get(1).get(0).getPlainText());
    assertEquals("C", headerRow.getTableRow().getCells().get(2).get(0).getPlainText());
  }

  @Test
  @DisplayName("builder with children list")
  void builder_withChildrenList() {
    TableRowBlock row = TableRowBlock.builder().row().cell("X").cell("Y").build();

    TableBlock block = TableBlock.builder().tableWidth(2).rows(List.of(row)).build();

    assertEquals(1, block.getTable().getChildren().size());
    assertSame(row, block.getTable().getChildren().get(0));
  }

  @Test
  @DisplayName("builder full example")
  void builder_fullExample() {
    TableBlock block =
        TableBlock.builder()
            .tableWidth(3)
            .hasColumnHeader(true)
            .hasRowHeader(false)
            .rows(
                rows ->
                    rows.row()
                        .cell("Mon")
                        .cell("Tue")
                        .cell("Wed")
                        .row()
                        .cell("gym")
                        .cell("run")
                        .cell("bike"))
            .build();

    assertEquals("table", block.getType());
    assertEquals(3, block.getTable().getTableWidth());
    assertTrue(block.getTable().isHasColumnHeader());
    assertFalse(block.getTable().isHasRowHeader());
    assertEquals(2, block.getTable().getChildren().size());
  }

  // Table inner class

  @Test
  @DisplayName("table getter setter")
  void table_getterSetter() {
    TableBlock.Table table = new TableBlock.Table();

    table.setTableWidth(5);
    table.setHasColumnHeader(true);
    table.setHasRowHeader(true);

    assertEquals(5, table.getTableWidth());
    assertTrue(table.isHasColumnHeader());
    assertTrue(table.isHasRowHeader());
  }

  // TableRowBlock constructors

  @Test
  @DisplayName("table row constructor sets type and initializes")
  void tableRow_constructor_setsTypeAndInitializes() {
    TableRowBlock block = new TableRowBlock();

    assertEquals("table_row", block.getType());
    assertNotNull(block.getTableRow());
    assertNotNull(block.getTableRow().getCells());
    assertTrue(block.getTableRow().getCells().isEmpty());
  }

  // TableRowBlock.Builder - single row

  @Test
  @DisplayName("table row builder single row with string cells")
  void tableRowBuilder_singleRow_withStringCells() {
    TableRowBlock row = TableRowBlock.builder().row().cell("A").cell("B").cell("C").build();

    assertEquals("table_row", row.getType());
    assertEquals(3, row.getTableRow().getCells().size());
    assertEquals("A", row.getTableRow().getCells().get(0).get(0).getPlainText());
    assertEquals("B", row.getTableRow().getCells().get(1).get(0).getPlainText());
    assertEquals("C", row.getTableRow().getCells().get(2).get(0).getPlainText());
  }

  @Test
  @DisplayName("table row builder single row with rich text consumer")
  void tableRowBuilder_singleRow_withRichTextConsumer() {
    TableRowBlock row =
        TableRowBlock.builder().row().cell(rt -> rt.plainText("Bold").bold()).build();

    assertEquals(1, row.getTableRow().getCells().size());
    assertTrue(row.getTableRow().getCells().get(0).get(0).getAnnotations().getBold());
  }

  // TableRowBlock.Builder - multiple rows

  @Test
  @DisplayName("table row builder multiple rows build list")
  void tableRowBuilder_multipleRows_buildList() {
    List<TableRowBlock> rows =
        TableRowBlock.builder()
            .row()
            .cell("r1c1")
            .cell("r1c2")
            .row()
            .cell("r2c1")
            .cell("r2c2")
            .buildList();

    assertEquals(2, rows.size());
    assertEquals("r1c1", rows.get(0).getTableRow().getCells().get(0).get(0).getPlainText());
    assertEquals("r2c1", rows.get(1).getTableRow().getCells().get(0).get(0).getPlainText());
  }

  @Test
  @DisplayName("table row builder multiple rows build throws exception")
  void tableRowBuilder_multipleRows_build_throwsException() {
    TableRowBlock.Builder builder = TableRowBlock.builder().row().cell("r1").row().cell("r2");

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("more than one row"));
  }

  // TableRowBlock.Builder - error cases

  @Test
  @DisplayName("table row builder build no rows throws exception")
  void tableRowBuilder_build_noRows_throwsException() {
    TableRowBlock.Builder builder = TableRowBlock.builder();

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("no row defined"));
  }

  @Test
  @DisplayName("table row builder cell without row throws exception")
  void tableRowBuilder_cellWithoutRow_throwsException() {
    TableRowBlock.Builder builder = TableRowBlock.builder();

    assertThrows(IllegalStateException.class, () -> builder.cell("data"));
  }

  @Test
  @DisplayName("table row builder build list empty returns empty list")
  void tableRowBuilder_buildList_emptyReturnsEmptyList() {
    List<TableRowBlock> rows = TableRowBlock.builder().buildList();

    assertNotNull(rows);
    assertTrue(rows.isEmpty());
  }
}
