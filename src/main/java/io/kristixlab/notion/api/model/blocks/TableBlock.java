package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class TableBlock extends Block {

  @JsonProperty("table")
  private Table table;

  public TableBlock() {
    setType("table");
    table = new Table();
  }

  @Data
  public static class Table {

    @JsonProperty("table_width")
    private int tableWidth;

    @Accessors(fluent = true)
    @JsonProperty("has_column_header")
    private boolean hasColumnHeader;

    @Accessors(fluent = true)
    @JsonProperty("has_row_header")
    private boolean hasRowHeader;

    @JsonProperty("children")
    private List<Block> children;
  }
}
