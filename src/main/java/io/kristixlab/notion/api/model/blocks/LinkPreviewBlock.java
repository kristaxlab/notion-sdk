package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkPreviewBlock extends Block {
  @JsonProperty("link_preview")
  private LinkPreview linkPreview;

  public LinkPreviewBlock() {
    setType("link_preview");
    linkPreview = new LinkPreview();
  }

  @Data
  public static class LinkPreview {
    @JsonProperty("url")
    private String url;
  }
}
