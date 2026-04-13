package integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.endpoints.util.FileUploadUtils;
import io.kristaxlab.notion.model.file.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.junit.jupiter.api.*;

public class FileUploadIT extends BaseIntegrationTest {

  private static final String IMAGE_FILE_PATH = "files/image_357kb.jpg";
  private static final String VIDEO_FILE_PATH = "files/video_sakura_18mb.MOV";
  private static final Long SPLIT_FILE_SIZE = 5242880L; // 5 MB

  @Test
  @DisplayName("[IT-16]: File Uploads - List all file uploads")
  public void testListAllFileUploads() throws IOException {
    FileUploadList response = getNotion().fileUploads().listFileUploads();
    assertNotNull(response);
  }

  @Test
  @DisplayName("[IT-17]: File Uploads - List all expired file uploads")
  public void testListAllFileUploadsExpired() throws IOException {
    String status = "uploaded";
    FileUploadList uploads = getNotion().fileUploads().listFileUploads(status);
    assertNotNull(uploads);
    if (uploads.getResults() != null) {
      uploads.getResults().forEach(f -> assertEquals(status, f.getStatus()));
    }
  }

  @Test
  @DisplayName("[IT-14]: File Uploads - Upload a single-part file")
  public void testFileUploadSinglePartAsFile() throws IOException {
    // prerequisites
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(
            IMAGE_FILE_PATH, getClass().getClassLoader());
    String uploadedFilename = "int-14-image.jpg";
    String expectedContentType = "image/jpeg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = FileUploadCreateParams.singlePart(uploadedFilename);
    FileUpload createRs = getNotion().fileUploads().create(request);

    // Step 2 - Retrieve File Upload
    String id = createRs.getId();
    FileUpload retrieveRS = getNotion().fileUploads().retrieve(id);

    assertEquals(expectedContentType, createRs.getContentType());
    assertEquals(request.getFilename(), createRs.getFilename());

    // Step 3 - Send File Content
    FileUploadSendParams sendRequest = FileUploadSendParams.of(file, createRs.getContentType());
    FileUpload sendContentRs = getNotion().fileUploads().upload(id, sendRequest);

    assertNotNull(sendContentRs);
    assertEquals(id, sendContentRs.getId());
    assertEquals("uploaded", sendContentRs.getStatus());
    assertEquals(request.getFilename(), sendContentRs.getFilename());
    assertEquals(expectedContentType, sendContentRs.getContentType());
    assertNotNull(sendContentRs.getContentLength());
  }

  @Test
  @DisplayName("[IT-15]: File Uploads - Upload a single-part file as input stream")
  public void testFileUploadSinglePartAsStream() throws IOException {
    // prerequisites
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(
            IMAGE_FILE_PATH, getClass().getClassLoader());
    String uploadedFilename = "int-15-image.jpg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = FileUploadCreateParams.singlePart(uploadedFilename);
    FileUpload createRs = getNotion().fileUploads().create(request);

    // Step 2 - Send File Content as byte[]
    try (InputStream is = new FileInputStream(file)) {
      FileUploadSendParams sendRequest = FileUploadSendParams.of(is, createRs.getContentType());
      FileUpload sendContentRs = getNotion().fileUploads().upload(createRs.getId(), sendRequest);

      assertNotNull(sendContentRs);
      assertEquals(createRs.getId(), sendContentRs.getId());
      assertEquals("uploaded", sendContentRs.getStatus());
      assertEquals(request.getFilename(), sendContentRs.getFilename());
      assertEquals(createRs.getContentType(), sendContentRs.getContentType());
      assertNotNull(sendContentRs.getContentLength());
    }
  }

