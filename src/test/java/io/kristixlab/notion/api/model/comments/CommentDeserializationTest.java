package io.kristixlab.notion.api.model.comments;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import io.kristixlab.notion.api.model.common.RichText;
import org.junit.jupiter.api.Test;

public class CommentDeserializationTest extends BaseTest {

  @Test
  void testSingleComment() throws Exception {
    Comment singleComment = loadFromFile("comments-retrieve.json", Comment.class);

    // basic fields
    assertEquals("comment", singleComment.getObject());
    assertEquals("255c5b96-8ec4-8182-ab3a-001d9f559edd", singleComment.getId());
    assertEquals("254c5b96-8ec4-809e-b971-001c658e6652", singleComment.getDiscussionId());
    assertEquals("2025-08-20T12:21:00.000Z", singleComment.getCreatedTime());
    assertEquals("2025-08-20T12:21:00.000Z", singleComment.getLastEditedTime());
    assertEquals("e2fef9e6-29de-4fd6-bf63-c064133fb03c", singleComment.getRequestId());
    assertNotNull(singleComment.getCreatedBy());
    assertEquals("user", singleComment.getCreatedBy().getObject());
    assertEquals("22222222-4444-43b3-8288-222222222222", singleComment.getCreatedBy().getId());

    // parent
    assertNotNull(singleComment.getParent());
    assertEquals("block_id", singleComment.getParent().getType());
    assertEquals("253c5b96-8ec4-803a-b04c-ee96020d5eaf", singleComment.getParent().getBlockId());

    // rich text
    assertNotNull(singleComment.getRichText());
    assertEquals(1, singleComment.getRichText().size());

    RichText richText = singleComment.getRichText().get(0);
    assertEquals("text", richText.getType());
    assertEquals("reply", richText.getPlainText());
    assertNull(richText.getHref());

    // Test text content
    assertNotNull(richText.getText());
    assertEquals("reply", richText.getText().getContent());
    assertNull(richText.getText().getLink());

    // Test annotations
    assertNotNull(richText.getAnnotations());
    assertTrue(richText.getAnnotations().isBold());
    assertFalse(richText.getAnnotations().isItalic());
    assertFalse(richText.getAnnotations().isStrikethrough());
    assertFalse(richText.getAnnotations().isUnderline());
    assertFalse(richText.getAnnotations().isCode());
    assertEquals("purple", richText.getAnnotations().getColor());

    assertNotNull(singleComment.getDisplayName());
    assertEquals("integration", singleComment.getDisplayName().getType());
    assertEquals("MakeIntegration", singleComment.getDisplayName().getResolvedName());
  }

