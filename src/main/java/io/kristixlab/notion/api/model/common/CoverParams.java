package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoverParams {

  @JsonProperty("type")
  private String type;

  @JsonProperty("file_upload")
  private FileUploadRef fileUpload;

  @JsonProperty("external")
  private ExternalFile external;

  public static CoverParams fromFileUpload(FileUploadRef fileUpload) {
    CoverParams coverParams = new CoverParams();
    coverParams.setType("file_upload");
    coverParams.setFileUpload(fileUpload);
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
