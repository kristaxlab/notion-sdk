package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/** A Notion video block that displays an embedded video. */
@Getter
@Setter
public class VideoBlock extends Block {

  private FileData video;

  public VideoBlock() {
    setType(BlockType.VIDEO.getValue());
    video = new FileData();
  }
}
