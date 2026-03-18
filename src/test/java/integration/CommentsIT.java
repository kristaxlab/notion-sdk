package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.blocks.*;
import io.kristixlab.notion.api.model.comments.*;
import io.kristixlab.notion.api.model.common.*;
import java.util.List;
import org.junit.jupiter.api.*;

public class CommentsIT extends BaseIntegrationTest {

  private static String commentsTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    commentsTestsPageId = IntegrationTestAssisstant.createPageForTests("Pages");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(info.getDisplayName(), commentsTestsPageId);
  }

  @Test
  @DisplayName("[IT-34]: Comments & Pages - Add 3 comments to a page and retrieve them")
  public void addCommentsToPage() {
    // Step 1: Add a plain-text comment to the page
    CreateCommentParams firstCommentRq =
        createCommentRequest("First comment on the page", Parent.pageParent(currTestPageId));
    Comment firstComment = getNotion().comments().create(firstCommentRq);

    assertNotNull(firstComment);
    assertNotNull(firstComment.getId());
    assertNotNull(firstComment.getDiscussionId());
    assertEquals("First comment on the page", firstComment.getRichText().get(0).getPlainText());

    // Step 2: Add a second comment with an image attachment
    CreateCommentParams secondComment =
        createCommentRequest("Comment with image", Parent.pageParent(currTestPageId));
    CommentAttachment imageAttachment = new CommentAttachment();
    imageAttachment.setFileUploadId(
        IntegrationTestAssisstant.getPrerequisites().getImageFileUploadId());
    secondComment.setAttachments(List.of(imageAttachment));
    Comment attachmentComment = getNotion().comments().create(secondComment);

    assertNotNull(attachmentComment);
    assertNotNull(attachmentComment.getId());
    assertNotNull(attachmentComment.getDiscussionId());

    // Step 3: Reply to the attachment comment with a custom display name
    CreateCommentParams replyRq = new CreateCommentParams();
    CustomDisplayName customName = new CustomDisplayName();
    customName.setName("Hejka");
    CommentDisplayName displayName = new CommentDisplayName();
    displayName.setType("custom");
    displayName.setCustom(customName);

    replyRq.setDiscussionId(attachmentComment.getDiscussionId());
    replyRq.setRichText(
        RichText.builder().fromText("Reply to the attachment comment").buildAsList());
    replyRq.setDisplayName(displayName);
    Comment reply = getNotion().comments().create(replyRq);

    assertNotNull(reply);
    assertNotNull(reply.getId());
    assertEquals(attachmentComment.getDiscussionId(), reply.getDiscussionId());

    // Step 3: Retrieve all the comments
    CommentList commentsList = getNotion().comments().listComments(currTestPageId);
    assertNotNull(commentsList);
    assertEquals(3, commentsList.getResults().size());
  }

  @Test
  @DisplayName("[IT-9]: Comments - Add comments to a block and retrieve it")
  public void addCommentToBlock() {
    // Step 1: Append a paragraph block to a testing page
    String blockId =
        getNotion()
            .blocks()
            .appendChildren(currTestPageId, ParagraphBlock.of("Text to comment on"))
            .getResults()
            .get(0)
            .getId();

    // Step 2: Add a comment to the whole block
    CreateCommentParams rq =
        createCommentRequest("Comment on the whole block", Parent.blockParent(blockId));
    Comment comment = getNotion().comments().create(rq);

    assertNotNull(comment);
    assertNotNull(comment.getId());
    assertNotNull(comment.getDiscussionId());

    // Step 3: List comments on the block and verify
    Comment retrieved = getNotion().comments().retrieve(comment.getId());
    assertNotNull(retrieved);
    assertEquals(comment.getId(), retrieved.getId());
    assertNotNull(retrieved.getParent());
    assertNotNull(retrieved.getDiscussionId());
    assertNotNull(retrieved.getDisplayName());
    assertNull(retrieved.getAttachments());
  }

  // --- helpers ---

  private CreateCommentParams createCommentRequest(String commentText, Parent parent) {
    CreateCommentParams rq = new CreateCommentParams();
    rq.setParent(parent);
    rq.setRichText(RichText.builder().fromText(commentText).buildAsList());
    return rq;
  }
}
