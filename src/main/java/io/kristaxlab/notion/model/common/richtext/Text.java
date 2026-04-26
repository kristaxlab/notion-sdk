package io.kristaxlab.notion.model.common.richtext;

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
