package io.kristixlab.notion.api.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion column list block that arranges multiple {@link ColumnBlock} instances side by side.
 *
 * <p>Use {@link #builder()} to construct a column layout with fluent column definitions.
 */
@Getter
@Setter
public class ColumnListBlock extends Block {

  private ColumnList columnList;

  public ColumnListBlock() {
    setType("column_list");
    columnList = new ColumnList();
  }

  /** The inner content object of a column list block. */
  @Getter
  @Setter
  public static class ColumnList {

    private List<ColumnBlock> children;
  }

  /**
   * Returns a new builder for constructing a {@link ColumnListBlock}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link ColumnListBlock}. */
  public static class Builder {

    private final ColumnListBlock block;

    private Builder() {
      block = new ColumnListBlock();
    }

    /**
     * Adds columns using a {@link ColumnBlock.Builder} consumer.
     *
     * @param columnsConsumer a consumer that defines columns via the column builder
     * @return this builder
     */
    public Builder children(Consumer<ColumnBlock.Builder> columnsConsumer) {
      ColumnBlock.Builder builder = ColumnBlock.builder();
      columnsConsumer.accept(builder);
      getChildren().addAll(builder.buildList());
      return this;
    }

    /**
     * Adds columns from a pre-built list.
     *
     * @param children the column blocks to add
     * @return this builder
     */
    public Builder children(List<ColumnBlock> children) {
      getChildren().addAll(children);
      return this;
    }

    private List<ColumnBlock> getChildren() {
      if (block.getColumnList().getChildren() == null) {
        block.getColumnList().setChildren(new ArrayList<>());
      }
      return block.getColumnList().getChildren();
    }

    /**
     * Builds the {@link ColumnListBlock}.
     *
     * @return a new ColumnListBlock
     */
    public ColumnListBlock build() {
      return block;
    }
  }
}
