package io.kristixlab.notion.api.model.files;

import java.io.File;
import java.io.InputStream;
import lombok.Data;

/**
 * Request object for sending file content to Notion. This is used for multipart/form-data requests,
 * not JSON.
 */
@Data
public class FileUploadSendParams {

  /** InputStream to be uploaded. One of 3 must present: inputStream, bytes or file */
  private InputStream inputStream;

  /** Bytes to be uploaded. One of 3 must present: inputStream, bytes or file */
  private byte[] bytes;

  /** File to be uploaded. One of 3 must present: inputStream, bytes or file */
  private File file;

  /** Content type of the file being uploaded, e.g., "image/png" or "application/pdf" */
  private String contentType;

  /** Filename */
  private String fileName;

  /** Part number of the file being uploaded. Optional */
  private Integer partNumber;
}
