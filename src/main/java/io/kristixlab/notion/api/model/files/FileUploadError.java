package io.kristixlab.notion.api.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileUploadError {

  /**
   * The type of error that occurred during file import.
   *
   * <p>Available options: validation_error, internal_system_error, download_error, upload_error
   */
  @JsonProperty("type")
  private String type;

  /** A short string code representing the error. */
  @JsonProperty("code")
  private String code;

  /** A human-readable message describing the error. */
  @JsonProperty("message")
  private String message;

  /** The parameter related to the error, if applicable. Null if not applicable. */
  @JsonProperty("parameter")
  private String parameter;

  /** The HTTP status code associated with the error, if available. Null if not applicable. */
  @JsonProperty("status_code")
  private Integer statusCode;
}
