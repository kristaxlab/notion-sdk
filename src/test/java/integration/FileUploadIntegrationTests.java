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
import org.junit.jupiter.api.Test;

public class FileUploadIntegrationTests {

  private static NotionApiClient NOTION;
  private static NotionSdkSettings SETTINGS;
  private static final String EXTERNAL_IMG = "integration-tests.assets.external.image-url";
  private static final String IMAGE_FILE_PATH = "integration-tests.assets.files.image-file-path";
  private static final String VIDEO_FILE_PATH = "integration-tests.assets.files.video-file-path";

  @BeforeAll
  public static void initClient() {
    NOTION = NotionClientProvider.internalTestingClient();
    SETTINGS = NotionSdkSettings.getInstance();

    IntegrationTestUtil.checkThatExists(SETTINGS, FileUploadIntegrationTests.class, EXTERNAL_IMG);
    IntegrationTestUtil.checkThatExists(
        SETTINGS, FileUploadIntegrationTests.class, IMAGE_FILE_PATH);
    IntegrationTestUtil.checkThatExists(
        SETTINGS, FileUploadIntegrationTests.class, VIDEO_FILE_PATH);
  }

  /**
   * INT-16: File Uploads: list all the file uploads. Checks that endpoint returns successful
   * response
   */
  @Test
  public void listAllFileUploadsInt16() throws IOException {
    FileUploadList response = NOTION.fileUploads().listFileUploads();
    assertNotNull(response);
    assertNotNull(response.getRequestId());
  }

  /**
   * INT-17: File Uploads: list all the expired file uploads. Checks that if any result presents, it
   * has status "expired"
   */
  @Test
  public void testListAllFileUploadsExpiredInt17() throws IOException {
    String status = "expired";
    FileUploadList pendingUploads = NOTION.fileUploads().listFileUploads(status);
    assertNotNull(pendingUploads);
    assertNotNull(pendingUploads.getRequestId());
    pendingUploads.getResults().forEach(f -> assertEquals(status, f.getStatus()));
  }

  /** INT-14: File Uploads: upload a single-part file */
  @Test
  public void testFileUploadSinglePartInt14() throws IOException {
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

  /** INT-15: File Uploads: upload a single-part file sent as stream */
  @Test
  public void testFileUploadSinglePartBigFileInt15() throws IOException {
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

  /** INT-18: File Uploads: upload a single-part file as byte[] */
  @Test
  public void testFileUploadSinglePartInt18() throws IOException {
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

  /** INT-19: File Uploads: import external file */
  @Test
  public void testFileUploadExternalInt19() throws IOException {
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

  /** INT-20: File Uploads: upload a multi-part file (not supported on free plan) */
  // @Test
  // TODO implement possibility to skip test if plan is free
  // @NotionPlan(acceptedPlans = {NotionPlan.PRO, NotionPlan.ENTERPRISE})
  public void testFileUploadMultiPartInt20() throws IOException {
    // prerequisites
    String filePath = SETTINGS.getString(VIDEO_FILE_PATH);
    File file = IntegrationTestUtil.loadFileFailIfMissing(filePath, getClass().getClassLoader());
    String uploadedFilename = "int-20-video_18mb.jpg";
    long partSizeInBytes = 5 * 1024 * 1024; // 5 MB
    int numberOfParts = FileUploadUtils.calculateNumberOfParts(file.length(), partSizeInBytes);

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
