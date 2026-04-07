package io.kristixlab.notion.api.model.blocks;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBlock extends Block {

  private Table table;

  public TableBlock() {
    setType("table");
    table = new Table();
  }

  @Getter
  @Setter
  public static class Table {

    private int tableWidth;

    private boolean hasColumnHeader;

    private boolean hasRowHeader;

    private List<Block> children;
  }
}
