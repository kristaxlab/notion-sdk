package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.Color;
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

  public static TableOfContentsBlock of() {
    return new TableOfContentsBlock();
  }

  public static TableOfContentsBlock of(Color color) {
    TableOfContentsBlock block = new TableOfContentsBlock();
    if (color != null) {
      block.getTableOfContents().setColor(color.getValue());
    }
    return block;
  }
}
