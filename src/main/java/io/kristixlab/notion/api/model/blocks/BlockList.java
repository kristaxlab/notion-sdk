package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockList extends NotionList<Block> {

  private Object block;

  public BlockList() {
    setType("block");
    block = new Object();
  }
}
