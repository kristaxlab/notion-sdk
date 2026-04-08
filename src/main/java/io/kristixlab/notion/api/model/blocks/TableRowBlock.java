package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion table row block.
 *
 * <p>Typically constructed inline via {@link TableBlock.TableRowListBuilder#row(Consumer)}. For
 * standalone use:
 *
 * <pre>{@code
 * TableRowBlock row = TableRowBlock.builder()
 *     .cells(c -> c.cell("Monday").cell("Tuesday").cell("Wednesday"))
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

  /** Returns a new builder for constructing a {@link TableRowBlock}. */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final List<List<RichText>> cells = new ArrayList<>();

    private Builder() {}

    /** Populates cells using a {@link CellsBuilder} lambda. Each call adds one cell to the row. */
    public Builder cells(Consumer<CellsBuilder> cellsConsumer) {
      CellsBuilder builder = new CellsBuilder();
      cellsConsumer.accept(builder);
      this.cells.addAll(builder.cells);
      return this;
    }

    public TableRowBlock build() {
      TableRowBlock block = new TableRowBlock();
      block.getTableRow().setCells(new ArrayList<>(cells));
      return block;
    }
  }

  /**
   * Fluent builder for the cells of a table row. Each method appends one cell.
   *
   * <p>{@link #cell(String)} is the shorthand for a plain-text cell; {@link #cell(Consumer)}
   * supports full rich-text formatting within a cell.
   */
  public static class CellsBuilder {

    private final List<List<RichText>> cells = new ArrayList<>();

    private CellsBuilder() {}

    /** Adds a cell containing a single plain-text run. */
    public CellsBuilder cell(String text) {
      cells.add(RichText.of(text));
      return this;
    }

    /** Adds a formatted cell via a {@link RichText.Builder} consumer. */
    public CellsBuilder cell(Consumer<RichText.Builder> richTextConsumer) {
      RichText.Builder builder = RichText.builder();
      richTextConsumer.accept(builder);
      cells.add(builder.buildList());
      return this;
    }

    /** Adds a cell from a pre-built list of rich text. */
    public CellsBuilder cell(List<RichText> richText) {
      cells.add(richText);
      return this;
    }
  }

  @Getter
  @Setter
  public static class TableRow {

    /** Each inner list represents a separate cell. */
    private List<List<RichText>> cells;
  }
}
