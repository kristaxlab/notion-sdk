package io.kristaxlab.notion.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * A read-only Notion link preview block. Returned by the API when a URL is pasted inline and
 * rendered as a preview; cannot be created via the API.
 */
@Getter
@Setter
public class LinkPreviewBlock extends Block {

  private LinkPreview linkPreview;

  public LinkPreviewBlock() {
    setType(BlockType.LINK_PREVIEW.getValue());
    linkPreview = new LinkPreview();
  }

  /** The inner content object of a link preview block. */
  @Getter
  @Setter
  public static class LinkPreview {

    private String url;
  }
}
