package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion audio block that displays an embedded audio player.
 *
 * <p>Simple construction via {@link #of(FileData)}.
 */
@Getter
@Setter
public class AudioBlock extends Block {

  private FileData audio;

  public AudioBlock() {
    setType("audio");
    audio = new FileData();
  }

  /**
   * Creates an audio block from the given file data.
   *
   * @param fileData the audio file data (external URL or Notion-hosted file)
   * @return a new AudioBlock
   */
  public static AudioBlock of(FileData fileData) {
    AudioBlock audioBlock = new AudioBlock();
    audioBlock.setAudio(fileData);
    return audioBlock;
  }
}
