package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IconParams {

  private String type;

  private String emoji;

  private CustomEmoji customEmoji;

  private ExternalFile external;

  private FileUploadRef fileUpload;

  public static IconParams external(String url) {
    IconParams icon = new IconParams();
    icon.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    icon.setExternal(external);
    return icon;
  }

  public static IconParams emoji(String emoji) {
    IconParams icon = new IconParams();
    icon.setType("emoji");
    icon.setEmoji(emoji);
    return icon;
  }

  public static IconParams fileUpload(String fileUploadId) {
    IconParams icon = new IconParams();
    icon.setType("file_upload");
    FileUploadRef fu = new FileUploadRef();
    fu.setId(fileUploadId);
    icon.setFileUpload(fu);
    return icon;
  }
}
