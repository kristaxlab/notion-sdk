package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion file block that displays a downloadable file attachment.
 *
 * <p>Simple construction via {@link #of(FileData)}.
 */
@Getter
@Setter
public class FileBlock extends Block {

  private FileData file;

  public FileBlock() {
    setType("file");
    file = new FileData();
  }

  /**
   * Creates a file block from the given file data.
   *
   * @param fileData the file data (external URL or Notion-hosted file)
   * @return a new FileBlock
   */
  public static FileBlock of(FileData fileData) {
    FileBlock fileBlock = new FileBlock();
    fileBlock.setFile(fileData);
    return fileBlock;
  }
}
