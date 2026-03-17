package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ColumnBlock extends Block {
  @JsonProperty("column")
  private Column column;

  public ColumnBlock() {
    setType("column");
    column = new Column();
  }

  @Data
  public static class Column {
    // No properties, just an empty object
  }
}
