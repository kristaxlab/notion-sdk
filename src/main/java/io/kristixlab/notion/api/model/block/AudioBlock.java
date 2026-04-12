package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/** A Notion audio block that displays an embedded audio player. */
@Getter
@Setter
public class AudioBlock extends Block {

  private FileData audio;

  public AudioBlock() {
    setType(BlockType.AUDIO.getValue());
    audio = new FileData();
  }
}
