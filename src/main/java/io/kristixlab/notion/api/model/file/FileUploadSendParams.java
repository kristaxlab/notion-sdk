package io.kristixlab.notion.api.model.file;

import java.io.File;
import java.io.InputStream;
import lombok.Getter;
import lombok.Setter;

/**
 * Request object for sending file content to Notion. This is used for multipart/form-data requests,
 * not JSON.
 */
@Getter
@Setter
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

  public static FileUploadSendParams of(
      InputStream inputStream, String contentType, String fileName) {
    FileUploadSendParams params = new FileUploadSendParams();
    params.setInputStream(inputStream);
    params.setContentType(contentType);
    params.setFileName(fileName);
    return params;
  }

  public static FileUploadSendParams of(byte[] bytes, String contentType, String fileName) {
    FileUploadSendParams params = new FileUploadSendParams();
    params.setBytes(bytes);
    params.setContentType(contentType);
    params.setFileName(fileName);
    return params;
  }

  public static FileUploadSendParams of(File file, String contentType, String fileName) {
    FileUploadSendParams params = new FileUploadSendParams();
    params.setFile(file);
    params.setContentType(contentType);
    params.setFileName(fileName);
    return params;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private InputStream inputStream;
    private byte[] bytes;
    private File file;
    private String contentType;
    private String fileName;
    private Integer partNumber;

    public Builder inputStream(InputStream inputStream) {
      if (inputStream == null) {
        throw new IllegalArgumentException("InputStream cannot be null");
      }
      if (bytes != null || file != null) {
        throw new IllegalArgumentException(
            "Only one of file, bytes, or inputStream can be set. bytes and file must be null when inputStream is set.");
      }
      this.inputStream = inputStream;
      return this;
    }

    public Builder bytes(byte[] bytes) {
      if (bytes == null) {
        throw new IllegalArgumentException("Bytes cannot be null");
      }
      if (inputStream != null || file != null) {
        throw new IllegalArgumentException(
            "Only one of file, bytes, or inputStream can be set. inputStream and file must be null when bytes is set.");
      }
      this.bytes = bytes;
      return this;
    }

    public Builder file(File file) {
      if (file == null) {
        throw new IllegalArgumentException("File cannot be null");
      }
      if (bytes != null || inputStream != null) {
        throw new IllegalArgumentException(
            "Only one of file, bytes, or inputStream can be set. bytes and inputStream must be null when file is set.");
      }
      this.file = file;
      return this;
    }

    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder partNumber(Integer partNumber) {
      this.partNumber = partNumber;
      return this;
    }

    public FileUploadSendParams build() {
      FileUploadSendParams params = new FileUploadSendParams();
      params.setInputStream(inputStream);
      params.setBytes(bytes);
      params.setFile(file);
      params.setContentType(contentType);
      params.setFileName(fileName);
      params.setPartNumber(partNumber);
      return params;
    }
  }
}
