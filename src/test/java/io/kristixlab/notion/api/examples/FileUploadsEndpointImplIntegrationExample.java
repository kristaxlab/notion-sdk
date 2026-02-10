package io.kristixlab.notion.api.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristixlab.notion.api.endpoints.impl.FileUploadsEndpointImpl;
import io.kristixlab.notion.api.model.files.FileUploadCreateParams;
import io.kristixlab.notion.api.model.files.FileUploadList;
import io.kristixlab.notion.api.model.files.FileUploadResponse;
import io.kristixlab.notion.api.model.files.FileUploadSendParams;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileUploadsEndpointImplIntegrationExample extends IntegrationTest {

  private static FileUploadsEndpointImpl fileUploadsApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    fileUploadsApi = new FileUploadsEndpointImpl(getTransport());
  }

  @Test
  void testCreateAndSendFileUploads() throws Exception {
    // Create a file upload request
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode("single_part");
    request.setFilename("snowy_day.jpg");
    request.setContentType("image/jpeg");

    // Create file upload
    FileUploadResponse createResponse = fileUploadsApi.createFileUpload(request);
    saveToFile(createResponse, "file-upload-create-snowy-day-response.json");

    // Validate the create response
    assertNotNull(createResponse);
    assertNotNull(createResponse.getId());
    assertEquals("file_upload", createResponse.getObject());
    assertEquals("snowy_day.jpg", createResponse.getFilename());
    assertEquals("image/jpeg", createResponse.getContentType());

    // Read the image file from resources
    try (InputStream inputStream = getClass().getResourceAsStream("/files/image.jpg")) {
      assertNotNull(inputStream, "Image file should exist in resources/files directory");
      byte[] fileContent = inputStream.readAllBytes();

      // Send file content
      FileUploadSendParams sendRequest = new FileUploadSendParams();
      // TODO
      //      sendRequest.setFile(fileContent);
      sendRequest.setContentType(createResponse.getContentType());
      FileUploadResponse sendResponse =
          fileUploadsApi.sendFileContent(createResponse.getId(), sendRequest);
      saveToFile(sendResponse, "file-upload-send-snowy-day-response.json");

      // Validate the send response
      assertNotNull(sendResponse);
      assertEquals(createResponse.getId(), sendResponse.getId());
    }
  }

  @Test
  void testListAllFileUploads() throws Exception {
    // Retrieve a user by ID
    FileUploadList filUploads = fileUploadsApi.listFileUploads();
    saveToFile(filUploads, "file-uploads-list-rs.json");

    // Validate the retrieved user
    assertNotNull(filUploads);
  }

  @Test
  void testListFileUploadsByStatus() throws Exception {
    // Retrieve a user by ID
    FileUploadList filUploads = fileUploadsApi.listFileUploads("uploaded");
    saveToFile(filUploads, "file-uploads-list-filtered-rs.json");

    // Validate the retrieved user
    assertNotNull(filUploads);
  }

  @Test
  void testRetrieveFileUploads() throws Exception {
    // Retrieve a user by ID
    FileUploadResponse filUpload =
        fileUploadsApi.retrieveFileUpload("25eb90f8-6d76-8143-8ff7-00b2486a195a");
    for (int i = 0; i < 1000; i++) {
      fileUploadsApi.retrieveFileUpload("25eb90f8-6d76-8143-8ff7-00b2486a195a");
    }
    saveToFile(filUpload, "file-uploads-retrieve-by-id-rs.json");
  }

  @Test
  void testCompleteFileUploads() throws Exception {
    //  File uploads must be in a `pending` status to use the complete API
    FileUploadResponse filUpload =
        fileUploadsApi.completeFileUpload("25eb90f8-6d76-81db-b720-00b2848e40bd");
    saveToFile(filUpload, "file-uploads-complete-by-id-rs.json");
  }
}
