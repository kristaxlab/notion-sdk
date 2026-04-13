package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;
import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.FileUploadsEndpoint;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.client.HttpClient.BytesPart;
import io.kristaxlab.notion.http.base.client.HttpClient.FilePart;
import io.kristaxlab.notion.http.base.client.HttpClient.InputStreamPart;
import io.kristaxlab.notion.http.base.client.HttpClient.MultipartBody;
import io.kristaxlab.notion.http.base.client.HttpClient.Part;
import io.kristaxlab.notion.http.base.client.HttpClient.TextPart;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.file.*;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadList;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.util.ArrayList;
import java.util.List;

/**
 * API for file upload operations in Notion.
 *
 * <p>Provides methods for the complete file upload lifecycle: 1. Create a file upload request to
 * get a pre-signed URL 2. Send file content to the pre-signed URL (external to this API) 3.
 * Complete the file upload process 4. Retrieve and list file uploads
 */
public class FileUploadsEndpointImpl extends BaseEndpointImpl implements FileUploadsEndpoint {

  public FileUploadsEndpointImpl(ApiClient client) {
    super(client);
  }

  /**
   * Creates a new file upload and returns a pre-signed URL for uploading the file content. POST
   * https://api.notion.com/v1/file_uploads
   *
   * @param request The file upload request containing file name, content type, and parent
   * @return FileUpload containing the pre-signed URL and file metadata
   * @throws IllegalArgumentException if request or required fields are null/empty
   */
  public FileUpload create(FileUploadCreateParams request) {
    checkNotNull(request, "request");

    if (request.getFilename() == null || request.getFilename().trim().isEmpty()) {
      request.setFilename("filename");
    }
    return getClient().call("POST", ApiPath.from("/file_uploads"), request, FileUpload.class);
  }

  /**
   * Sends file content to the pre-signed URL. This is typically done externally using the
   * upload_url from createFileUpload response. POST
   * https://api.notion.com/v1/file_uploads/{file_upload_id}/send
   *
   * @param fileUploadId The ID of the file upload
   * @param request request
   * @return Response from the file upload service
   */
  public FileUpload upload(String fileUploadId, FileUploadSendParams request) {
    validateRequest(request);
    ApiPath urlInfo =
        ApiPath.builder("/file_uploads/{file_upload_id}/send")
            .pathParam("file_upload_id", fileUploadId)
            .build();

    List<Part> parts = new ArrayList<>();
    String filename = request.getFilename() != null ? request.getFilename() : "filename";
    String contentType = request.getContentType();

    if (request.getFile() != null) {
      parts.add(new FilePart("file", filename, request.getFile(), contentType));
    } else if (request.getBytes() != null) {
      parts.add(new BytesPart("file", filename, request.getBytes(), contentType));
    } else if (request.getInputStream() != null) {
      parts.add(new InputStreamPart("file", filename, request.getInputStream(), contentType));
    }

    if (request.getPartNumber() != null) {
      parts.add(new TextPart("part_number", request.getPartNumber().toString()));
    }
    return getClient().call("POST", urlInfo, new MultipartBody(parts), FileUpload.class);
  }

  /**
   * Use this API to finalize a mode=multi_part file upload after all of the parts have been sent
   * successfully.
   *
   * @param fileUploadId The ID of the file upload to complete
   * @return Updated FileUpload with completion status
   */
  public FileUpload complete(String fileUploadId) {
    checkNotNullOrEmpty(fileUploadId, "fileUploadId");

    ApiPath urlInfo =
        ApiPath.builder("/file_uploads/{file_upload_id}/complete")
            .pathParam("file_upload_id", fileUploadId)
            .build();
    return getClient().call("POST", urlInfo, new Object(), FileUpload.class);
  }

  /**
   * Retrieves a specific file upload by its ID. GET
   * https://api.notion.com/v1/file_uploads/{file_upload_id}
   *
   * @param fileUploadId The ID of the file upload to retrieve
   * @return FileUpload containing the file upload details
   */
  public FileUpload retrieve(String fileUploadId) {
    checkNotNullOrEmpty(fileUploadId, "fileUploadId");

    ApiPath urlInfo =
        ApiPath.builder("/file_uploads/{file_upload_id}")
            .pathParam("file_upload_id", fileUploadId)
            .build();
    return getClient().call("GET", urlInfo, FileUpload.class);
  }

  /**
   * Lists all file uploads in the workspace. GET https://api.notion.com/v1/file_uploads
   *
   * @return FileUploadListResponse containing the list of file uploads
   */
  public FileUploadList listFileUploads() {
    return listFileUploads(null);
  }

  /**
   * Lists all file uploads in the workspace. GET https://api.notion.com/v1/file_uploads
   *
   * @return FileUploadListResponse containing the list of file uploads
   */
  public FileUploadList listFileUploads(String status) {
    return listFileUploads(status, null, null);
  }

  /**
   * Use this API to retrieve file uploads for the current bot integration, sorted by most recent
   * first.
   *
   * @param startCursor Cursor for pagination (can be null for first page)
   * @param pageSize Number of items to return (can be null for default)
   * @return FileUploadListResponse containing the paginated list of file uploads
   */
  public FileUploadList listFileUploads(String startCursor, Integer pageSize) {
    return listFileUploads(null, startCursor, pageSize);
  }

  /**
   * Use this API to retrieve file uploads for the current bot integration, sorted by most recent
   * first.
   *
   * @param status Filter file uploads by specifying the status. Supported values are pending,
   *     uploaded, expired, failed.
   * @param startCursor Cursor for pagination (can be null for first page)
   * @param pageSize Number of items to return (can be null for default)
   * @return FileUploadListResponse containing the paginated list of file uploads
   */
  public FileUploadList listFileUploads(String status, String startCursor, Integer pageSize) {
    ApiPath.Builder urlInfo = paginatedPath("/file_uploads", startCursor, pageSize);
    if (status != null && !status.trim().isEmpty()) {
      urlInfo.queryParam("status", status);
    }

    return getClient().call("GET", urlInfo.build(), FileUploadList.class);
  }

  /**
   * Validates the FileUploadSendParams request to ensure that exactly one of file, bytes, or
   * inputStream is provided, and that contentType is not null. Throws IllegalArgumentException if
   * validation fails.
   *
   * @param request
   */
  private void validateRequest(FileUploadSendParams request) {
    if (request.getFile() == null
        && request.getBytes() == null
        && request.getInputStream() == null) {
      throw new IllegalArgumentException("One of file, bytes, or inputStream must be provided");
    }

    if (request.getContentType() == null) {
      throw new IllegalArgumentException("Content type cannot be null");
    }
  }
}
