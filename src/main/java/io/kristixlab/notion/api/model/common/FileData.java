package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

// TODO check if I should do a separation for blocks the way it is done for pages (icon/iconparams)
@Data
public class FileData {

  @JsonProperty("type")
  private String type;

  @JsonProperty("external")
  private ExternalFile external;

  /** TODO check if file and file_upload are mutually exclusive */
  @JsonProperty("file")
  private File file;

  @JsonProperty("file_upload")
  private FileUploadRef fileUpload;

  @JsonProperty("caption")
  private List<RichText> caption;

  @JsonProperty("name")
  private String name;

  public static FileData fromExternalUrl(String url) {
    FileData fileData = new FileData();
    fileData.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static FileData fromFileUpload(String fileUploadId) {
    FileData fileData = new FileData();
    fileData.setType("file_upload");
    FileUploadRef fileUpload = new FileUploadRef();
    fileUpload.setId(fileUploadId);
    fileData.setFileUpload(fileUpload);
    return fileData;
  }
}
