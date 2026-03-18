package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IconParams {

  @JsonProperty("type")
  private String type;

  @JsonProperty("emoji")
  private String emoji;

  @JsonProperty("custom_emoji")
  private CustomEmoji customEmoji;

  @JsonProperty("external")
  private ExternalFile external;

  @JsonProperty("file_upload")
  private FileUpload fileUpload;

  public static IconParams fromExternalUrl(String url) {
    IconParams icon = new IconParams();
    icon.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    icon.setExternal(external);
    return icon;
  }

  public static IconParams fromEmoji(String emoji) {
    IconParams icon = new IconParams();
    icon.setType("emoji");
    icon.setEmoji(emoji);
    return icon;
  }

  public static IconParams fromFileUpload(String fileUploadId) {
    IconParams icon = new IconParams();
    icon.setType("file_upload");
    FileUpload fu = new FileUpload();
    fu.setId(fileUploadId);
    icon.setFileUpload(fu);
    return icon;
  }
}
