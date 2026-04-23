package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.file.*;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadList;
import io.kristaxlab.notion.model.file.FileUploadSendParams;

/**
 * Multipart file uploads: create session, send parts, complete, list, retrieve.
 *
 * @see <a href="https://developers.notion.com/reference/file-uploads">Notion File Uploads API</a>
 */
public interface FileUploadsEndpoint {
  /**
   * Starts an upload session (returns {@code file_upload} id).
   *
   * @param request upload-create payload
   * @return created upload session
   */
  FileUpload create(FileUploadCreateParams request);

  /**
   * Uploads a part or chunk for the given upload id.
   *
   * @param fileUploadId upload session id
   * @param request upload-send payload
   * @return updated upload session details
   */
  FileUpload upload(String fileUploadId, FileUploadSendParams request);

  /**
   * Finalizes the upload after all parts are sent.
   *
   * @param fileUploadId upload session id
   * @return finalized upload session
   */
  FileUpload complete(String fileUploadId);

  /**
   * Loads a single upload record by id.
   *
   * @param fileUploadId upload session id
   * @return upload record
   */
  FileUpload retrieve(String fileUploadId);

  /**
   * Lists uploads (first page).
   *
   * @return paginated upload list
   */
  FileUploadList listFileUploads();

  /**
   * Lists uploads filtered by status (e.g. {@code completed}).
   *
   * @param status status filter
   * @return paginated upload list
   */
  FileUploadList listFileUploads(String status);

  /**
   * Lists uploads with pagination.
   *
   * @param startCursor pagination cursor
   * @param pageSize page size
   * @return paginated upload list
   */
  FileUploadList listFileUploads(String startCursor, Integer pageSize);

  /**
   * Lists uploads with status filter and pagination.
   *
   * @param status status filter
   * @param startCursor pagination cursor
   * @param pageSize page size
   * @return paginated upload list
   */
  FileUploadList listFileUploads(String status, String startCursor, Integer pageSize);
}
