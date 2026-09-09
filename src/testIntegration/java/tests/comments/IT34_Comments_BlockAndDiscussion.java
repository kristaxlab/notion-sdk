package tests.comments;

import static io.kristaxlab.notion.fluent.NotionBlocks.paragraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.comment.Comment;
import io.kristaxlab.notion.model.comment.CommentCreateParams;
import io.kristaxlab.notion.model.comment.CommentDisplayName;
import io.kristaxlab.notion.model.comment.CommentList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT34_Comments_BlockAndDiscussion extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-34: Comments - Comment on a block and reply with discussion_id")
  public void testBlockCommentAndDiscussionReply() {
    BlockList children =
        getSetupClient().blocks().appendChildren(getTestPageId(), paragraph("Commented block"));
    assertEquals(1, children.getResults().size());
    String blockId = children.getResults().get(0).getId();

    Comment onBlock =
        getNotionClient()
            .comments()
            .create(
                CommentCreateParams.builder()
                    .inBlock(blockId)
                    .richText("This comment is attached to a block.")
                    .displayName(CommentDisplayName.integration())
                    .build());

    assertNotNull(onBlock.getId());
    assertEquals("block_id", onBlock.getParent().getType());
    assertEquals(blockId, onBlock.getParent().getBlockId());
    assertNotNull(onBlock.getDiscussionId());
    assertEquals(
        "This comment is attached to a block.", onBlock.getRichText().get(0).getPlainText());

    Comment reply =
        getNotionClient()
            .comments()
            .create(
                CommentCreateParams.builder()
                    .discussionId(onBlock.getDiscussionId())
                    .richText("Reply on the same discussion_id")
                    .displayName(CommentDisplayName.custom("Testing Bot"))
                    .build());

    assertNotNull(reply.getId());
    assertEquals(onBlock.getDiscussionId(), reply.getDiscussionId());
    assertEquals("Reply on the same discussion_id", reply.getRichText().get(0).getPlainText());

    CommentList listed = getNotionClient().comments().listComments(blockId);
    assertNotNull(listed.getResults());
    assertTrue(
        listed.getResults().stream().anyMatch(comment -> onBlock.getId().equals(comment.getId())));
    assertTrue(
        listed.getResults().stream().anyMatch(comment -> reply.getId().equals(comment.getId())));
  }
}
