package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoverParams {

  private String type;

  private FileUploadRef fileUpload;

  private ExternalFile external;

  public static CoverParams external(String url) {
    CoverParams coverParams = new CoverParams();
    coverParams.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    coverParams.setExternal(external);
    return coverParams;
  }

  public static CoverParams fileUpload(FileUploadRef fileUpload) {
    CoverParams coverParams = new CoverParams();
    coverParams.setType("file_upload");
    coverParams.setFileUpload(fileUpload);
    return coverParams;
  }
}
