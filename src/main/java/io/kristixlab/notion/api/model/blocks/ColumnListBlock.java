package io.kristixlab.notion.api.model.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnListBlock extends Block {

  private ColumnList columnList;

  public ColumnListBlock() {
    setType("column_list");
    columnList = new ColumnList();
  }

  @Getter
  @Setter
  public static class ColumnList {

    private List<ColumnBlock> children;
  }

  /** Returns a new builder for constructing a {@link ColumnListBlock}. */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final ColumnListBlock block;

    private Builder() {
      block = new ColumnListBlock();
    }

    public Builder children(Consumer<ColumnBlock.Builder> columnsConsumer) {
      ColumnBlock.Builder builder = ColumnBlock.builder();
      columnsConsumer.accept(builder);
      getChildren().addAll(builder.buildList());
      return this;
    }

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

    public ColumnListBlock build() {
      return block;
    }
  }
}
