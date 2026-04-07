package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.NotionList;

public class BlockList extends NotionList<Block> {

  private Object block;

  public BlockList() {
    setType("block");
    block = new Object();
  }
}
