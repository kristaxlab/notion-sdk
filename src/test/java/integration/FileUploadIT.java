package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestUtil;
import io.kristixlab.notion.NotionSdkSettings;
import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.endpoints.util.FileUploadUtils;
import io.kristixlab.notion.api.model.files.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class FileUploadIT {

  private static NotionApiClient NOTION;
  private static NotionSdkSettings SETTINGS;
  private static final String EXTERNAL_IMG = "integration-tests.assets.external.image-url";
  private static final String IMAGE_FILE_PATH = "integration-tests.assets.files.image-file-path";
  private static final String IMAGE_FILE_PATH_4MB =
      "integration-tests.assets.files.image-file-path-4mb";
  private static final String VIDEO_FILE_PATH = "integration-tests.assets.files.video-file-path";
  private static final String SPLIT_FILE_SIZE =
      "notion.endpoints.file-uploads.multi-part.split-size-bytes";

  @BeforeAll
  public static void initClient() {
    NOTION = NotionClientProvider.internalTestingClient();
    SETTINGS = NotionSdkSettings.getInstance();

    IntegrationTestUtil.checkThatExists(SETTINGS, FileUploadIT.class, EXTERNAL_IMG);
    IntegrationTestUtil.checkThatExists(SETTINGS, FileUploadIT.class, IMAGE_FILE_PATH);
    IntegrationTestUtil.checkThatExists(SETTINGS, FileUploadIT.class, IMAGE_FILE_PATH_4MB);
    IntegrationTestUtil.checkThatExists(SETTINGS, FileUploadIT.class, VIDEO_FILE_PATH);
    IntegrationTestUtil.checkThatExists(SETTINGS, FileUploadIT.class, SPLIT_FILE_SIZE);
  }

  @Test
  @DisplayName("[IT-16]: File Uploads - List all file uploads")
  public void testListAllFileUploads() throws IOException {
    FileUploadList response = NOTION.fileUploads().listFileUploads();
    assertNotNull(response);
    assertNotNull(response.getRequestId());
  }

  @Test
  @DisplayName("[IT-17]: File Uploads - List all expired file uploads")
  public void testListAllFileUploadsExpired() throws IOException {
    String status = "expired";
    FileUploadList pendingUploads = NOTION.fileUploads().listFileUploads(status);
    assertNotNull(pendingUploads);
    assertNotNull(pendingUploads.getRequestId());
    pendingUploads.getResults().forEach(f -> assertEquals(status, f.getStatus()));
  }

  @Test
  @DisplayName("[IT-14]: File Uploads - Upload a single-part file")
  public void testFileUploadSinglePartAsFile() throws IOException {
    // prerequisites
    String filePath = SETTINGS.getString(IMAGE_FILE_PATH);
    File file = IntegrationTestUtil.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-14-image.jpg";
    String expectedContentType = "image/jpeg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.SINGLE_PART.getValue());
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = NOTION.fileUploads().createFileUpload(request);

    // Step 2 - Retrieve File Upload
    String id = createRs.getId();
    FileUploadResponse retrieveRS = NOTION.fileUploads().retrieveFileUpload(id);

    assertEquals("pending", retrieveRS.getStatus());
    assertEquals(expectedContentType, createRs.getContentType());
    assertEquals(request.getFilename(), createRs.getFilename());

    // Step 3 - Send File Content
    FileUploadSendParams sendRequest = new FileUploadSendParams();
    sendRequest.setFile(file);
    sendRequest.setFileName(request.getFilename());
    sendRequest.setContentType(createRs.getContentType());
    FileUploadResponse sendContentRs = NOTION.fileUploads().sendFileContent(id, sendRequest);

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
    String filePath = SETTINGS.getString(IMAGE_FILE_PATH);
    File file = IntegrationTestUtil.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-15-image.jpg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.SINGLE_PART.getValue());
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = NOTION.fileUploads().createFileUpload(request);

    // Step 2 - Send File Content as byte[]
    try (InputStream is = new FileInputStream(file)) {
      FileUploadSendParams sendRequest = new FileUploadSendParams();
      sendRequest.setInputStream(is);
      sendRequest.setFileName(request.getFilename());
      sendRequest.setContentType(createRs.getContentType());
      FileUploadResponse sendContentRs =
          NOTION.fileUploads().sendFileContent(createRs.getId(), sendRequest);

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
    String filePath = SETTINGS.getString(IMAGE_FILE_PATH);
    File file = IntegrationTestUtil.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-18-image.jpg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.SINGLE_PART.getValue());
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = NOTION.fileUploads().createFileUpload(request);

    // Step 2 - Send File Content as byte[]
    FileUploadSendParams sendRequest = new FileUploadSendParams();
    sendRequest.setBytes(FileUploadUtils.fileToBytes(file));
    sendRequest.setFileName(request.getFilename());
    sendRequest.setContentType(createRs.getContentType());
    FileUploadResponse sendContentRs =
        NOTION.fileUploads().sendFileContent(createRs.getId(), sendRequest);

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
    // prerequisites
    String uploadedFilename = "int-19-image.jpg";
    String expectedContentType = "image/jpeg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.EXTERNAL_URL.getValue());
    request.setExternalUrl(SETTINGS.getString(EXTERNAL_IMG));
    request.setContentType(expectedContentType);
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = NOTION.fileUploads().createFileUpload(request);

    // Step 2 - Retrieve File Upload
    String id = createRs.getId();
    FileUploadResponse retrieveRS = NOTION.fileUploads().retrieveFileUpload(id);

    assertEquals("pending", retrieveRS.getStatus());
    assertEquals(expectedContentType, createRs.getContentType());
    assertEquals(request.getFilename(), createRs.getFilename());
  }

  @Test
  @DisplayName("[IT-20]: File Uploads - Upload a multi-part file")
  @Tag("paid")
  public void testFileUploadMultiPart() throws IOException {
    // prerequisites
    String filePath = SETTINGS.getString(VIDEO_FILE_PATH);
    File file = IntegrationTestUtil.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-20-video_18mb.jpg";
    long partSizeInBytes = SETTINGS.getLong(SPLIT_FILE_SIZE);
    int numberOfParts = FileUploadUtils.calculateNumberOfParts(file.length(), partSizeInBytes);
    assertTrue(
        numberOfParts > 1, "File should be split into more than 1 part for multi-part upload test");

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.MULTI_PART.getValue());
    request.setFilename(uploadedFilename);
    request.setNumberOfParts(numberOfParts);
    FileUploadResponse createRs = NOTION.fileUploads().createFileUpload(request);

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
          partRequest.setFileName(uploadedFilename);
          partRequest.setContentType(createRs.getContentType());
          FileUploadResponse uploadResponse = NOTION.fileUploads().sendFileContent(id, partRequest);

          assertNotNull(uploadResponse);
          assertNotNull(uploadResponse.getNumberOfParts());
          assertEquals("pending", uploadResponse.getStatus());
          assertEquals(numberOfParts, uploadResponse.getNumberOfParts().getTotal());
          assertEquals(partNumber, uploadResponse.getNumberOfParts().getSent());
        });

    // Step 4 - Complete File Upload
    FileUploadResponse completeRs = NOTION.fileUploads().completeFileUpload(id);
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
