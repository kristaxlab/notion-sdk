package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DividerBlock extends Block {

  private Object divider;

  public DividerBlock() {
    setType("divider");
    divider = new Object();
  }
}
