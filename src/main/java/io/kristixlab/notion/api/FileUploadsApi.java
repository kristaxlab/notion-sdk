package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.ApiRequestUtil;
import io.kristixlab.notion.api.exchange.transport.ApiTransport;
import io.kristixlab.notion.api.model.files.*;
import java.util.HashMap;
import java.util.Map;

/**
 * API for file upload operations in Notion.
 *
 * <p>Provides methods for the complete file upload lifecycle: 1. Create a file upload request to
 * get a pre-signed URL 2. Send file content to the pre-signed URL (external to this API) 3.
 * Complete the file upload process 4. Retrieve and list file uploads
 */
public class FileUploadsApi {

  private static final String STATUS = "status";
  private static final String FILE_UPLOAD_ID = "file_upload_id";
  private final ApiTransport transport;

  public FileUploadsApi(NotionApiTransport transport) {
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
  public FileUploadResponse createFileUpload(FileUploadCreateRequest request) {
    validateRequest(request);
    return transport.call("POST", "/file_uploads", null, null, request, FileUploadResponse.class);
  }

  /**
   * Sends file content to the pre-signed URL. This is typically done externally using the
   * upload_url from createFileUpload response. POST
   * https://api.notion.com/v1/file_uploads/{file_upload_id}/send
   *
   * @param fileUploadId The ID of the file upload
   * @param fileContent The binary content of the file
   * @param partNumber The part number for multipart uploads
   * @return Response from the file upload service
   */
  public FileUploadResponse sendFileContent(
      String fileUploadId, byte[] fileContent, int partNumber) {
    if (fileUploadId == null || fileUploadId.trim().isEmpty()) {
      throw new IllegalArgumentException("File upload ID cannot be null or empty");
    }
    if (fileContent == null || fileContent.length == 0) {
      throw new IllegalArgumentException("File content cannot be null or empty");
    }

    // Use the multipart transport method for file uploads
    return transport.callMultipart(
        "POST",
        "/file_uploads/" + fileUploadId + "/send",
        null,
        null,
        fileContent,
        String.valueOf(partNumber),
        FileUploadResponse.class);
  }

  /**
   * Sends file content to the pre-signed URL using a FileUploadSendRequest object. POST
   * https://api.notion.com/v1/file_uploads/{file_upload_id}/send
   *
   * @param fileUploadId The ID of the file upload
   * @param request request containing part number and file byte array
   * @return Response from the file upload service
   */
  public FileUploadResponse sendFileContent(String fileUploadId, FileUploadSendRequest request) {
    if (fileUploadId == null || fileUploadId.trim().isEmpty()) {
      throw new IllegalArgumentException("File upload ID cannot be null or empty");
    }
    if (request == null) {
      throw new IllegalArgumentException("FileUploadSendRequest cannot be null");
    }
    if (request.getFile() == null) {
      throw new IllegalArgumentException("File byte array cannot be null");
    }

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(FILE_UPLOAD_ID, fileUploadId);
    return transport.callMultipart(
        "POST",
        "/file_uploads/" + fileUploadId + "/send",
        request.getContentType(),
        pathParams,
        request.getFile(),
        request.getPartNumber(),
        FileUploadResponse.class);
  }

  /**
   * Use this API to finalize a mode=multi_part file upload after all of the parts have been sent
   * successfully.
   *
   * @param fileUploadId The ID of the file upload to complete
   * @return Updated FileUploadResponse with completion status
   */
  public FileUploadResponse completeFileUpload(String fileUploadId) {
    if (fileUploadId == null || fileUploadId.trim().isEmpty()) {
      throw new IllegalArgumentException("File upload ID cannot be null or empty");
    }

    return transport.call(
        "POST",
        "/file_uploads/" + fileUploadId + "/complete",
        null,
        null,
        null,
        FileUploadResponse.class);
  }

  /**
   * Retrieves a specific file upload by its ID. GET
   * https://api.notion.com/v1/file_uploads/{file_upload_id}
   *
   * @param fileUploadId The ID of the file upload to retrieve
   * @return FileUploadResponse containing the file upload details
   */
  public FileUploadResponse retrieveFileUpload(String fileUploadId) {
    if (fileUploadId == null || fileUploadId.trim().isEmpty()) {
      throw new IllegalArgumentException("File upload ID cannot be null or empty");
    }

    return transport.call(
        "GET", "/file_uploads/" + fileUploadId, null, null, null, FileUploadResponse.class);
  }

  /**
   * Lists all file uploads in the workspace. GET https://api.notion.com/v1/file_uploads
   *
   * @return FileUploadListResponse containing the list of file uploads
   */
  public FileUploadList listFileUploads() {
    return transport.call("GET", "/file_uploads", null, null, null, FileUploadList.class);
  }

  /**
   * Lists all file uploads in the workspace. GET https://api.notion.com/v1/file_uploads
   *
   * @return FileUploadListResponse containing the list of file uploads
   */
  public FileUploadList listFileUploads(String status) {
    Map<String, String[]> queryParams = new HashMap<String, String[]>();
    if (status != null && !status.trim().isEmpty()) {
      queryParams.put(STATUS, new String[] {status});
    }
    return transport.call("GET", "/file_uploads", queryParams, null, null, FileUploadList.class);
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
    Map<String, String[]> queryParams = ApiRequestUtil.createQueryParams(startCursor, pageSize);
    return transport.call("GET", "/file_uploads", queryParams, null, null, FileUploadList.class);
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
    Map<String, String[]> queryParams = ApiRequestUtil.createQueryParams(startCursor, pageSize);
    if (status != null && !status.trim().isEmpty()) {
      queryParams.put(STATUS, new String[] {status});
    }

    return transport.call("GET", "/file_uploads", queryParams, null, null, FileUploadList.class);
  }

  /**
   * Validates the file upload request.
   *
   * @param request The request to validate
   * @throws IllegalArgumentException if request or required fields are invalid
   */
  private void validateRequest(FileUploadCreateRequest request) {
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
    if (request.getContentType() == null || request.getContentType().trim().isEmpty()) {
      throw new IllegalArgumentException("Content type cannot be null or empty");
    }

    // Validate number of parts for multi_part mode
    if ("multi_part".equals(mode)) {
      int numberOfParts = request.getNumberOfParts();
      if (numberOfParts < 1 || numberOfParts > 1000) {
        throw new IllegalArgumentException(
            "Number of parts must be between 1 and 1,000 for multi_part mode");
      }
    }

    // Validate external URL for external_url mode
    if ("external_url".equals(mode)) {
      String externalUrl = request.getExternal_url();
      if (externalUrl == null || externalUrl.trim().isEmpty()) {
        throw new IllegalArgumentException("External URL is required when mode is external_url");
      }

      if (!externalUrl.toLowerCase().startsWith("https://")) {
        throw new IllegalArgumentException("External URL must be an HTTPS URL");
      }
    }
  }
}
