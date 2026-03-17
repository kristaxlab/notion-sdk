package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Position {

  /* after_block | page_start | page_end */
  @JsonProperty("type")
  public String type;

  public Position() {}

  public Position(String type) {
    this.type = type;
  }

  public Position(String type, String afterBlockId) {
    this.type = type;
    if (!type.equals(PositionType.AFTER_BLOCK.getValue())) {
      throw new IllegalArgumentException("afterBlockId is only allowed when type is 'after_block'");
    }
    this.afterBlock = new AfterBlock();
    this.afterBlock.setId(afterBlockId);
  }

  @JsonProperty("after_block")
  public AfterBlock afterBlock;

  @Data
  public static class AfterBlock {

    @JsonProperty("id")
    public String id;
  }
}
