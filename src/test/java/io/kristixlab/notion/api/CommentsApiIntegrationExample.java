package io.kristixlab.notion.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristixlab.notion.api.model.IntegrationTest;
import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentDisplayName;
import io.kristixlab.notion.api.model.comments.CommentsList;
import io.kristixlab.notion.api.model.comments.CustomDisplayName;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test for CommentsApi that calls actual Notion API endpoints. Stores all responses to
 * files for inspection.
 */
public class CommentsApiIntegrationExample extends IntegrationTest {

  private static final String PAGE_ID = "246c5b968ec480a6b8d6f61a77fd8fd6";
  private static final String BLOCK_ID = "253c5b968ec4803ab04cee96020d5eaf";
  private static final String COMMENT_ID = "254c5b96-8ec4-809f-8fe7-001df0c687fb";

  private static CommentsApi commentsApi;

  @BeforeEach
  protected void setup() throws Exception {
    super.setUp();
    commentsApi = new CommentsApi(getTransport());
  }

  /** Test creating a comment on a page. This will create a new comment and save the response. */
  @Test
  void testCreateComment() throws IOException {
    Comment comment = createCommentRequest("reply from user", "purple");
    saveToFile(comment, "comment-create-rq.json");

    Comment createdComment = commentsApi.create(comment);
    saveToFile(createdComment, "comment-create-rs.json");

    // Validate the created comment
    assertNotNull(createdComment);
    assertNotNull(createdComment.getId());
    assertEquals("comment", createdComment.getObject());
    assertNotNull(createdComment.getRichText());
    assertNotNull(createdComment.getCreatedTime());
    assertNotNull(createdComment.getCreatedBy());
  }

  /**
   * Test retrieving comments for a block. This will retrieve comments with default parameters and
   * save the response.
   */
  @Test
  void testRetrieveComment() throws IOException {
    Comment response = commentsApi.retrieve(COMMENT_ID);
    saveToFile(response, "comment-retrieve-response.json");

    // Validate the response
    assertNotNull(response);
  }

  /**
   * Test retrieving comments for a block. This will retrieve comments with default parameters and
   * save the response.
   */
  @Test
  void testRetrieveCommentsForBlock() throws IOException {
    CommentsList response = commentsApi.listComments(BLOCK_ID);
    saveToFile(response, "comments-list-for-block-response.json");

    assertNotNull(response);
  }

  /**
   * Test retrieving comments for a block. This will retrieve comments with default parameters and
   * save the response.
   */
  @Test
  void testRetrieveCommentsForPage() throws IOException {
    CommentsList response = commentsApi.listComments(PAGE_ID);
    saveToFile(response, "comments-list-for-page-response.json");

    assertNotNull(response);
    assertNotNull(response.getResults());
  }

  /**
   * Test retrieving comments with pagination. This will retrieve comments with page size limit and
   * save the response.
   */
  @Test
  void testRetrieveCommentsWithPagination() throws IOException {
    CommentsList response = commentsApi.listComments(BLOCK_ID, null, 5);
    saveToFile(response, "comments-list-paginated-response.json");

    // Validate the response
    assertNotNull(response);
    assertNotNull(response.getResults());
  }

  /**
   * Test deserializing a saved comment response. This will load a comment response from file and
   * re-serialize it.
   */
  @Test
  void testCommentDeserialization() throws IOException {
    // This test assumes a comment response has been saved previously
    try {
      CommentsList loadedResponse =
          loadFromFile("comments-retrieve-block-response.json", CommentsList.class);
      saveToFile(loadedResponse, "comments-deserialized-response.json");

      assertNotNull(loadedResponse);
      assertNotNull(loadedResponse.getResults());
      System.out.println(
          "Successfully deserialized " + loadedResponse.getResults().size() + " comments");
    } catch (Exception e) {
      System.out.println("No existing comment response to deserialize: " + e.getMessage());
    }
  }

  /** Creates a comment request object for testing. */
  private Comment createCommentRequest(String text, String color) {
    Comment comment = new Comment();
    comment.setDiscussionId("254c5b96-8ec4-809e-b971-001c658e6652");
    // Set parent to the specified page
    Parent parent = new Parent();
    parent.setType("page_id");
    parent.setPageId(BLOCK_ID);
    // comment.setParent(parent);

    // Set rich text content
    RichText richText = new RichText();
    richText.setType("text");
    richText.setPlainText(text);

    RichText.Text textContent = new RichText.Text();
    textContent.setContent(text);
    richText.setText(textContent);

    // Set annotations (optional)
    RichText.Annotations annotations = new RichText.Annotations();
    annotations.setBold(true);
    annotations.setColor(color);
    richText.setAnnotations(annotations);

    comment.setRichText(List.of(richText));
    comment.setDisplayName(new CommentDisplayName());
    comment.getDisplayName().setType("custom");
    comment.getDisplayName().setCustom(new CustomDisplayName());
    comment.getDisplayName().getCustom().setName("Custom Name");
    return comment;
  }
}
