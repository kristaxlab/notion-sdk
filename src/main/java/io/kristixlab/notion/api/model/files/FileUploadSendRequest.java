package io.kristixlab.notion.api.model.files;

import lombok.Data;

/**
 * Request object for sending file content to Notion. This is used for multipart/form-data requests,
 * not JSON.
 */
@Data
public class FileUploadSendRequest {

  /**
   * The raw binary file contents to upload. This should be sent as a file in multipart/form-data,
   * not JSON.
   */
  private byte[] file;

  /**
   * When using a mode=multi_part File Upload to send files greater than 20 MB in parts, this is the
   * current part number. Must be an integer between 1 and 1000 provided as a string form field.
   */
  private Integer partNumber;

  /**
   * Content type of the file being uploaded, e.g., "image/png" or "application/pdf".
   */
  private String contentType;
}
