package io.kristixlab.notion.api.model.files;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import org.junit.jupiter.api.Test;

public class FileUploadDeserializationTest extends BaseTest {

  @Test
  void testFileUploadCreateResponse() throws Exception {
    FileUploadResponse response =
        loadFromFile("file-uploads/file-uploads-create-rs.json", FileUploadResponse.class);

    assertEquals("file_upload", response.getObject());
    assertEquals("25eb90f8-6d76-81db-b720-00b2848e40bd", response.getId());
    assertEquals("2025-08-29T13:10:00.000Z", response.getCreatedTime());
    assertEquals("2025-08-29T13:10:00.000Z", response.getLastEditedTime());
    assertEquals("2025-08-29T14:10:00.000Z", response.getExpiryTime());
    assertEquals(
        "https://api.notion.com/v1/file_uploads/25eb90f8-6d76-81db-b720-00b2848e40bd/send",
        response.getUploadUrl());
    assertFalse(response.getArchived());
    assertEquals("pending", response.getStatus());
    assertEquals("snowy_day.jpg", response.getFilename());
    assertEquals("image/jpeg", response.getContentType());
    assertNull(response.getContentLength());
  }

  @Test
  void testFileUploadSendResponse() throws Exception {
    FileUploadResponse response =
        loadFromFile("file-uploads/file-upload-send-rs.json", FileUploadResponse.class);

    assertEquals("file_upload", response.getObject());
    assertEquals("25eb90f8-6d76-8143-8ff7-00b2486a195a", response.getId());
    assertEquals("2025-08-29T13:48:00.000Z", response.getCreatedTime());
    assertEquals("2025-08-29T13:48:00.000Z", response.getLastEditedTime());
    assertEquals("2025-08-29T14:48:00.000Z", response.getExpiryTime());
    assertFalse(response.getArchived());
    assertEquals("uploaded", response.getStatus());
    assertEquals("snowy_day.jpg", response.getFilename());
    assertEquals("image/jpeg", response.getContentType());
    assertEquals(366112L, response.getContentLength());
  }

  @Test
  void testFileUploadRetrieveById() throws Exception {
    FileUploadResponse response =
        loadFromFile("file-uploads/file-uploads-retrieve-by-id-rs.json", FileUploadResponse.class);

    assertEquals("file_upload", response.getObject());
    assertNotNull(response.getId());
    assertNotNull(response.getCreatedTime());
    assertNotNull(response.getLastEditedTime());
    assertNotNull(response.getFilename());
    assertNotNull(response.getContentType());
    assertNotNull(response.getStatus());
  }

  @Test
  void testFileUploadListResponse() throws Exception {
    FileUploadList response =
        loadFromFile("file-uploads/file-uploads-list-all.json", FileUploadList.class);

    assertEquals("list", response.getObject());
    assertNotNull(response.getResults());
    assertTrue(response.getResults().size() > 0);

    // Test first file upload in the list
    FileUploadResponse firstUpload = response.getResults().get(0);
    assertEquals("file_upload", firstUpload.getObject());
    assertEquals("25eb90f8-6d76-8143-8ff7-00b2486a195a", firstUpload.getId());
    assertEquals("2025-08-29T13:48:00.000Z", firstUpload.getCreatedTime());
    assertEquals("2025-08-29T13:48:00.000Z", firstUpload.getLastEditedTime());
    assertEquals("2025-08-29T14:48:00.000Z", firstUpload.getExpiryTime());
    assertFalse(firstUpload.getArchived());
    assertEquals("uploaded", firstUpload.getStatus());
    assertEquals("snowy_day.jpg", firstUpload.getFilename());
    assertEquals("image/jpeg", firstUpload.getContentType());
    assertEquals(366112L, firstUpload.getContentLength());

    // Test second file upload in the list
    if (response.getResults().size() > 1) {
      FileUploadResponse secondUpload = response.getResults().get(1);
      assertEquals("file_upload", secondUpload.getObject());
      assertEquals("25eb90f8-6d76-81d8-ab09-00b24d5ff149", secondUpload.getId());
      assertEquals("snowy_day.jpg", secondUpload.getFilename());
      assertEquals("image/jpeg", secondUpload.getContentType());
    }
  }
}
