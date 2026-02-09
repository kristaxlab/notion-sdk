package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.files.*;

/**
 * Interface defining operations for Notion File Uploads.
 *
 * @see <a href="https://developers.notion.com/reference/file-uploads">Notion File Uploads API</a>
 */
public interface FileUploadsEndpoint {
  FileUploadResponse createFileUpload(FileUploadCreateParams request);

  FileUploadResponse sendFileContent(String fileUploadId, FileUploadSendParams request);

  FileUploadResponse completeFileUpload(String fileUploadId);

  FileUploadResponse retrieveFileUpload(String fileUploadId);

  FileUploadList listFileUploads();

  FileUploadList listFileUploads(String status);

  FileUploadList listFileUploads(String startCursor, Integer pageSize);

  FileUploadList listFileUploads(String status, String startCursor, Integer pageSize);
}
