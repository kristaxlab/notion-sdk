package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

/*
 * The Cover object represents the cover in responses with a page or a database.
 * For requests see CoverParams.
 */
@Getter
@Setter
public class Cover {

  private String type;

  private ExternalFile external;

  private FileUploadRef fileUpload;

  public static Cover external(String url) {
    Cover fileData = new Cover();
    fileData.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static Cover fileUpload(String fileUploadId) {
    Cover fileData = new Cover();
    fileData.setType("file_upload");
    FileUploadRef fileUpload = new FileUploadRef();
    fileUpload.setId(fileUploadId);
    fileData.setFileUpload(fileUpload);
    return fileData;
  }
}
