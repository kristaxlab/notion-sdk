package io.kristixlab.notion.api.model.blocks;

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
    // No properties, just an empty object
  }
}
