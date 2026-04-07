package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileBlock extends Block {

  private FileData file;

  public FileBlock() {
    setType("file");
    file = new FileData();
  }
}
