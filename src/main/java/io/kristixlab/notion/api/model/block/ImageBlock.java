package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion image block that displays an image from an external URL or Notion-hosted file.
 *
 * <p>Simple construction via {@link #of(FileData)}.
 */
@Getter
@Setter
public class ImageBlock extends Block {

  private FileData image;

  public ImageBlock() {
    setType("image");
    image = new FileData();
  }
}
