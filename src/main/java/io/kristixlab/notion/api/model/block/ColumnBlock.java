package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.helper.BlocksBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion column block representing a single column within a {@link ColumnListBlock}.
 *
 * <p>Each column contains child blocks as its content and an optional width ratio. Use {@link
 * #builder()} to construct one or more columns fluently, then pass them to a {@link
 * ColumnListBlock}.
 */
@Getter
@Setter
public class ColumnBlock extends Block {

  private Column column;

  public ColumnBlock() {
    setType("column");
    column = new Column();
  }

  /** The inner content object of a column block. */
  @Getter
  @Setter
  public static class Column {

    private Double widthRatio;

    private List<Block> children = new ArrayList<>();
  }

  /**
   * Creates an empty column block with no children.
   *
   * @return a new ColumnBlock
   */
  public static ColumnBlock of() {
    return new ColumnBlock();
  }

  /**
   * Returns a new builder for constructing one or more {@link ColumnBlock} instances.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for constructing a single {@link ColumnBlock} or a list of columns.
   *
   * <p>Call {@link #build()} when exactly one column has been defined, or {@link #buildList()} to
   * retrieve all accumulated columns.
   */
  public static class Builder {

    private final List<ColumnBlock> columns = new ArrayList<>();

    private Builder() {}

    /**
     * Adds a column whose content is defined by a {@link BlocksBuilder} consumer.
     *
     * @param contentConsumer a consumer that populates the column content
     * @return this builder
     */
    public Builder column(Consumer<BlocksBuilder> contentConsumer) {
      return column(null, contentConsumer);
    }

    /**
     * Adds an empty column with no children.
     *
     * @return this builder
     */
    public Builder emptyColumn() {
      columns.add(ColumnBlock.of());
      return this;
    }

    /**
     * Adds a column containing a single empty paragraph block.
     *
     * @return this builder
     */
    public Builder blankColumn() {
      return column(b -> b.paragraph(""));
    }

    /**
     * Adds a column with an optional width ratio and content defined by a consumer.
     *
     * @param widthRatio the column width ratio, or {@code null} for default sizing
     * @param contentConsumer a consumer that populates the column content
     * @return this builder
     */
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

    /**
     * Sets the width ratio on the most recently added column.
     *
     * @param ratio the width ratio (e.g., {@code 0.5} for half-width)
     * @return this builder
     * @throws IllegalStateException if no columns have been added yet
     */
    public Builder widthRatio(double ratio) {
      if (columns.isEmpty()) {
        throw new IllegalStateException(
            "No columns defined to set width ratio on, set up a column first");
      }
      columns.get(columns.size() - 1).getColumn().setWidthRatio(ratio);
      return this;
    }

    /**
     * Sets width ratios for all columns by computing proportional fractions from integer weights.
     *
     * @param widthRatios integer weights for each column (e.g., {@code 1, 2, 1} for 25%/50%/25%)
     * @return this builder
     * @throws IllegalArgumentException if the number of ratios does not match the number of columns
     */
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

    /**
     * Sets explicit width ratios for all columns.
     *
     * @param widthRatios the width ratios for each column
     * @return this builder
     * @throws IllegalArgumentException if the number of ratios does not match the number of columns
     */
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

    /**
     * Builds a single {@link ColumnBlock}. Use this when exactly one column has been defined.
     *
     * @return the single column block
     * @throws IllegalStateException if no columns have been defined, or more than one column exists
     */
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

    /**
     * Builds the list of all accumulated {@link ColumnBlock} instances.
     *
     * @return a new list containing all columns
     */
    public List<ColumnBlock> buildList() {
      return new ArrayList<>(columns);
    }
  }
}
