package io.kristixlab.notion.api.model.common.richtext;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Text {

  private String content;

  private Link link;

  @Getter
  @Setter
  public static class Link {

    private String url;
  }
}
