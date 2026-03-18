package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.files.*;

/**
 * Interface defining operations for Notion File Uploads.
 *
 * @see <a href="https://developers.notion.com/reference/file-uploads">Notion File Uploads API</a>
 */
public interface FileUploadsEndpoint {
  FileUpload createFileUpload(FileUploadCreateParams request);

  FileUpload sendFileContent(String fileUploadId, FileUploadSendParams request);

  FileUpload completeFileUpload(String fileUploadId);

  FileUpload retrieveFileUpload(String fileUploadId);

  FileUploadList listFileUploads();

  FileUploadList listFileUploads(String status);

  FileUploadList listFileUploads(String startCursor, Integer pageSize);

  FileUploadList listFileUploads(String status, String startCursor, Integer pageSize);
}
