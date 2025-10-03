package io.kristixlab.notion.api.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;

/**
 * Response object for file upload request.
 *
 * <p>Contains the file upload information including pre-signed URL and metadata.
 */
@Data
public class FileUploadResponse extends BaseNotionResponse {

  /**
   * The unique identifier for the file upload.
   */
  @JsonProperty("id")
  private String id;

  /**
   * The time when the file upload was created.
   */
  @JsonProperty("created_time")
  private String createdTime;

  /**
   * The user who created the file upload.
   */
  @JsonProperty("created_by")
  private User createdBy;

  /**
   * The time when the file upload was last edited.
   */
  @JsonProperty("last_edited_time")
  private String lastEditedTime;

  /**
   * The expiration time for the upload URL.
   */
  @JsonProperty("expiry_time")
  private String expiryTime;

  /**
   * The pre-signed URL where the file should be uploaded.
   */
  @JsonProperty("upload_url")
  private String uploadUrl;

  /**
   * Whether the file upload is archived.
   */
  @JsonProperty("archived")
  private Boolean archived;

  /**
   * The status of the file upload (e.g., "pending").
   */
  @JsonProperty("status")
  private String status;

  /**
   * The name of the file.
   */
  @JsonProperty("filename")
  private String filename;

  /**
   * The MIME type of the file.
   */
  @JsonProperty("content_type")
  private String contentType;

  /**
   * The size of the file in bytes.
   */
  @JsonProperty("content_length")
  private Long contentLength;
}
