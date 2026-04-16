package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a page/database cover payload in Notion requests and responses.
 *
 * <p>Use {@link #external(String)} for URL-based covers and {@link #fileUpload(String)} for
 * pre-uploaded assets.
 */
@Getter
@Setter
public class Cover {

  private String type;

  private ExternalFile external;

  /** Only in requests */
  private FileUploadRef fileUpload;

  /** Only in responses */
  private NotionFile file;

  /**
   * Builds an external cover payload.
   *
   * @param url externally hosted image URL
   * @return cover payload with {@code external} type
   */
  public static Cover external(String url) {
    Cover fileData = new Cover();
    fileData.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  /**
   * Builds a file-upload cover payload.
   *
   * @param fileUploadId uploaded file identifier
   * @return cover payload with {@code file_upload} type
   */
  public static Cover fileUpload(String fileUploadId) {
    Cover fileData = new Cover();
    fileData.setType("file_upload");
    FileUploadRef fileUpload = new FileUploadRef();
    fileUpload.setId(fileUploadId);
    fileData.setFileUpload(fileUpload);
    return fileData;
  }
}
