package tests.comments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristaxlab.notion.model.comment.Comment;
import io.kristaxlab.notion.model.comment.CommentAttachment;
import io.kristaxlab.notion.model.comment.CommentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;
import testkit.util.FileLoader;

public class IT35_Comments_Attachment extends WithEmptyTestPage {

  private static final String IMAGE_PATH = "files/image_357kb.jpg";
  private static final String IMAGE_NAME = "image.jpg";

  private String fileUploadId;

  @BeforeEach
  public void setup() {
    fileUploadId = FileLoader.uploadFile(IMAGE_PATH, IMAGE_NAME, getSetupClient());
  }

  @Test
  @DisplayName("IT-35: Comments - Create a page comment with an attachment")
  public void testCreateCommentWithAttachment() {
    Comment created =
        getNotionClient()
            .comments()
            .create(
                CommentCreateParams.builder()
                    .inPage(getTestPageId())
                    .richText("Comment with an attached image")
                    .attachment(CommentAttachment.fileUpload(fileUploadId))
                    .build());

    assertNotNull(created.getId());
    assertEquals("comment", created.getObject());
    assertNotNull(created.getAttachments());
    assertEquals(1, created.getAttachments().size());

    CommentAttachment attached = created.getAttachments().get(0);
    assertEquals("image", attached.getCategory());
    assertNotNull(attached.getFile());
    assertNotNull(attached.getFile().getUrl());

    Comment retrieved = getNotionClient().comments().retrieve(created.getId());
    assertEquals(created.getId(), retrieved.getId());
    assertNotNull(retrieved.getAttachments());
    assertEquals(1, retrieved.getAttachments().size());
    assertEquals("image", retrieved.getAttachments().get(0).getCategory());
    assertNotNull(retrieved.getAttachments().get(0).getFile());
    assertNotNull(retrieved.getAttachments().get(0).getFile().getUrl());
  }
}
