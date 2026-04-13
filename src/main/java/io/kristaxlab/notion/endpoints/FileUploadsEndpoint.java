package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.file.*;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadList;
import io.kristaxlab.notion.model.file.FileUploadSendParams;

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
