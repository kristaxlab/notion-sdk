package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentsList;
import io.kristixlab.notion.api.model.comments.CreateCommentRequest;

/**
 * Interface defining operations for Notion Comments.
 *
 * @see <a href="https://developers.notion.com/reference/comments">Notion Comments API</a>
 */
public interface CommentsEndpoint {
  Comment create(CreateCommentRequest request);

  Comment retrieve(String commentId);

  CommentsList listComments(String blockId);

  CommentsList listComments(String blockId, String startCursor, Integer pageSize);
}
