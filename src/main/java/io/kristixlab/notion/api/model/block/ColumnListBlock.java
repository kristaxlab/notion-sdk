package io.kristixlab.notion.api.model.block;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** A Notion column list block that arranges multiple {@link ColumnBlock} instances side by side. */
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
}
