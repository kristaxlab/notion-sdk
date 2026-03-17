package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.endpoints.util.FileUploadUtils;
import io.kristixlab.notion.api.model.files.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.junit.jupiter.api.*;

public class FileUploadIT extends BaseIntegrationTest {

  private static final String EXTERNAL_IMG = "integration-tests.assets.external.image-url";
  private static final String IMAGE_FILE_PATH = "integration-tests.assets.files.image-file-path";
  private static final String IMAGE_FILE_PATH_4MB =
      "integration-tests.assets.files.image-file-path-4mb";
  private static final String VIDEO_FILE_PATH = "integration-tests.assets.files.video-file-path";
  private static final String SPLIT_FILE_SIZE =
      "notion.endpoints.file-uploads.multi-part.split-size-bytes";

  @BeforeEach
  protected void beforeEach(TestInfo testInfo) {
    super.beforeEach(testInfo);
    IntegrationTestAssisstant.checkThatExists(getSettings(), FileUploadIT.class, EXTERNAL_IMG);
    IntegrationTestAssisstant.checkThatExists(getSettings(), FileUploadIT.class, IMAGE_FILE_PATH);
    IntegrationTestAssisstant.checkThatExists(
        getSettings(), FileUploadIT.class, IMAGE_FILE_PATH_4MB);
    IntegrationTestAssisstant.checkThatExists(getSettings(), FileUploadIT.class, VIDEO_FILE_PATH);
    IntegrationTestAssisstant.checkThatExists(getSettings(), FileUploadIT.class, SPLIT_FILE_SIZE);
  }

  @Test
  @DisplayName("[IT-16]: File Uploads - List all file uploads")
  public void testListAllFileUploads() throws IOException {
    FileUploadList response = getNotion().fileUploads().listFileUploads();
    assertNotNull(response);
  }

  @Test
  @DisplayName("[IT-17]: File Uploads - List all expired file uploads")
  public void testListAllFileUploadsExpired() throws IOException {
    String status = "expired";
    FileUploadList pendingUploads = getNotion().fileUploads().listFileUploads(status);
    assertNotNull(pendingUploads);
    pendingUploads.getResults().forEach(f -> assertEquals(status, f.getStatus()));
  }

  @Test
  @DisplayName("[IT-14]: File Uploads - Upload a single-part file")
  public void testFileUploadSinglePartAsFile() throws IOException {
    // prerequisites
    String filePath = getSettings().getString(IMAGE_FILE_PATH);
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-14-image.jpg";
    String expectedContentType = "image/jpeg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.SINGLE_PART.getValue());
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = getNotion().fileUploads().createFileUpload(request);

    // Step 2 - Retrieve File Upload
    String id = createRs.getId();
    FileUploadResponse retrieveRS = getNotion().fileUploads().retrieveFileUpload(id);

    assertEquals("pending", retrieveRS.getStatus());
    assertEquals(expectedContentType, createRs.getContentType());
    assertEquals(request.getFilename(), createRs.getFilename());

    // Step 3 - Send File Content
    FileUploadSendParams sendRequest = new FileUploadSendParams();
    sendRequest.setFile(file);
    sendRequest.setFileName(request.getFilename());
    sendRequest.setContentType(createRs.getContentType());
    FileUploadResponse sendContentRs = getNotion().fileUploads().sendFileContent(id, sendRequest);

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
    String filePath = getSettings().getString(IMAGE_FILE_PATH);
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-15-image.jpg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.SINGLE_PART.getValue());
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = getNotion().fileUploads().createFileUpload(request);

    // Step 2 - Send File Content as byte[]
    try (InputStream is = new FileInputStream(file)) {
      FileUploadSendParams sendRequest = new FileUploadSendParams();
      sendRequest.setInputStream(is);
      sendRequest.setFileName(request.getFilename());
      sendRequest.setContentType(createRs.getContentType());
      FileUploadResponse sendContentRs =
          getNotion().fileUploads().sendFileContent(createRs.getId(), sendRequest);

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
    String filePath = getSettings().getString(IMAGE_FILE_PATH);
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-18-image.jpg";

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.SINGLE_PART.getValue());
    request.setFilename(uploadedFilename);
    FileUploadResponse createRs = getNotion().fileUploads().createFileUpload(request);

    // Step 2 - Send File Content as byte[]
    FileUploadSendParams sendRequest = new FileUploadSendParams();
    sendRequest.setBytes(FileUploadUtils.fileToBytes(file));
    sendRequest.setFileName(request.getFilename());
    sendRequest.setContentType(createRs.getContentType());
    FileUploadResponse sendContentRs =
        getNotion().fileUploads().sendFileContent(createRs.getId(), sendRequest);

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
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.EXTERNAL_URL.getValue());
    request.setExternalUrl(getSettings().getString(EXTERNAL_IMG));
    request.setContentType("image/jpeg");
    request.setFilename("int-19-image.jpg");
    FileUploadResponse createRs = getNotion().fileUploads().createFileUpload(request);

    // Step 2 - Retrieve File Upload
    String id = createRs.getId();
    FileUploadResponse retrieveRS = getNotion().fileUploads().retrieveFileUpload(id);
    assertEquals(request.getFilename(), retrieveRS.getFilename());
  }

  @Test
  @DisplayName("[IT-20]: File Uploads - Upload a multi-part file")
  @Tag("paid")
  public void testFileUploadMultiPart() throws IOException {
    // prerequisites
    String filePath = getSettings().getString(VIDEO_FILE_PATH);
    File file =
        IntegrationTestAssisstant.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-20-video_18mb.jpg";
    long partSizeInBytes = getSettings().getLong(SPLIT_FILE_SIZE);
    int numberOfParts = FileUploadUtils.calculateNumberOfParts(file.length(), partSizeInBytes);
    assertTrue(
        numberOfParts > 1, "File should be split into more than 1 part for multi-part upload test");

    // Step 1 - Create File Upload
    FileUploadCreateParams request = new FileUploadCreateParams();
    request.setMode(FileUploadMode.MULTI_PART.getValue());
    request.setFilename(uploadedFilename);
    request.setNumberOfParts(numberOfParts);
    FileUploadResponse createRs = getNotion().fileUploads().createFileUpload(request);

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
          FileUploadResponse uploadResponse =
              getNotion().fileUploads().sendFileContent(id, partRequest);

          assertNotNull(uploadResponse);
          assertNotNull(uploadResponse.getNumberOfParts());
          assertEquals("pending", uploadResponse.getStatus());
          assertEquals(numberOfParts, uploadResponse.getNumberOfParts().getTotal());
          assertEquals(partNumber, uploadResponse.getNumberOfParts().getSent());
        });

    // Step 4 - Complete File Upload
    FileUploadResponse completeRs = getNotion().fileUploads().completeFileUpload(id);
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
