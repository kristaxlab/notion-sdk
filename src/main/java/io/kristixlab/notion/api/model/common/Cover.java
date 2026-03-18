package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * The Cover object represents the cover in responses with a page or a database.
 * For requests see CoverParams.
 */
@Data
public class Cover {

  @JsonProperty("type")
  private String type;

  @JsonProperty("external")
  private ExternalFile external;

  @JsonProperty("file_upload")
  private FileUploadRef fileUpload;

  public static Cover fromExternalUrl(String url) {
    Cover fileData = new Cover();
    fileData.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static Cover fromFileUpload(String fileUploadId) {
    Cover fileData = new Cover();
    fileData.setType("file_upload");
    FileUploadRef fileUpload = new FileUploadRef();
    fileUpload.setId(fileUploadId);
    fileData.setFileUpload(fileUpload);
    return fileData;
  }
}
