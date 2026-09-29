package tests.comments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.comment.Comment;
import io.kristaxlab.notion.model.comment.CommentCreateParams;
import io.kristaxlab.notion.model.comment.CommentDisplayName;
import io.kristaxlab.notion.model.comment.CommentList;
import io.kristaxlab.notion.model.comment.CommentUpdateParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT33_Comments_Page_CRUD extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-33: Comments - Create, list, retrieve, update and delete a page comment")
  public void testCommentCrud() {
    Comment created =
        getNotionClient()
            .comments()
            .create(
                CommentCreateParams.builder()
                    .inPage(getTestPageId())
                    .richText("Hello from the SDK")
                    .displayName(CommentDisplayName.user())
                    .build());

    assertNotNull(created);
    assertNotNull(created.getId());
    assertEquals("comment", created.getObject());
    assertNotNull(created.getParent());
    assertEquals("page_id", created.getParent().getType());
    assertEquals(getTestPageId(), created.getParent().getPageId());
    assertEquals("Hello from the SDK", created.getRichText().get(0).getPlainText());
    assertNotNull(created.getDisplayName());
    assertEquals("custom", created.getDisplayName().getType());
    assertEquals("SDK bot", created.getDisplayName().getResolvedName());

    CommentList listed = getNotionClient().comments().listComments(getTestPageId());
    assertEquals("list", listed.getObject());
    assertEquals("comment", listed.getType());
    assertNotNull(listed.getResults());
    assertTrue(
        listed.getResults().stream().anyMatch(comment -> created.getId().equals(comment.getId())),
        "Created comment should appear in the comment list");

    CommentList page = getNotionClient().comments().listComments(getTestPageId(), null, 1);
    assertNotNull(page.getResults());
    assertEquals(1, page.getResults().size());

    Comment retrieved = getNotionClient().comments().retrieve(created.getId());
    assertEquals(created.getId(), retrieved.getId());
    assertEquals(created.getDiscussionId(), retrieved.getDiscussionId());
    assertEquals("Hello from the SDK", retrieved.getRichText().get(0).getPlainText());

    Comment updated =
        getNotionClient()
            .comments()
            .update(
                created.getId(),
                CommentUpdateParams.builder().markdown("Updated **comment**").build());
    assertEquals(created.getId(), updated.getId());
    assertNotNull(updated.getRichText());
    assertTrue(
        updated.getRichText().stream()
            .map(text -> text.getPlainText())
            .reduce("", String::concat)
            .contains("Updated"));

    Comment deleted = getNotionClient().comments().delete(created.getId());
    assertEquals(created.getId(), deleted.getId());

    CommentList afterDelete = getNotionClient().comments().listComments(getTestPageId());
    assertNotNull(afterDelete.getResults());
    assertTrue(afterDelete.getResults().isEmpty());
  }
}
