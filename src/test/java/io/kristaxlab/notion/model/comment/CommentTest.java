package io.kristaxlab.notion.model.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  private static String fixture(String name) throws IOException {
    try (var stream = CommentTest.class.getResourceAsStream("/json/" + name)) {
      assertNotNull(stream, "Missing fixture: " + name);
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Test
  @DisplayName("deserialize official comment object example")
  void deserialize_officialCommentObjectExample() throws IOException {
    Comment comment = JSON.toObject(fixture("retrieve-comment.json"), Comment.class);

    assertEquals("comment", comment.getObject());
    assertEquals("7a793800-3e55-4d5e-8009-2261de026179", comment.getId());
    assertEquals("page_id", comment.getParent().getType());
    assertEquals("5c6a2821-6bb1-4a7e-b6e1-c50111515c3d", comment.getParent().getPageId());
    assertEquals("f4be6752-a539-4da2-a8a9-c3953e13bc0b", comment.getDiscussionId());
    assertEquals("2022-07-15T21:17:00.000Z", comment.getCreatedTime());
    assertEquals("2022-07-15T21:17:00.000Z", comment.getLastEditedTime());
    assertEquals("e450a39e-9051-4d36-bc4e-8581611fc592", comment.getCreatedBy().getId());
    assertEquals(1, comment.getRichText().size());
    assertEquals("Hello world", comment.getRichText().get(0).getPlainText());
    assertEquals(1, comment.getAttachments().size());
    assertEquals("image", comment.getAttachments().get(0).getCategory());
    assertEquals(
        "https://s3.us-west-2.amazonaws.com/example.png",
        comment.getAttachments().get(0).getFile().getUrl());
    assertEquals(
        "2025-06-10T21:58:51.599Z", comment.getAttachments().get(0).getFile().getExpiryTime());
    assertEquals("user", comment.getDisplayName().getType());
    assertEquals("Avo Cado", comment.getDisplayName().getResolvedName());
    assertEquals(Boolean.FALSE, comment.getOriginalContentDeleted());
  }

  @Test
  @DisplayName("serialize create params uses snake case and omits nulls")
  void serialize_createParamsUsesSnakeCaseAndOmitsNulls() {
    CommentCreateParams params =
        CommentCreateParams.builder()
            .inPage("page-1")
            .richText("Hello")
            .attachment(CommentAttachment.fileUpload("upload-1"))
            .displayName(CommentDisplayName.custom("Notion Bot"))
            .build();

    String json = JSON.toJson(params);

    assertTrue(json.contains("\"page_id\""));
    assertTrue(json.contains("\"rich_text\""));
    assertTrue(json.contains("\"file_upload_id\""));
    assertTrue(json.contains("\"display_name\""));
    assertTrue(json.contains("\"resolved_name\"") == false);
    assertTrue(json.contains("discussion_id") == false);
    assertTrue(json.contains("markdown") == false);
  }

  @Test
  @DisplayName("file upload factory sets request fields")
  void fileUploadFactory_setsRequestFields() {
    CommentAttachment attachment = CommentAttachment.fileUpload("upload-1");

    assertEquals("upload-1", attachment.getFileUploadId());
    assertEquals("file_upload", attachment.getType());
    assertNull(attachment.getCategory());
    assertNull(attachment.getFile());
  }

  @Test
  @DisplayName("display name factories set request types")
  void displayNameFactories_setRequestTypes() {
    assertEquals(CommentDisplayNameType.USER.type(), CommentDisplayName.user().getType());
    assertEquals(
        CommentDisplayNameType.INTEGRATION.type(), CommentDisplayName.integration().getType());

    CommentDisplayName custom = CommentDisplayName.custom("Notion Bot");
    assertEquals(CommentDisplayNameType.CUSTOM.type(), custom.getType());
    assertNotNull(custom.getCustom());
    assertEquals("Notion Bot", custom.getCustom().getName());
    assertNull(custom.getResolvedName());
  }
}