  @Test
  void testCommentsList() throws Exception {
    CommentsList commentsList = loadFromFile("comments-list.json", CommentsList.class);
    assertEquals("list", commentsList.getObject());
    assertEquals("8b40bef4-a6ea-4ac0-a460-7705f3b2fa04", commentsList.getRequestId());
    assertFalse(commentsList.hasMore());
    assertNull(commentsList.getNextCursor());
    assertEquals("comment", commentsList.getType());
    assertNotNull(commentsList.getCommentMetadata()); // Empty comment object

    assertNotNull(commentsList.getResults());
    assertEquals(11, commentsList.getResults().size());

    // Test first comment
    Comment firstComment = commentsList.getResults().get(0);
    assertEquals("comment", firstComment.getObject());
    assertEquals("254c5b96-8ec4-809f-8fe7-001df0c687fb", firstComment.getId());
    assertEquals("254c5b96-8ec4-800e-8221-001c0bea0076", firstComment.getDiscussionId());
    assertEquals("2025-08-19T12:26:00.000Z", firstComment.getCreatedTime());
    assertEquals("2025-08-19T12:26:00.000Z", firstComment.getLastEditedTime());

    // Test parent
    assertNotNull(firstComment.getParent());
    assertEquals("block_id", firstComment.getParent().getType());
    assertEquals("253c5b96-8ec4-803a-b04c-ee96020d5eaf", firstComment.getParent().getBlockId());

    // Test rich text content
    assertNotNull(firstComment.getRichText());
    assertEquals(1, firstComment.getRichText().size());
    RichText richText = firstComment.getRichText().get(0);
    assertEquals("jjjj", richText.getPlainText());
    assertEquals("text", richText.getType());
    assertEquals("default", richText.getAnnotations().getColor());
    assertFalse(richText.getAnnotations().isCode());

    // Test display name
    assertNotNull(firstComment.getDisplayName());
    assertEquals("integration", firstComment.getDisplayName().getType());
    assertEquals("MakeIntegration", firstComment.getDisplayName().getResolvedName());

    // Test that comments are created by different users
    Comment userAComment = commentsList.getResults().get(0);
    Comment userBComment = commentsList.getResults().get(1);

    assertEquals("44444444-4444-43b3-8288-444444444444", userAComment.getCreatedBy().getId());
    assertEquals("22222222-4444-43b3-8288-222222222222", userBComment.getCreatedBy().getId());

    // Both should be user objects
    assertEquals("user", userAComment.getCreatedBy().getObject());
    assertEquals("user", userBComment.getCreatedBy().getObject());

    // Test that comments belong to different discussion threads
    Comment firstDiscussion = commentsList.getResults().get(0);
    Comment secondDiscussion = commentsList.getResults().get(2);
    Comment thirdDiscussion = commentsList.getResults().get(7);

    // test comments list different discussions
    assertEquals("254c5b96-8ec4-800e-8221-001c0bea0076", firstDiscussion.getDiscussionId());
    assertEquals("254c5b96-8ec4-809e-b971-001c658e6652", secondDiscussion.getDiscussionId());
    assertEquals("254c5b96-8ec4-8086-830c-001c3720fcdc", thirdDiscussion.getDiscussionId());

    // Verify they all have the same parent block
    assertEquals("253c5b96-8ec4-803a-b04c-ee96020d5eaf", firstDiscussion.getParent().getBlockId());
    assertEquals("253c5b96-8ec4-803a-b04c-ee96020d5eaf", secondDiscussion.getParent().getBlockId());
    assertEquals("253c5b96-8ec4-803a-b04c-ee96020d5eaf", thirdDiscussion.getParent().getBlockId());
  }

  @Test
  void testCommentsListWithAttachments() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-attachments.json", CommentsList.class);
    assertEquals("list", commentsWithAttachments.getObject());
    assertEquals("4474c6bb-fe9a-444a-9251-cefa3abc6908", commentsWithAttachments.getRequestId());
    assertFalse(commentsWithAttachments.hasMore());
    assertNull(commentsWithAttachments.getNextCursor());
    assertEquals("comment", commentsWithAttachments.getType());
    assertNotNull(commentsWithAttachments.getCommentMetadata()); // Empty comment object

    assertNotNull(commentsWithAttachments.getResults());
    assertEquals(7, commentsWithAttachments.getResults().size());

    // Test first comment with image attachment
    Comment firstComment = commentsWithAttachments.getResults().get(0);
    assertEquals("comment", firstComment.getObject());
    assertEquals("255c5b96-8ec4-803b-9ef5-001d0d803418", firstComment.getId());
    assertEquals("254c5b96-8ec4-8086-830c-001c3720fcdc", firstComment.getDiscussionId());
    assertEquals("2025-08-20T13:37:00.000Z", firstComment.getCreatedTime());
    assertEquals("2025-08-20T13:37:00.000Z", firstComment.getLastEditedTime());

    // Test parent
    assertNotNull(firstComment.getParent());
    assertEquals("block_id", firstComment.getParent().getType());
    assertEquals("253c5b96-8ec4-803a-b04c-ee96020d5eaf", firstComment.getParent().getBlockId());

    // Test rich text content
    assertNotNull(firstComment.getRichText());
    assertEquals(1, firstComment.getRichText().size());
    RichText richText = firstComment.getRichText().get(0);
    assertEquals("comment with image attachment", richText.getPlainText());
    assertEquals("text", richText.getType());
    assertEquals("default", richText.getAnnotations().getColor());
    assertFalse(richText.getAnnotations().isBold());

