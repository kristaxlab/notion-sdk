package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion image block that displays an image from an external URL or Notion-hosted file.
 *
 * <p>Create instances with the default constructor and assign the {@code image} payload.
 */
@Getter
@Setter
public class ImageBlock extends Block {

  private FileData image;

  /** Creates an image block initialized with an empty {@link FileData} payload. */
  public ImageBlock() {
    setType(BlockType.IMAGE.getValue());
    image = new FileData();
  }
}
