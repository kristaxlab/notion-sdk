package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionList;

public class Blocks extends NotionList<Block> {

  @JsonProperty("block")
  private Object block;
}
