package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/** A Notion file block that displays a downloadable file attachment. */
@Getter
@Setter
public class FileBlock extends Block {

  private FileData file;

  /** Creates a file block initialized with an empty {@link FileData} payload. */
  public FileBlock() {
    setType(BlockType.FILE.getValue());
    file = new FileData();
  }
}
