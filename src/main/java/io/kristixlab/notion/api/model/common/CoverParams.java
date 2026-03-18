package io.kristixlab.notion.api.model.common;

import lombok.Data;

@Data
public class CoverParams {

  private String type;

  private FileUpload file_upload;

  private ExternalFile external;

  public static CoverParams fromFileUpload(FileUpload fileUpload) {
    CoverParams coverParams = new CoverParams();
    coverParams.setType("file");
    coverParams.setFile_upload(fileUpload);
    return coverParams;
  }

  public static CoverParams fromExternalUrl(String url) {
    CoverParams coverParams = new CoverParams();
    coverParams.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    coverParams.setExternal(external);
    return coverParams;
  }
}
