package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.ApiClientStub;
import io.kristixlab.notion.api.http.base.client.HttpClient.MultipartBody;
import io.kristixlab.notion.api.http.base.client.HttpClient.TextPart;
import io.kristixlab.notion.api.model.file.FileUploadCreateParams;
import io.kristixlab.notion.api.model.file.FileUploadSendParams;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("File uploads endpoint behaviors")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileUploadsEndpointImplTest {

  private ApiClientStub client;
  private FileUploadsEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new FileUploadsEndpointImpl(client);
  }

  @Test
  @DisplayName("works for valid create file upload request")
  void create() {
    FileUploadCreateParams request = singlePartRequest();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  @DisplayName("rejects null create file upload request")
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  @DisplayName("works for valid upload request with bytes content")
  void upload() {
    FileUploadSendParams request = bytesRequest();

    endpoint.upload("upload-id-1", request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/file_uploads/{file_upload_id}/send", client.getLastUrlInfo().getUrl());
    assertEquals("upload-id-1", client.getLastUrlInfo().getPathParams().get("file_upload_id"));
    assertInstanceOf(MultipartBody.class, client.getLastBody());
  }

  @Test
  @DisplayName("works for valid upload request with file content")
  void sendFileContent_withFile() {
    FileUploadSendParams request = new FileUploadSendParams();
    request.setFile(new File("file.pdf"));
    request.setFilename("file.pdf");
    request.setContentType("application/pdf");

    endpoint.upload("upload-id-1", request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/file_uploads/{file_upload_id}/send", client.getLastUrlInfo().getUrl());
    assertEquals("upload-id-1", client.getLastUrlInfo().getPathParams().get("file_upload_id"));
    assertInstanceOf(MultipartBody.class, client.getLastBody());
  }

  @Test
  @DisplayName("works for valid upload request with input stream content")
  void upload_withInputStream() {
    FileUploadSendParams request = new FileUploadSendParams();
    request.setInputStream(new ByteArrayInputStream(new byte[] {1, 2, 3}));
    request.setFilename("file.pdf");
    request.setContentType("application/pdf");

    endpoint.upload("upload-id-1", request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/file_uploads/{file_upload_id}/send", client.getLastUrlInfo().getUrl());
    assertEquals("upload-id-1", client.getLastUrlInfo().getPathParams().get("file_upload_id"));
    assertInstanceOf(MultipartBody.class, client.getLastBody());
  }

  @Test
  @DisplayName("works for valid upload request with part number")
  void upload_withPartNumber() {
    FileUploadSendParams request = bytesRequest();
    request.setPartNumber(3);

    endpoint.upload("upload-id-1", request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/file_uploads/{file_upload_id}/send", client.getLastUrlInfo().getUrl());
    assertEquals("upload-id-1", client.getLastUrlInfo().getPathParams().get("file_upload_id"));

    MultipartBody body = assertInstanceOf(MultipartBody.class, client.getLastBody());
    assertEquals(2, body.parts().size());
    TextPart partNumber =
        body.parts().stream()
            .filter(TextPart.class::isInstance)
            .map(TextPart.class::cast)
            .filter(p -> "part_number".equals(p.name()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("part_number part not found"));
    assertEquals("3", partNumber.value());
  }

  @Test
  @DisplayName("works for valid upload request without part number")
  void upload_withoutPartNumber_omitsPartNumberPart() {
    FileUploadSendParams request = bytesRequest();

    endpoint.upload("upload-id-1", request);

    MultipartBody body = assertInstanceOf(MultipartBody.class, client.getLastBody());
    boolean hasPartNumber =
        body.parts().stream()
            .filter(TextPart.class::isInstance)
            .map(TextPart.class::cast)
            .anyMatch(p -> "part_number".equals(p.name()));
    assertFalse(hasPartNumber, "part_number part should not be present when not set");
  }

  @Test
  @DisplayName("rejects upload request without file content")
  void upload_rejectsRequestWithNoFileContent() {
    FileUploadSendParams request = new FileUploadSendParams();
    request.setFilename("file.pdf");
    request.setContentType("application/pdf");

    assertThrows(IllegalArgumentException.class, () -> endpoint.upload("upload-id-1", request));
  }

  @Test
  @DisplayName("rejects upload request with missing content type")
  void uploadType() {
    FileUploadSendParams request = new FileUploadSendParams();
    request.setBytes(new byte[] {1});
    request.setFilename("file.pdf");

    assertThrows(IllegalArgumentException.class, () -> endpoint.upload("upload-id-1", request));
  }

  @Test
  @DisplayName("works for valid complete file upload id")
  void complete() {
    endpoint.complete("upload-id-1");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/file_uploads/{file_upload_id}/complete", client.getLastUrlInfo().getUrl());
    assertEquals("upload-id-1", client.getLastUrlInfo().getPathParams().get("file_upload_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null complete file upload id")
  void completeFileUpload_rejectsBlankOrNullId(String fileUploadId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.complete(fileUploadId));
  }

  @Test
  @DisplayName("works for valid retrieve file upload id")
  void retrieve() {
    endpoint.retrieve("upload-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads/{file_upload_id}", client.getLastUrlInfo().getUrl());
    assertEquals("upload-id-1", client.getLastUrlInfo().getPathParams().get("file_upload_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null retrieve file upload id")
  void retrieveFileUpload_rejectsBlankOrNullId(String fileUploadId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(fileUploadId));
  }

  // ── listFileUploads ───────────────────────────────────────────────────────

  @Test
  @DisplayName("works for default list file uploads request")
  void listFileUploads() {
    endpoint.listFileUploads();

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
  }

  @Test
  @DisplayName("works with status filter")
  void listFileUploads_withStatus() {
    endpoint.listFileUploads("uploaded");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("uploaded"), client.getLastUrlInfo().getQueryParams().get("status"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  @DisplayName("works with start cursor and no status")
  void listFileUploads_withStartCursor() {
    endpoint.listFileUploads("cursor-abc", 0);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("status"));
  }

  @Test
  @DisplayName("works with page size and no status")
  void listFileUploads_withPageSize() {
    endpoint.listFileUploads(null, 50);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("50"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("status"));
  }

  @Test
  @DisplayName("works with status and pagination params")
  void listFileUploads_withAllParams() {
    endpoint.listFileUploads("pending", "cursor-abc", 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("pending"), client.getLastUrlInfo().getQueryParams().get("status"));
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("works and omits status for blank or null status value")
  void listFileUploads_omitsBlankOrNullStatus(String status) {
    endpoint.listFileUploads(status);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/file_uploads", client.getLastUrlInfo().getUrl());
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("status"));
  }

  private static FileUploadCreateParams singlePartRequest() {
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode("single_part");
    return request;
  }

  private static FileUploadSendParams bytesRequest() {
    FileUploadSendParams request = new FileUploadSendParams();
    request.setBytes(new byte[] {1, 2, 3});
    request.setFilename("file.pdf");
    request.setContentType("application/pdf");
    return request;
  }
}
