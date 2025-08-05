package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class TableRowBlock extends Block {
  @JsonProperty("table_row")
  private TableRow tableRow;

  @Data
  public static class TableRow {
    @JsonProperty("cells")
    private List<List<RichText>> cells;
  }
}
