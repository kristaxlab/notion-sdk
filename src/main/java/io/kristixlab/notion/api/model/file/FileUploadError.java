package io.kristixlab.notion.api.model.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadError {

  /**
   * The type of error that occurred during file import.
   *
   * <p>Available options: validation_error, internal_system_error, download_error, upload_error
   */
  private String type;

  /** A short string code representing the error. */
  private String code;

  /** A human-readable message describing the error. */
  private String message;

  /** The parameter related to the error, if applicable. Null if not applicable. */
  private String parameter;

  /** The HTTP status code associated with the error, if available. Null if not applicable. */
  private Integer statusCode;
}
