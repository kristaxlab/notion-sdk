package io.kristixlab.notion.api.model.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBlock extends Block {

  private Table table;

  public TableBlock() {
    setType("table");
    table = new Table();
  }

  /** Returns a new builder for constructing a {@link TableBlock}. */
  public static Builder builder() {
    return new Builder();
  }

  @Getter
  @Setter
  public static class Table {

    private int tableWidth;

    private boolean hasColumnHeader;

    private boolean hasRowHeader;

    private List<TableRowBlock> children;
  }

  public static class Builder {

    private final TableBlock tableBlock;

    private Builder() {
      this.tableBlock = new TableBlock();
    }

    public Builder tableWidth(int tableWidth) {
      tableBlock.getTable().setTableWidth(tableWidth);
      return this;
    }

    public Builder hasColumnHeader(boolean hasColumnHeader) {
      tableBlock.getTable().setHasColumnHeader(hasColumnHeader);
      return this;
    }

    public Builder hasRowHeader(boolean hasRowHeader) {
      tableBlock.getTable().setHasRowHeader(hasRowHeader);
      return this;
    }

    public Builder children(Consumer<TableRowBlock.Builder> rowsConsumer) {
      TableRowBlock.Builder builder = TableRowBlock.builder();
      rowsConsumer.accept(builder);
      getChildren().addAll(builder.buildList());
      return this;
    }

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

    public TableBlock build() {
      return tableBlock;
    }
  }
}
