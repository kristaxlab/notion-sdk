package io.kristixlab.notion.api.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionObject;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for file upload request.
 *
 * <p>Contains the file upload information including pre-signed URL and metadata.
 */
// TODO comparing to NotionObject: parent, lastEditedBy are missing
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUpload extends BaseNotionObject {

  /** The unique identifier for the file upload. */
  @JsonProperty("id")
  private String id;

  /** The time when the file upload was created. */
  @JsonProperty("created_time")
  private String createdTime;

  /** The user who created the file upload. */
  @JsonProperty("created_by")
  private User createdBy;

  /** The time when the file upload was last edited. */
  @JsonProperty("last_edited_time")
  private String lastEditedTime;

  /** Whether the file upload is archived. */
  @JsonProperty("in_trash")
  private Boolean inTrash;

  /** The expiration time for the upload URL. */
  @JsonProperty("expiry_time")
  private String expiryTime;

  /** The status of the file upload (e.g., "pending"). */
  @JsonProperty("status")
  private String status;

  /** The name of the file. */
  @JsonProperty("filename")
  private String filename;

  /** The MIME type of the file. */
  @JsonProperty("content_type")
  private String contentType;

  /** The size of the file in bytes. */
  @JsonProperty("content_length")
  private Long contentLength;

  /** The pre-signed URL where the file should be uploaded. */
  @JsonProperty("upload_url")
  private String uploadUrl;

  @JsonProperty("number_of_parts")
  private NumberOfParts numberOfParts;

  /** The URL to access the completed file upload. */
  @JsonProperty("complete_url")
  private String completeUrl;

  @JsonProperty("file_import_result")
  private FileImportResult fileImportResult;

  public static class FileImportResult {
    /** ISO-8601 */
    @JsonProperty("imported_time")
    private String importedTime;

    @JsonProperty("type")
    private String type;

    /** Empty object. */
    @JsonProperty("success")
    private Object success;

    /** The reason for failure, if applicable. */
    @JsonProperty("error")
    private FileUploadError error;
  }

  @Data
  public static class NumberOfParts {
    /** The total number of parts the file was split into for upload. */
    @JsonProperty("total")
    private Integer total;

    /** The part number of the current upload part. */
    @JsonProperty("sent")
    private Integer sent;
  }
}
