package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkPreviewBlock extends Block {

  private LinkPreview linkPreview;

  public LinkPreviewBlock() {
    setType("link_preview");
    linkPreview = new LinkPreview();
  }

  @Getter
  @Setter
  public static class LinkPreview {

    private String url;
  }
}
