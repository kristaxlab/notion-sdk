package io.kristaxlab.notion.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion table block containing rows and columns of cell data.
 *
 * <p>Tables are composed of {@link TableRowBlock} children. Use {@link #builder()} to configure the
 * table width, header options, and row content.
 */
@Getter
@Setter
public class TableBlock extends Block {

  private Table table;

  public TableBlock() {
    setType(BlockType.TABLE.getValue());
    table = new Table();
  }

  /** The inner content object of a table block. */
  @Getter
  @Setter
  public static class Table {

    private int tableWidth;

    private boolean hasColumnHeader;

    private boolean hasRowHeader;

    private List<TableRowBlock> children;
  }

  /**
   * Returns a new builder for constructing a {@link TableBlock}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link TableBlock}. */
  public static class Builder {

    private Integer tableWidth;

    private boolean hasColumnHeader;

    private boolean hasRowHeader;

    private final List<TableRowBlock> children = new ArrayList<>();

    private Builder() {}

    /**
     * Sets the number of columns in the table.
     *
     * @param tableWidth the column count
     * @return this builder
     */
    public Builder tableWidth(int tableWidth) {
      this.tableWidth = tableWidth;
      return this;
    }

    /**
     * Sets whether the first row is treated as a column header.
     *
     * @param hasColumnHeader {@code true} to enable column headers
     * @return this builder
     */
    public Builder hasColumnHeader(boolean hasColumnHeader) {
      this.hasColumnHeader = hasColumnHeader;
      return this;
    }

    /**
     * Sets whether the first column is treated as a row header.
     *
     * @param hasRowHeader {@code true} to enable row headers
     * @return this builder
     */
    public Builder hasRowHeader(boolean hasRowHeader) {
      this.hasRowHeader = hasRowHeader;
      return this;
    }

    /**
     * Adds table rows using a {@link TableRowBlock.Builder} consumer.
     *
     * @param rowsConsumer a consumer that defines rows via the row builder
     * @return this builder
     */
    public Builder rows(Consumer<TableRowBlock.Builder> rowsConsumer) {
      TableRowBlock.Builder builder = TableRowBlock.builder();
      rowsConsumer.accept(builder);
      this.children.addAll(builder.buildList());
      return this;
    }

    /**
     * Adds table rows from a pre-built list.
     *
     * @param rows the table row blocks to add
     * @return this builder
     */
    public Builder rows(List<TableRowBlock> rows) {
      this.children.addAll(rows);
      return this;
    }

    /**
     * Builds the {@link TableBlock}.
     *
     * <p>If {@link #tableWidth(int)} was never called, the width is inferred from the first row's
     * cell count. An explicit call to {@link #tableWidth(int)} always takes precedence.
     *
     * @return a new TableBlock
     */
    public TableBlock build() {
      TableBlock tableBlock = new TableBlock();
      tableBlock.getTable().setHasColumnHeader(hasColumnHeader);
      tableBlock.getTable().setHasRowHeader(hasRowHeader);
      if (tableWidth == null) {
        if (!children.isEmpty()) {
          tableWidth = children.get(0).getTableRow().getCells().size();
        } else {
          tableWidth = 0;
        }
      }
      tableBlock.getTable().setTableWidth(tableWidth);
      if (!children.isEmpty()) {
        for (TableRowBlock row : children) {
          if (row.getTableRow() == null
              || row.getTableRow().getCells() == null
              || row.getTableRow().getCells().size() != tableWidth) {
            throw new IllegalStateException(
                "All rows must have a cell count equal to the table width of " + tableWidth);
          }
        }
        tableBlock.getTable().setChildren(new ArrayList<>(children));
      }

      return tableBlock;
    }
  }
}
