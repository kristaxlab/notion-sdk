package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/** A Notion audio block that displays an embedded audio player. */
@Getter
@Setter
public class AudioBlock extends Block {

  private FileData audio;

  /** Creates an audio block initialized with an empty {@link FileData} payload. */
  public AudioBlock() {
    setType(BlockType.AUDIO.getValue());
    audio = new FileData();
  }
}
