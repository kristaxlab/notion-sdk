package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageBlock extends Block {

  private FileData image;

  public ImageBlock() {
    setType("image");
    image = new FileData();
  }
}
