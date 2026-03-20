package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;

public class BlockList extends NotionListType<Block> {

  @JsonProperty("block")
  private Object block;

  public BlockList() {
    setType("block");
    block = new Object();
  }

  public Blocks wrap() {
    return new Blocks(this);
  }

  // * a wrapper model class to make it easier to work with this model
  public static class Blocks {
    private BlockList dto;

    public Blocks(BlockList dto) {
      this.dto = dto;
    }
  }
}