    // Test display name
    assertNotNull(firstComment.getDisplayName());
    assertEquals("integration", firstComment.getDisplayName().getType());
    assertEquals("MakeIntegration", firstComment.getDisplayName().getResolvedName());

    // Test created by
    assertNotNull(firstComment.getCreatedBy());
    assertEquals("44444444-4444-43b3-8288-444444444444", firstComment.getCreatedBy().getId());
    assertEquals("user", firstComment.getCreatedBy().getObject());
  }

  @Test
  void testCommentsWithImageAttachment() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment imageComment = commentsWithAttachments.getResults().get(0);

    // Test attachment exists
    assertNotNull(imageComment.getAttachments());
    assertEquals(1, imageComment.getAttachments().size());

    // Test image attachment properties
    CommentAttachment attachment = imageComment.getAttachments().get(0);
    assertEquals("image", attachment.getCategory());
    assertNotNull(attachment.getFile());
    assertTrue(attachment.getFile().getUrl().contains("IMG_6167.png"));
    assertEquals("2025-08-20T15:50:38.768Z", attachment.getFile().getExpiryTime());
    assertTrue(
        attachment
            .getFile()
            .getUrl()
            .startsWith("https://prod-files-secure.s3.us-west-2.amazonaws.com/"));
  }

  @Test
  void testCommentsWithArchiveAttachment() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment archiveComment = commentsWithAttachments.getResults().get(1);

    assertEquals("255c5b96-8ec4-803e-9969-001de8d60d6d", archiveComment.getId());
    assertEquals("comment with archive", archiveComment.getRichText().get(0).getPlainText());

    // Test attachment exists
    assertNotNull(archiveComment.getAttachments());
    assertEquals(1, archiveComment.getAttachments().size());

    // Test archive attachment properties
    CommentAttachment attachment = archiveComment.getAttachments().get(0);
    assertEquals("productivity", attachment.getCategory());
    assertNotNull(attachment.getFile());
    assertTrue(attachment.getFile().getUrl().contains("archive.zip"));
    assertEquals("2025-08-20T15:50:38.804Z", attachment.getFile().getExpiryTime());
  }

  @Test
  void testCommentsWithVideoAttachment() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment videoComment = commentsWithAttachments.getResults().get(2);

    assertEquals("255c5b96-8ec4-807b-821f-001df58a7e4e", videoComment.getId());
    assertEquals("comment with video", videoComment.getRichText().get(0).getPlainText());
    assertEquals("2025-08-20T13:43:00.000Z", videoComment.getCreatedTime());

    // Test attachment exists
    assertNotNull(videoComment.getAttachments());
    assertEquals(1, videoComment.getAttachments().size());

    // Test video attachment properties
    CommentAttachment attachment = videoComment.getAttachments().get(0);
    assertEquals("video", attachment.getCategory());
    assertNotNull(attachment.getFile());
    assertTrue(attachment.getFile().getUrl().contains("video.mp4"));
    assertEquals("2025-08-20T15:50:38.838Z", attachment.getFile().getExpiryTime());
  }

  @Test
  void testCommentsWithPdfAttachment() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment pdfComment = commentsWithAttachments.getResults().get(3);

    assertEquals("255c5b96-8ec4-80f3-a55b-001da6cf62b1", pdfComment.getId());
    assertEquals("comment with pdf", pdfComment.getRichText().get(0).getPlainText());

    // Test attachment exists
    assertNotNull(pdfComment.getAttachments());
    assertEquals(1, pdfComment.getAttachments().size());

    // Test PDF attachment properties
    CommentAttachment attachment = pdfComment.getAttachments().get(0);
    assertEquals("productivity", attachment.getCategory());
    assertNotNull(attachment.getFile());
    assertTrue(attachment.getFile().getUrl().contains("weekly_flower_tracker.pdf"));
    assertEquals("2025-08-20T15:50:38.906Z", attachment.getFile().getExpiryTime());
  }

  @Test
  void testCommentsWithAudioAttachment() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment audioComment = commentsWithAttachments.getResults().get(4);

    assertEquals("255c5b96-8ec4-80b5-9b54-001d8cf8cb10", audioComment.getId());
    assertEquals("comment with audio", audioComment.getRichText().get(0).getPlainText());
    assertEquals("2025-08-20T13:44:00.000Z", audioComment.getCreatedTime());

    // Test attachment exists
    assertNotNull(audioComment.getAttachments());
    assertEquals(1, audioComment.getAttachments().size());

    // Test audio attachment properties
    CommentAttachment attachment = audioComment.getAttachments().get(0);
    assertEquals("audio", attachment.getCategory());
    assertNotNull(attachment.getFile());
    assertTrue(attachment.getFile().getUrl().contains("audio-voice.wav"));
    assertEquals("2025-08-20T15:50:38.942Z", attachment.getFile().getExpiryTime());
  }

  @Test
  void testCommentsWithPowerPointAttachment() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment slidesComment = commentsWithAttachments.getResults().get(5);

    assertEquals("255c5b96-8ec4-80d8-b338-001df9983252", slidesComment.getId());
    assertEquals("comment with slides", slidesComment.getRichText().get(0).getPlainText());
    assertEquals("2025-08-20T13:46:00.000Z", slidesComment.getCreatedTime());

    // Test attachment exists
    assertNotNull(slidesComment.getAttachments());
    assertEquals(1, slidesComment.getAttachments().size());

    // Test PowerPoint attachment properties
    CommentAttachment attachment = slidesComment.getAttachments().get(0);
    assertEquals("productivity", attachment.getCategory());
    assertNotNull(attachment.getFile());
    assertTrue(attachment.getFile().getUrl().contains("slides.pptx"));
    assertEquals("2025-08-20T15:50:39.001Z", attachment.getFile().getExpiryTime());
  }

  @Test
  void testCommentsWithMultipleAttachments() throws Exception {
    CommentsList commentsWithAttachments =
        loadFromFile("comments-list-with-attachments.json", CommentsList.class);
    Comment multiAttachmentComment = commentsWithAttachments.getResults().get(6);

    assertEquals("255c5b96-8ec4-8054-9931-001dc4664082", multiAttachmentComment.getId());
    assertEquals("list of attachments", multiAttachmentComment.getRichText().get(0).getPlainText());
    assertEquals("2025-08-20T13:49:00.000Z", multiAttachmentComment.getCreatedTime());

    // Test multiple attachments exist
    assertNotNull(multiAttachmentComment.getAttachments());
    assertEquals(3, multiAttachmentComment.getAttachments().size());

    // Test first attachment (PowerPoint)
    CommentAttachment firstAttachment = multiAttachmentComment.getAttachments().get(0);
    assertEquals("productivity", firstAttachment.getCategory());
    assertTrue(firstAttachment.getFile().getUrl().contains("slides.pptx"));
    assertEquals("2025-08-20T15:50:39.083Z", firstAttachment.getFile().getExpiryTime());

    // Test second attachment (Image)
    CommentAttachment secondAttachment = multiAttachmentComment.getAttachments().get(1);
    assertEquals("image", secondAttachment.getCategory());
    assertTrue(secondAttachment.getFile().getUrl().contains("sdk_5242016.png"));
    assertEquals("2025-08-20T15:50:39.101Z", secondAttachment.getFile().getExpiryTime());

    // Test third attachment (Archive)
    CommentAttachment thirdAttachment = multiAttachmentComment.getAttachments().get(2);
    assertEquals("productivity", thirdAttachment.getCategory());
    assertTrue(thirdAttachment.getFile().getUrl().contains("archive.zip"));
    assertEquals("2025-08-20T15:50:39.118Z", thirdAttachment.getFile().getExpiryTime());
  }
}
