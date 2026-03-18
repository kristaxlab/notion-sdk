package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * The Icon object represents the icon in responses with pages, some blocks (ex.callout) or database.
 * For requests see IconParams.
 */
@Data
public class Icon {

  @JsonProperty("type")
  private String type;

  @JsonProperty("emoji")
  private String emoji;

  @JsonProperty("custom_emoji")
  private CustomEmoji customEmoji;

  @JsonProperty("external")
  private ExternalFile external;

  @JsonProperty("file")
  private File file;
}
