package io.kristixlab.notion.api.model.block;

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
    setType("table");
    table = new Table();
  }

  /**
   * Returns a new builder for constructing a {@link TableBlock}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
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

  /** Builder for {@link TableBlock}. */
  public static class Builder {

    private final TableBlock tableBlock;

    private Builder() {
      this.tableBlock = new TableBlock();
    }

    /**
     * Sets the number of columns in the table.
     *
     * @param tableWidth the column count
     * @return this builder
     */
    public Builder tableWidth(int tableWidth) {
      tableBlock.getTable().setTableWidth(tableWidth);
      return this;
    }

    /**
     * Sets whether the first row is treated as a column header.
     *
     * @param hasColumnHeader {@code true} to enable column headers
     * @return this builder
     */
    public Builder hasColumnHeader(boolean hasColumnHeader) {
      tableBlock.getTable().setHasColumnHeader(hasColumnHeader);
      return this;
    }

    /**
     * Sets whether the first column is treated as a row header.
     *
     * @param hasRowHeader {@code true} to enable row headers
     * @return this builder
     */
    public Builder hasRowHeader(boolean hasRowHeader) {
      tableBlock.getTable().setHasRowHeader(hasRowHeader);
      return this;
    }

    /**
     * Adds table rows using a {@link TableRowBlock.Builder} consumer.
     *
     * @param rowsConsumer a consumer that defines rows via the row builder
     * @return this builder
     */
    public Builder children(Consumer<TableRowBlock.Builder> rowsConsumer) {
      TableRowBlock.Builder builder = TableRowBlock.builder();
      rowsConsumer.accept(builder);
      getChildren().addAll(builder.buildList());
      return this;
    }

    /**
     * Adds table rows from a pre-built list.
     *
     * @param rows the table row blocks to add
     * @return this builder
     */
    public Builder children(List<TableRowBlock> rows) {
      getChildren().addAll(rows);
      return this;
    }

    private List<TableRowBlock> getChildren() {
      if (tableBlock.getTable().getChildren() == null) {
        tableBlock.getTable().setChildren(new ArrayList<>());
      }
      return tableBlock.getTable().getChildren();
    }

    /**
     * Builds the {@link TableBlock}.
     *
     * @return a new TableBlock
     */
    public TableBlock build() {
      return tableBlock;
    }
  }
}
