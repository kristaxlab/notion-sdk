package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
/* TODO handle the mess of types insside */
@Data
public class Icon {

  @JsonProperty("type")
  private String type;

  @JsonProperty("emoji")
  private String emoji;

  @JsonProperty("custom_emoji")
  private CustomEmoji customEmoji;

  @JsonProperty("file")
  private File file;

  @JsonProperty("external")
  private ExternalFile external;

  @Data
  public static class CustomEmoji {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;
  }

  public static Icon fromExternalUrl(String url) {
    Icon icon = new Icon();
    icon.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    icon.setExternal(external);
    return icon;
  }

  public static Icon fromEmoji(String emoji) {
    Icon icon = new Icon();
    icon.setType("emoji");
    icon.setEmoji(emoji);
    return icon;
  }

  public static Icon fromFileUrl(String url) {
    Icon icon = new Icon();
    icon.setType("file");
    File file = new File();
    file.setUrl(url);
    icon.setFile(file);
    return icon;
  }
}
