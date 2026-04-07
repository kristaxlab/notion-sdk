package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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

    private List<List<RichText>> cells;
  }
}
