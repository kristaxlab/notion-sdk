package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockReference {

  private String id;

  public static BlockReference fromId(String id) {
    BlockReference blockReference = new BlockReference();
    blockReference.setId(id);
    return blockReference;
  }
}
