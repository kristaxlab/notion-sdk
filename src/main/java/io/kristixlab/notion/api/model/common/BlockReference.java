package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockReference {
  @JsonProperty("id")
  private String id;

  public static BlockReference of(String id) {
    BlockReference blockReference = new BlockReference();
    blockReference.setId(id);
    return blockReference;
  }
}
