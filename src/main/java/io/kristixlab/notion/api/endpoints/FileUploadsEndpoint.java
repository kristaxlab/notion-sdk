package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.file.*;

/**
 * Interface defining operations for Notion File Uploads.
 *
 * @see <a href="https://developers.notion.com/reference/file-uploads">Notion File Uploads API</a>
 */
public interface FileUploadsEndpoint {
  FileUpload create(FileUploadCreateParams request);

  FileUpload upload(String fileUploadId, FileUploadSendParams request);

  FileUpload complete(String fileUploadId);

  FileUpload retrieve(String fileUploadId);

  FileUploadList listFileUploads();

  FileUploadList listFileUploads(String status);

  FileUploadList listFileUploads(String startCursor, Integer pageSize);

  FileUploadList listFileUploads(String status, String startCursor, Integer pageSize);
}
