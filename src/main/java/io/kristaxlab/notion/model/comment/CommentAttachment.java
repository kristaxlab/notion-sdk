package io.kristaxlab.notion.model.comment;

import io.kristaxlab.notion.model.common.NotionFile;
import lombok.Getter;
import lombok.Setter;

/**
 * File attached to a comment.
 *
 * <p>Create requests send {@code file_upload_id} and optional {@code type} {@code file_upload}.
 * Responses return {@code category} and a {@link NotionFile}.
 *
 * @see <a href="https://developers.notion.com/reference/comment-attachment">Comment attachment</a>
 */
@Getter
@Setter
public class CommentAttachment {

  /** File upload identifier used when attaching a file on create. */
  private String fileUploadId;

  /** Request type token. Notion accepts {@code file_upload}. */
  private String type;

  /**
   * Response category of the attached file: {@code audio}, {@code image}, {@code pdf}, {@code
   * productivity}, or {@code video}.
   */
  private String category;

  /** Hosted file metadata returned on read. */
  private NotionFile file;

  /**
   * Creates a comment attachment that references an uploaded file.
   *
   * @param fileUploadId identifier of a file upload with status {@code uploaded}
   * @return comment attachment for a create request
   */
  public static CommentAttachment fileUpload(String fileUploadId) {
    CommentAttachment attachment = new CommentAttachment();
    attachment.setFileUploadId(fileUploadId);
    attachment.setType("file_upload");
    return attachment;
  }
}
