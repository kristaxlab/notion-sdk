package io.kristixlab.notion.api.model.block;

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
    setType("breadcrumb");
    breadcrumb = new Object();
  }

  /**
   * Creates a new breadcrumb block.
   *
   * @return a new BreadcrumbBlock
   */
  public static BreadcrumbBlock of() {
    return new BreadcrumbBlock();
  }
}
