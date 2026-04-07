package io.kristixlab.notion.api.model.blocks;

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
    // No properties, just an empty object
  }
}
