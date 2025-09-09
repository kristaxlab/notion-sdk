package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Icon {

  @JsonProperty("type")
  private String type;

  @JsonProperty("emoji")
  private String emoji;

  @JsonProperty("custom_emoji")
  private CustomEmoji customEmoji;

  @JsonProperty("file")
  private FileData.File file;

  @JsonProperty("external")
  private FileData.External external;

  @Data
  public static class CustomEmoji {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;
  }
}
