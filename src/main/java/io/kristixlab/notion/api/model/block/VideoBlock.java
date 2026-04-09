package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion video block that displays an embedded video.
 *
 * <p>Simple construction via {@link #of(FileData)}.
 */
@Getter
@Setter
public class VideoBlock extends Block {

  private FileData video;

  public VideoBlock() {
    setType("video");
    video = new FileData();
  }

  /**
   * Creates a video block from the given file data.
   *
   * @param fileData the video file data (external URL or Notion-hosted file)
   * @return a new VideoBlock
   */
  public static VideoBlock of(FileData fileData) {
    VideoBlock videoBlock = new VideoBlock();
    videoBlock.setVideo(fileData);
    return videoBlock;
  }
}