  @Test
  @DisplayName("[IT-18]: File Uploads - Upload a single-part file as byte array")
  public void testFileUploadSinglePartAsBytes() throws IOException {
    // prerequisites
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(
            IMAGE_FILE_PATH, getClass().getClassLoader());
    String uploadedFilename = "int-18-image.jpg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = FileUploadCreateParams.singlePart(uploadedFilename);
    FileUpload createRs = getNotion().fileUploads().create(request);

    // Step 2 - Send File Content as byte[]
    FileUploadSendParams sendRequest =
        FileUploadSendParams.of(FileUploadUtils.fileToBytes(file), createRs.getContentType());
    FileUpload sendContentRs = getNotion().fileUploads().upload(createRs.getId(), sendRequest);

    assertNotNull(sendContentRs);
    assertEquals(createRs.getId(), sendContentRs.getId());
    assertEquals("uploaded", sendContentRs.getStatus());
    assertEquals(request.getFilename(), sendContentRs.getFilename());
    assertEquals(createRs.getContentType(), sendContentRs.getContentType());
    assertNotNull(sendContentRs.getContentLength());
  }

  @Test
  @DisplayName("[IT-19]: File Uploads - Import external file")
  public void testFileUploadExternal() throws IOException {
    // Step 1 - Create File Upload
    FileUploadCreateParams request =
        FileUploadCreateParams.external(
            "int-19-image.jpg", IntegrationTestAssisstant.getPrerequisites().getExternalImageUrl());
    FileUpload createRs = getNotion().fileUploads().create(request);

    // Step 2 - Retrieve File Upload
    String id = createRs.getId();
    FileUpload retrieveRS = getNotion().fileUploads().retrieve(id);
    assertEquals(request.getFilename(), retrieveRS.getFilename());
  }

  @Test
  @DisplayName("[IT-20]: File Uploads - Upload a multi-part file")
  @Tag("paid")
  public void testFileUploadMultiPart() throws IOException {
    // prerequisites
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(
            VIDEO_FILE_PATH, getClass().getClassLoader());
    String uploadedFilename = "int-20-video_18mb.jpg";
    long partSizeInBytes = SPLIT_FILE_SIZE;
    int numberOfParts = FileUploadUtils.calculateNumberOfParts(file.length(), partSizeInBytes);
    assertTrue(
        numberOfParts > 1, "File should be split into more than 1 part for multi-part upload test");

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.MULTI_PART.getValue());
    request.setFilename(uploadedFilename);
    request.setNumberOfParts(numberOfParts);
    FileUpload createRs = getNotion().fileUploads().create(request);

    assertEquals("pending", createRs.getStatus());
    assertEquals(request.getFilename(), createRs.getFilename());
    assertNotNull(createRs.getNumberOfParts());
    assertEquals(request.getNumberOfParts(), createRs.getNumberOfParts().getTotal());

    // Step 3 - Send File Content
    final String id = createRs.getId();
    Map<Integer, File> parts = FileUploadUtils.splitFileIntoParts(file, partSizeInBytes, "./temp/");
    parts.forEach(
        (partNumber, filePart) -> {
          FileUploadSendParams partRequest = new FileUploadSendParams();
          partRequest.setPartNumber(partNumber);
          partRequest.setFile(filePart);
          partRequest.setFilename(uploadedFilename);
          partRequest.setContentType(createRs.getContentType());
          FileUpload uploadResponse = getNotion().fileUploads().upload(id, partRequest);

          assertNotNull(uploadResponse);
          assertNotNull(uploadResponse.getNumberOfParts());
          assertEquals("pending", uploadResponse.getStatus());
          assertEquals(numberOfParts, uploadResponse.getNumberOfParts().getTotal());
          assertEquals(partNumber, uploadResponse.getNumberOfParts().getSent());
        });

    // Step 4 - Complete File Upload
    FileUpload completeRs = getNotion().fileUploads().complete(id);
    assertNotNull(completeRs);
    assertEquals(id, completeRs.getId());
    assertEquals("uploaded", completeRs.getStatus());
    assertEquals(uploadedFilename, completeRs.getFilename());
    assertNotNull(completeRs.getContentType());
    assertNotNull(completeRs.getContentLength());
    assertNotNull(completeRs.getNumberOfParts());
    assertEquals(numberOfParts, completeRs.getNumberOfParts().getTotal());
    assertEquals(numberOfParts, completeRs.getNumberOfParts().getSent());

    FileUploadUtils.cleanupPartsFiles(parts.values());
  }
}
