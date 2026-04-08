package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion table of contents block that renders an auto-generated outline of the page's headings.
 *
 * <p>Simple construction via {@link #of()} or {@link #of(Color)} for a colored variant.
 */
@Getter
@Setter
public class TableOfContentsBlock extends Block {

  private TableOfContents tableOfContents;

  public TableOfContentsBlock() {
    setType("table_of_contents");
    tableOfContents = new TableOfContents();
  }

  /** The inner content object of a table of contents block. */
  @Getter
  @Setter
  public static class TableOfContents {

    private String color;
  }

  /**
   * Creates a table of contents block with default color.
   *
   * @return a new TableOfContentsBlock
   */
  public static TableOfContentsBlock of() {
    return new TableOfContentsBlock();
  }

  /**
   * Creates a table of contents block with the specified color.
   *
   * @param color the block color, or {@code null} for default
   * @return a new TableOfContentsBlock
   */
  public static TableOfContentsBlock of(Color color) {
    TableOfContentsBlock block = new TableOfContentsBlock();
    if (color != null) {
      block.getTableOfContents().setColor(color.getValue());
    }
    return block;
  }
}
