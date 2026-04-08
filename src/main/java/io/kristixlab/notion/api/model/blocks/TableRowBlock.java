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

  @Getter
  @Setter
  public static class TableRow {

    /** Each inner list represents a separate cell. */
    private List<List<RichText>> cells = new ArrayList<>();
  }

  /**
   * Fluent builder for constructing a single {@link TableRowBlock} or a list of {@link
   * TableRowBlock}s.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final List<TableRowBlock> rows = new ArrayList<>();

    private Builder() {}

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

    public List<TableRowBlock> buildList() {
      return new ArrayList<>(rows);
    }
  }
}
