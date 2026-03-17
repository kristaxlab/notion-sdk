package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.model.blocks.*;
import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentAttachment;
import io.kristixlab.notion.api.model.comments.CommentDisplayName;
import io.kristixlab.notion.api.model.comments.CreateCommentRequest;
import io.kristixlab.notion.api.model.comments.CustomDisplayName;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.properties.TitleProperty;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.*;

public class CommentsIT extends BaseIntegrationTest {

  private static String commentsTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    commentsTestsPageId = IntegrationTestAssisstant.createTestsPage("Pages");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createTestsPage(info.getDisplayName(), commentsTestsPageId);
  }

  @Test
  @DisplayName("[IT-34]: Pages - Add comments to a page")
  public void addCommentsToPage() {
    // Step 1: Create a new page
    Page page =
        getNotion().pages().create(createPageParams("A page with comments", currTestPageId));
    String pageId = page.getId();

    // Step 2: Add a plain-text comment to the page
    CreateCommentRequest firstCommentRq = createCommentRequest("First comment on the page", pageId);
    Comment firstComment = getNotion().comments().create(firstCommentRq);

    assertNotNull(firstComment);
    assertNotNull(firstComment.getId());
    assertNotNull(firstComment.getDiscussionId());
    assertEquals("First comment on the page", firstComment.getRichText().get(0).getPlainText());

    // Step 3: Add a second comment with an image attachment
    CreateCommentRequest secondComment = createCommentRequest("Comment with image", pageId);
    CommentAttachment imageAttachment = new CommentAttachment();
    imageAttachment.setFileUploadId(
        IntegrationTestAssisstant.getPrerequisites().getImageFileUploadId());
    secondComment.setAttachments(List.of(imageAttachment));
    Comment attachmentComment = getNotion().comments().create(secondComment);

    assertNotNull(attachmentComment);
    assertNotNull(attachmentComment.getId());
    assertNotNull(attachmentComment.getDiscussionId());

    // Step 4: Reply to the attachment comment with a custom display name
    CreateCommentRequest replyRq = new CreateCommentRequest();
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
  }

  // --- helpers ---

  private CreatePageParams createPageParams(String title, String pageId) {
    CreatePageParams newPage = new CreatePageParams();
    newPage.setProperties(new HashMap<>());
    newPage.getProperties().put("title", TitleProperty.of(title));
    newPage.setParent(Parent.pageParent(pageId));
    return newPage;
  }

  private CreateCommentRequest createCommentRequest(String commentText, String parentId) {
    CreateCommentRequest rq = new CreateCommentRequest();
    rq.setParent(Parent.pageParent(parentId));
    rq.setRichText(RichText.builder().fromText(commentText).buildAsList());
    return rq;
  }
}
