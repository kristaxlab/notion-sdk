package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableOfContentsBlock extends Block {

  private TableOfContents tableOfContents;

  public TableOfContentsBlock() {
    setType("table_of_contents");
    tableOfContents = new TableOfContents();
  }

  @Getter
  @Setter
  public static class TableOfContents {

    private String color;
  }
}
