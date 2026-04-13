package io.kristaxlab.notion.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * A Notion breadcrumb block that displays the page's location within the workspace hierarchy.
 *
 * <p>This block has no configurable content. Use {@link #of()} for convenient construction.
 */
@Getter
@Setter
public class BreadcrumbBlock extends Block {

  private Object breadcrumb;

  public BreadcrumbBlock() {
    setType(BlockType.BREADCRUMB.getValue());
    breadcrumb = new Object();
  }
}
