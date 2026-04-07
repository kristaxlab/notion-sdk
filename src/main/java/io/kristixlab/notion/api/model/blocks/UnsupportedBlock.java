package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsupportedBlock extends Block {

  private Unsupported unsupported;

  @Getter
  @Setter
  public class Unsupported {

    private String blockType;
  }
}
