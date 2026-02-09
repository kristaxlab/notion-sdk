package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.FileUploadsEndpoint;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.http.transport.rq.MultipartFormDataRequest;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.http.transport.util.URLInfoBuilder;
import io.kristixlab.notion.api.model.files.*;
import io.kristixlab.notion.api.util.Pagination;

/**
 * API for file upload operations in Notion.
 *
 * <p>Provides methods for the complete file upload lifecycle: 1. Create a file upload request to
 * get a pre-signed URL 2. Send file content to the pre-signed URL (external to this API) 3.
 * Complete the file upload process 4. Retrieve and list file uploads
 */
public class FileUploadsEndpointImpl implements FileUploadsEndpoint {

  private static final String STATUS = "status";
  private static final String FILE_UPLOAD_ID = "file_upload_id";

  private final NotionHttpTransport transport;

  public FileUploadsEndpointImpl(NotionHttpTransport transport) {
    this.transport = transport;
  }

  /**
   * Creates a new file upload and returns a pre-signed URL for uploading the file content. POST
   * https://api.notion.com/v1/file_uploads
   *
   * @param request The file upload request containing file name, content type, and parent
   * @return FileUploadResponse containing the pre-signed URL and file metadata
   * @throws IllegalArgumentException if request or required fields are null/empty
   */
  public FileUploadResponse createFileUpload(FileUploadCreateParams request) {
    validateRequest(request);
    return transport.call(
        "POST", URLInfo.build("/file_uploads"), request, FileUploadResponse.class);
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
  public FileUploadResponse sendFileContent(String fileUploadId, FileUploadSendParams request) {
    validateRequest(request);
    URLInfo urlInfo =
        URLInfo.builder("/file_uploads/{file_upload_id}/send")
            .pathParam(FILE_UPLOAD_ID, fileUploadId)
            .build();

    MultipartFormDataRequest multipartRq = new MultipartFormDataRequest();
    if (request.getFile() != null) {
      multipartRq.addFilePart(
          "file", request.getFile(), request.getFileName(), request.getContentType());
    } else if (request.getBytes() != null) {
      multipartRq.addByteArrayPart(
          "file", request.getBytes(), request.getFileName(), request.getContentType());
    } else if (request.getInputStream() != null) {
      multipartRq.addInputStreamPart(
          "file", request.getInputStream(), request.getFileName(), request.getContentType());
    }
    if (request.getPartNumber() != null) {
      multipartRq.addPart("part_number", request.getPartNumber().toString());
    }
    return transport.call("POST", urlInfo, multipartRq, FileUploadResponse.class);
  }

  /**
   * Use this API to finalize a mode=multi_part file upload after all of the parts have been sent
   * successfully.
   *
   * @param fileUploadId The ID of the file upload to complete
   * @return Updated FileUploadResponse with completion status
   */
  public FileUploadResponse completeFileUpload(String fileUploadId) {
    validateFileUploadId(fileUploadId);

    URLInfo urlInfo =
        URLInfo.builder("/file_uploads/{file_upload_id}/complete")
            .pathParam(FILE_UPLOAD_ID, fileUploadId)
            .build();
    return transport.call("POST", urlInfo, new Object(), FileUploadResponse.class);
  }

  /**
   * Retrieves a specific file upload by its ID. GET
   * https://api.notion.com/v1/file_uploads/{file_upload_id}
   *
   * @param fileUploadId The ID of the file upload to retrieve
   * @return FileUploadResponse containing the file upload details
   */
  public FileUploadResponse retrieveFileUpload(String fileUploadId) {
    validateFileUploadId(fileUploadId);
    URLInfo urlInfo =
        URLInfo.builder("/file_uploads/{file_upload_id}")
            .pathParam(FILE_UPLOAD_ID, fileUploadId)
            .build();
    return transport.call("GET", urlInfo, FileUploadResponse.class);
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
    URLInfoBuilder urlInfo = URLInfo.builder("/file_uploads");
    if (status != null && !status.trim().isEmpty()) {
      urlInfo.queryParam(STATUS, status);
    }

    if (startCursor != null) {
      urlInfo.queryParam(Pagination.START_CURSOR, startCursor);
    }

    if (pageSize != null) {
      urlInfo.queryParam(Pagination.PAGE_SIZE, pageSize);
    }

    return transport.call("GET", urlInfo.build(), FileUploadList.class);
  }

  /**
   * Validates the file upload request.
   *
   * @param request The request to validate
   * @throws IllegalArgumentException if request or required fields are invalid
   */
  private void validateRequest(FileUploadCreateParams request) {
    if (request == null) {
      throw new IllegalArgumentException("FileUploadRequest cannot be null");
    }

    // Validate mode
    String mode = request.getMode();
    if (mode == null || mode.trim().isEmpty()) {
      throw new IllegalArgumentException("Mode cannot be null or empty");
    }

    if (!("multi_part".equals(mode) || "external_url".equals(mode) || "single_part".equals(mode))) {
      throw new IllegalArgumentException(
          "Mode must be one of: multi_part, external_url, single_part");
    }

    // Validate filename for multi_part and external_url modes
    if (("multi_part".equals(mode) || "external_url".equals(mode))) {
      if (request.getFilename() == null || request.getFilename().trim().isEmpty()) {
        throw new IllegalArgumentException("Filename is required when mode is " + mode);
      }
    }

    // Validate content type
    //    if (request.getContentType() == null || request.getContentType().trim().isEmpty()) {
    //      throw new IllegalArgumentException("Content type cannot be null or empty");
    //    }

    // Validate number of parts for multi_part mode
    if ("multi_part".equals(mode)) {
      Integer numberOfParts = request.getNumberOfParts();
      if (numberOfParts != null && (numberOfParts < 1 || numberOfParts > 1000)) {
        throw new IllegalArgumentException(
            "Number of parts must be between 1 and 1,000 for multi_part mode");
      }
    }

    // Validate external URL for external_url mode
    if ("external_url".equals(mode)) {
      String externalUrl = request.getExternalUrl();
      if (externalUrl == null || externalUrl.trim().isEmpty()) {
        throw new IllegalArgumentException("External URL is required when mode is external_url");
      }

      if (!externalUrl.toLowerCase().startsWith("https://")) {
        throw new IllegalArgumentException("External URL must be an HTTPS URL");
      }
    }
  }

  private void validateFileUploadId(String fileUploadId) {
    if (fileUploadId == null || fileUploadId.trim().isEmpty()) {
      throw new IllegalArgumentException("File upload ID cannot be null or empty");
    }
  }

  private void validateRequest(FileUploadSendParams request) {
    if (request.getFile() == null
        && request.getBytes() == null
        && request.getInputStream() == null) {
      throw new IllegalArgumentException("One of file, bytes, or inputStream must be provided");
    }

    if (request.getFileName() == null || request.getFileName().trim().isEmpty()) {
      throw new IllegalArgumentException("Filename cannot be null or empty");
    }

    if (request.getContentType() == null) {
      throw new IllegalArgumentException("Content type cannot be null");
    }
  }

  private void validateFileContentType(String contentType) {
    if (contentType == null || contentType.trim().isEmpty()) {
      throw new IllegalArgumentException("File content type cannot be null or empty");
    }
  }
}
