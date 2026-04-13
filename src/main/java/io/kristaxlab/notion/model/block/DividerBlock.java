package io.kristaxlab.notion.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * A Notion divider block that renders a horizontal rule.
 *
 * <p>This block has no configurable content. Simply instantiate it with {@code new DividerBlock()}.
 */
@Getter
@Setter
public class DividerBlock extends Block {

  private Object divider;

  public DividerBlock() {
    setType(BlockType.DIVIDER.getValue());
    divider = new Object();
  }
}
