package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class TableOfContentsBlock extends Block {
  @JsonProperty("table_of_contents")
  private TableOfContents tableOfContents;

  public TableOfContentsBlock() {
    setType("table_of_contents");
    tableOfContents = new TableOfContents();
  }

  @Data
  public static class TableOfContents {
    @JsonProperty("color")
    private String color;
  }
}
