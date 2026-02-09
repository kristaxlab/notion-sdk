package io.kristixlab.notion.api.model.pages;

import io.kristixlab.notion.api.model.common.ExternalFile;
import io.kristixlab.notion.api.model.common.FileUpload;
import lombok.Data;

@Data
public class CoverParams {

  private String type;

  private FileUpload file;

  private ExternalFile external;

  public static CoverParams fromFileUpload(FileUpload fileUpload) {
    CoverParams coverParams = new CoverParams();
    coverParams.setType("file");
    coverParams.setFile(fileUpload);
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
