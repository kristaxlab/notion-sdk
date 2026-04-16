package io.kristaxlab.notion.model.block;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** A Notion column block representing a single column within a {@link ColumnListBlock}. */
@Getter
@Setter
public class ColumnBlock extends Block {

  private Column column;

  /** Creates a column block initialized with empty child content. */
  public ColumnBlock() {
    setType(BlockType.COLUMN.getValue());
    column = new Column();
  }

  /** The inner content object of a column block. */
  @Getter
  @Setter
  public static class Column {

    private Double widthRatio;

    private List<Block> children = new ArrayList<>();
  }
}
