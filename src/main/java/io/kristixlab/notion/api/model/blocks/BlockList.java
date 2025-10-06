package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;

public class BlockList extends NotionListType<Block> {

  @JsonProperty("block")
  private Object block;
}
