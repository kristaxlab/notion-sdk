package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/** A Notion file block that displays a downloadable file attachment. */
@Getter
@Setter
public class FileBlock extends Block {

  private FileData file;

  public FileBlock() {
    setType(BlockType.FILE.getValue());
    file = new FileData();
  }
}
