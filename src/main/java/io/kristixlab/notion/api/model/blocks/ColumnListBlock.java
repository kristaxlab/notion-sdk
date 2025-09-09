package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ColumnListBlock extends Block {
  @JsonProperty("column_list")
  private ColumnList columnList;

  @Data
  public static class ColumnList {
    // No properties, just an empty object
  }
}
