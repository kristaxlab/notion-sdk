package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoBlock extends Block {

  private FileData video;

  public VideoBlock() {
    setType("video");
    video = new FileData();
  }

  public static VideoBlock of(FileData fileData) {
    VideoBlock videoBlock = new VideoBlock();
    videoBlock.setVideo(fileData);
    return videoBlock;
  }
}
