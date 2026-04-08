package io.kristixlab.notion.api.model.file;

import io.kristixlab.notion.api.model.BaseNotionObject;
import io.kristixlab.notion.api.model.users.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for file upload request.
 *
 * <p>Contains the file upload information including pre-signed URL and metadata.
 */
// TODO comparing to NotionObject: parent, lastEditedBy are missing
@Getter
@Setter
public class FileUpload extends BaseNotionObject {

  /** The unique identifier for the file upload. */
  private String id;

  /** The time when the file upload was created. */
  private String createdTime;

  /** The user who created the file upload. */
  private User createdBy;

  /** The time when the file upload was last edited. */
  private String lastEditedTime;

  /** Whether the file upload is archived. */
  private Boolean inTrash;

  /** The expiration time for the upload URL. */
  private String expiryTime;

  /** The status of the file upload (e.g., "pending"). */
  private String status;

  /** The name of the file. */
  private String filename;

  /** The MIME type of the file. */
  private String contentType;

  /** The size of the file in bytes. */
  private Long contentLength;

  /** The pre-signed URL where the file should be uploaded. */
  private String uploadUrl;

  private NumberOfParts numberOfParts;

  /** The URL to access the completed file upload. */
  private String completeUrl;

  private FileImportResult fileImportResult;

  @Getter
  @Setter
  public static class FileImportResult {
    /** ISO-8601 */
    private String importedTime;

    private String type;

    /** Empty object. */
    private Object success;

    /** The reason for failure, if applicable. */
    private FileUploadError error;
  }

  @Getter
  @Setter
  public static class NumberOfParts {
    /** The total number of parts the file was split into for upload. */
    private Integer total;

    /** The part number of the current upload part. */
    private Integer sent;
  }
}
