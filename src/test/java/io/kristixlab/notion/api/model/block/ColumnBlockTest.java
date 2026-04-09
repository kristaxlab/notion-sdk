package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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

  @Test
  void of_createsEmptyColumn() {
    ColumnBlock block = ColumnBlock.of();

    assertEquals("column", block.getType());
    assertNotNull(block.getColumn());
    assertTrue(block.getColumn().getChildren().isEmpty());
    assertNull(block.getColumn().getWidthRatio());
  }

  // ColumnBlock.Builder - single column

  @Test
  void builder_singleColumn_withContent() {
    ColumnBlock col = ColumnBlock.builder().column(b -> b.paragraph("Hello")).build();

    assertEquals("column", col.getType());
    assertEquals(1, col.getColumn().getChildren().size());
    assertInstanceOf(ParagraphBlock.class, col.getColumn().getChildren().get(0));
  }

  @Test
  void builder_singleColumn_withWidthRatio() {
    ColumnBlock col = ColumnBlock.builder().column(0.5, b -> b.paragraph("Half")).build();

    assertEquals(0.5, col.getColumn().getWidthRatio());
    assertEquals(1, col.getColumn().getChildren().size());
  }

  @Test
  void builder_singleColumn_emptyColumn() {
    ColumnBlock col = ColumnBlock.builder().emptyColumn().build();

    assertEquals("column", col.getType());
    assertTrue(col.getColumn().getChildren().isEmpty());
  }

  @Test
  void builder_singleColumn_blankColumn() {
    ColumnBlock col = ColumnBlock.builder().blankColumn().build();

    assertEquals("column", col.getType());
    assertEquals(1, col.getColumn().getChildren().size());
    assertInstanceOf(ParagraphBlock.class, col.getColumn().getChildren().get(0));
  }

  // ColumnBlock.Builder - multiple columns

  @Test
  void builder_multipleColumns_buildList() {
    List<ColumnBlock> cols =
        ColumnBlock.builder()
            .column(b -> b.paragraph("Left"))
            .column(b -> b.paragraph("Right"))
            .buildList();

    assertEquals(2, cols.size());
    assertEquals(1, cols.get(0).getColumn().getChildren().size());
    assertEquals(1, cols.get(1).getColumn().getChildren().size());
  }

  @Test
  void builder_multipleColumns_build_throwsException() {
    ColumnBlock.Builder builder =
        ColumnBlock.builder().column(b -> b.paragraph("One")).column(b -> b.paragraph("Two"));

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("more than one column"));
  }

  @Test
  void builder_noColumns_build_throwsException() {
    ColumnBlock.Builder builder = ColumnBlock.builder();

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("no column defined"));
  }

  @Test
  void builder_buildList_emptyReturnsEmptyList() {
    List<ColumnBlock> cols = ColumnBlock.builder().buildList();

    assertNotNull(cols);
    assertTrue(cols.isEmpty());
  }

  // widthRatio on last column

  @Test
  void builder_widthRatio_setsOnLastColumn() {
    List<ColumnBlock> cols =
        ColumnBlock.builder()
            .column(b -> b.paragraph("Left"))
            .widthRatio(0.3)
            .column(b -> b.paragraph("Right"))
            .widthRatio(0.7)
            .buildList();

    assertEquals(0.3, cols.get(0).getColumn().getWidthRatio());
    assertEquals(0.7, cols.get(1).getColumn().getWidthRatio());
  }

  @Test
  void builder_widthRatio_noColumns_throwsException() {
    ColumnBlock.Builder builder = ColumnBlock.builder();

    assertThrows(IllegalStateException.class, () -> builder.widthRatio(0.5));
  }

  // widthRatios(double...)

  @Test
  void builder_widthRatiosDouble_setsAllRatios() {
    List<ColumnBlock> cols =
        ColumnBlock.builder()
            .column(b -> b.paragraph("A"))
            .column(b -> b.paragraph("B"))
            .column(b -> b.paragraph("C"))
            .widthRatios(0.2, 0.5, 0.3)
            .buildList();

    assertEquals(0.2, cols.get(0).getColumn().getWidthRatio());
    assertEquals(0.5, cols.get(1).getColumn().getWidthRatio());
    assertEquals(0.3, cols.get(2).getColumn().getWidthRatio());
  }

  @Test
  void builder_widthRatiosDouble_mismatchCount_throwsException() {
    ColumnBlock.Builder builder =
        ColumnBlock.builder().column(b -> b.paragraph("A")).column(b -> b.paragraph("B"));

    assertThrows(IllegalArgumentException.class, () -> builder.widthRatios(0.5));
  }

  // widthRatios(int...)

  @Test
  void builder_widthRatiosInt_convertsToFractions() {
    List<ColumnBlock> cols =
        ColumnBlock.builder()
            .column(b -> b.paragraph("Left"))
            .column(b -> b.paragraph("Right"))
            .widthRatios(1, 3)
            .buildList();

    assertEquals(0.25, cols.get(0).getColumn().getWidthRatio(), 0.001);
    assertEquals(0.75, cols.get(1).getColumn().getWidthRatio(), 0.001);
  }

  @Test
  void builder_widthRatiosInt_mismatchCount_throwsException() {
    ColumnBlock.Builder builder = ColumnBlock.builder().emptyColumn().emptyColumn();

    assertThrows(IllegalArgumentException.class, () -> builder.widthRatios(1, 2, 3));
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

  // ColumnListBlock.Builder

  @Test
  void columnListBuilder_withChildrenConsumer() {
    ColumnListBlock block =
        ColumnListBlock.builder()
            .children(
                c -> c.column(col -> col.paragraph("Left")).column(col -> col.paragraph("Right")))
            .build();

    assertEquals("column_list", block.getType());
    assertNotNull(block.getColumnList().getChildren());
    assertEquals(2, block.getColumnList().getChildren().size());
  }

  @Test
  void columnListBuilder_withWidthRatios() {
    ColumnListBlock block =
        ColumnListBlock.builder()
            .children(
                c ->
                    c.column(0.33, col -> col.paragraph("Narrow"))
                        .column(0.67, col -> col.paragraph("Wide")))
            .build();

    assertEquals(2, block.getColumnList().getChildren().size());
    assertEquals(0.33, block.getColumnList().getChildren().get(0).getColumn().getWidthRatio());
    assertEquals(0.67, block.getColumnList().getChildren().get(1).getColumn().getWidthRatio());
  }

  // ColumnList inner class

  @Test
  void columnList_getterSetter() {
    ColumnListBlock.ColumnList cl = new ColumnListBlock.ColumnList();

    assertNull(cl.getChildren());
    List<ColumnBlock> cols = List.of(ColumnBlock.of());
    cl.setChildren(cols);
    assertSame(cols, cl.getChildren());
  }
}
