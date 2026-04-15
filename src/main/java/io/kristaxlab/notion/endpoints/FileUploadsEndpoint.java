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
  /** Starts an upload session (returns {@code file_upload} id). */
  FileUpload create(FileUploadCreateParams request);

  /** Uploads a part or chunk for the given upload id. */
  FileUpload upload(String fileUploadId, FileUploadSendParams request);

  /** Finalizes the upload after all parts are sent. */
  FileUpload complete(String fileUploadId);

  /** Loads a single upload record by id. */
  FileUpload retrieve(String fileUploadId);

  /** Lists uploads (first page). */
  FileUploadList listFileUploads();

  /** Lists uploads filtered by status (e.g. {@code completed}). */
  FileUploadList listFileUploads(String status);

  /** Lists uploads with pagination. */
  FileUploadList listFileUploads(String startCursor, Integer pageSize);

  /** Lists uploads with status filter and pagination. */
  FileUploadList listFileUploads(String status, String startCursor, Integer pageSize);
}
