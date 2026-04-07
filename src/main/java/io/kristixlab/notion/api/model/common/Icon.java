package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Icon {

  private String type;

  private String emoji;

  private CustomEmoji customEmoji;

  private ExternalFile external;

  private File file;

  // TODO
  public static Icon emoji(String emoji) {
    Icon icon = new Icon();
    icon.setType("emoji");
    icon.setEmoji(emoji);
    return icon;
  }
}
