package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.helper.BlocksBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnBlock extends Block {

  private Column column;

  public ColumnBlock() {
    setType("column");
    column = new Column();
  }

  @Getter
  @Setter
  public static class Column {

    private Double widthRatio;

    private List<Block> children = new ArrayList<>();
  }

  public static ColumnBlock of() {
    return new ColumnBlock();
  }

  /**
   * Fluent builder for constructing a single {@link ColumnBlock} or a list of {@link ColumnBlock}s.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Fluent builder for accumulating list of {@link ColumnBlock} */
  public static class Builder {

    private final List<ColumnBlock> columns = new ArrayList<>();

    private Builder() {}

    /** Adds a column whose content is defined by a {@link BlocksBuilder} consumer. */
    public Builder column(Consumer<BlocksBuilder> contentConsumer) {
      return column(null, contentConsumer);
    }

    /** Adds empty column */
    public Builder emptyColumn() {
      columns.add(ColumnBlock.of());
      return this;
    }

    /** Adds a column with paragraph block without content. */
    public Builder blankColumn() {
      return column(b -> b.paragraph(""));
    }

    public Builder column(Double widthRatio, Consumer<BlocksBuilder> contentConsumer) {
      BlocksBuilder contentBuilder = BlocksBuilder.of();
      if (contentConsumer != null) {
        contentConsumer.accept(contentBuilder);
      }
      ColumnBlock col = new ColumnBlock();
      col.getColumn().setChildren(contentBuilder.build());
      if (widthRatio != null) {
        col.getColumn().setWidthRatio(widthRatio);
      }
      columns.add(col);
      return this;
    }

    public Builder widthRatio(double ratio) {
      if (columns.isEmpty()) {
        throw new IllegalStateException(
            "No columns defined to set width ratio on, set up a column first");
      }
      columns.get(columns.size() - 1).getColumn().setWidthRatio(ratio);
      return this;
    }

    public Builder widthRatios(int... widthRatios) {
      if (widthRatios.length != columns.size()) {
        throw new IllegalArgumentException(
            String.format(
                "Number of width ratios must match number of columns: %s != %s",
                widthRatios.length, columns.size()));
      }
      int sum = Arrays.stream(widthRatios).sum();
      double[] ratios = new double[widthRatios.length];
      for (int i = 0; i < widthRatios.length; i++) {
        double ratio = widthRatios[i] / sum;
        ratios[i] = widthRatios[i] / (double) sum;
        ;
      }
      return widthRatios(ratios);
    }

    public Builder widthRatios(double... widthRatios) {
      if (widthRatios.length != columns.size()) {
        throw new IllegalArgumentException(
            String.format(
                "Number of width ratios must match number of columns: %s != %s",
                widthRatios.length, columns.size()));
      }

      List<Double> ratios = new ArrayList<>();
      for (int i = 0; i < widthRatios.length; i++) {
        columns.get(i).getColumn().setWidthRatio(widthRatios[i]);
      }
      return this;
    }

    public ColumnBlock build() {
      if (columns.isEmpty()) {
        throw new IllegalStateException("There is no column defined in the builder");
      }
      if (columns.size() > 1) {
        throw new IllegalStateException(
            "The builder contains more than one column, use buildList() instead");
      }
      return columns.get(0);
    }

    public List<ColumnBlock> buildList() {
      return new ArrayList<>(columns);
    }
  }
}
