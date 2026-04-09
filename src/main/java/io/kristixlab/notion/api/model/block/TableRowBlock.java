package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion table row block.
 *
 * <p>Typically constructed via {@link TableBlock.Builder#children(Consumer)}. For standalone use:
 *
 * <pre>{@code
 * TableRowBlock row = TableRowBlock.builder()
 *     .row()
 *     .cell("Monday").cell("Tuesday").cell("Wednesday")
 *     .build();
 * }</pre>
 */
@Getter
@Setter
public class TableRowBlock extends Block {

  private TableRow tableRow;

  public TableRowBlock() {
    setType("table_row");
    tableRow = new TableRow();
  }

  /** The inner content object of a table row block. */
  @Getter
  @Setter
  public static class TableRow {

    /** Each inner list represents a separate cell. */
    private List<List<RichText>> cells = new ArrayList<>();
  }

  /**
   * Returns a new builder for constructing a single {@link TableRowBlock} or a list of rows.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for constructing one or more {@link TableRowBlock} instances.
   *
   * <p>Call {@link #build()} when exactly one row has been defined, or {@link #buildList()} to
   * retrieve all accumulated rows.
   */
  public static class Builder {

    private final List<TableRowBlock> rows = new ArrayList<>();

    private Builder() {}

    /**
     * Starts a new row. Subsequent {@link #cell} calls add cells to this row.
     *
     * @return this builder
     */
    public Builder row() {
      rows.add(new TableRowBlock());
      return this;
    }

    /** Adds a cell containing a single plain-text run. */
    public Builder cell(String text) {
      getLastRowCells().add(RichText.of(text));
      return this;
    }

    /** Adds a formatted cell via a {@link RichText.Builder} consumer. */
    public Builder cell(Consumer<RichText.Builder> richTextConsumer) {
      RichText.Builder builder = RichText.builder();
      richTextConsumer.accept(builder);
      getLastRowCells().add(builder.buildList());
      return this;
    }

    /** Adds a cell from a pre-built list of rich text. */
    public Builder cell(List<RichText> richText) {
      getLastRowCells().add(richText);
      return this;
    }

    private List<List<RichText>> getLastRowCells() {
      if (rows.isEmpty()) {
        throw new IllegalStateException("There is no row defined in the builder");
      }
      TableRowBlock row = rows.get(rows.size() - 1);
      if (row.getTableRow().getCells() == null) {
        row.getTableRow().setCells(new ArrayList<>());
      }
      return row.getTableRow().getCells();
    }

    /**
     * Builds a single {@link TableRowBlock}. Use this when exactly one row has been defined.
     *
     * @return the single row block
     * @throws IllegalStateException if no rows have been defined, or more than one row exists
     */
    public TableRowBlock build() {
      if (rows.isEmpty()) {
        throw new IllegalStateException("There is no row defined in the builder");
      }
      if (rows.size() > 1) {
        throw new IllegalStateException(
            "The builder contains more than one row, use buildList() instead");
      }
      return rows.get(0);
    }

    /**
     * Builds the list of all accumulated {@link TableRowBlock} instances.
     *
     * @return a new list containing all rows
     */
    public List<TableRowBlock> buildList() {
      return new ArrayList<>(rows);
    }
  }
}
