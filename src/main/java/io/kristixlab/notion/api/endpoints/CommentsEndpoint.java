package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentList;
import io.kristixlab.notion.api.model.comments.CreateCommentParams;

/**
 * Interface defining operations for Notion Comments.
 *
 * @see <a href="https://developers.notion.com/reference/comments">Notion Comments API</a>
 */
public interface CommentsEndpoint {
  Comment create(CreateCommentParams request);

  Comment retrieve(String commentId);

  CommentList listComments(String blockId);

  CommentList listComments(String blockId, String startCursor, Integer pageSize);
}
