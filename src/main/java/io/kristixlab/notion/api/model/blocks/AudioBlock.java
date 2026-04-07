package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AudioBlock extends Block {

  private FileData audio;

  public AudioBlock() {
    setType("audio");
    audio = new FileData();
  }
}
