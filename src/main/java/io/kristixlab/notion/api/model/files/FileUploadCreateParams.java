package io.kristixlab.notion.api.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Request object for uploading a file to Notion. */
@Data
public class FileUploadCreateParams {

  /** multi_part / external_url / single_part */
  @JsonProperty("mode")
  private String mode;

  /**
   * Name of the file to be created. Required when mode is multi_part or external_url. Otherwise
   * optional, and used to override the filename. Must include an extension, or have one inferred
   * from the content_type parameter.
   */
  @JsonProperty("filename")
  private String filename;

  /** The MIME type of the file. */
  @JsonProperty("content_type")
  private String contentType;

  /**
   * When mode is multi_part, the number of parts you are uploading. Must be between 1 and 1,000.
   * This must match the number of parts as well as the final part_number you send.
   *
   * <p>Not allowed when mode is single_part or external_url.
   */
  @JsonProperty("number_of_parts")
  private Integer numberOfParts;

  /**
   * When mode is external_url, provide the HTTPS URL of a publicly accessible file to import into
   * your workspace.
   */
  @JsonProperty("external_url")
  private String externalUrl;
}
