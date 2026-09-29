package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.comment.Comment;
import io.kristaxlab.notion.model.comment.CommentCreateParams;
import io.kristaxlab.notion.model.comment.CommentList;
import io.kristaxlab.notion.model.comment.CommentUpdateParams;

/**
 * Notion Comments API: create, retrieve, update, delete, and list unresolved comments.
 *
 * @see <a href="https://developers.notion.com/reference/comment-object">Comment object</a>
 */
public interface CommentsEndpoint {

  /**
   * Creates a comment on a page, a block, or an existing {@code discussion_id}.
   *
   * @param request comment create payload
   * @return created comment
   * @throws IllegalArgumentException if {@code request} is {@code null}, the target is missing or
   *     ambiguous, or the body is missing or specifies both {@code rich_text} and {@code markdown}
   */
  Comment create(CommentCreateParams request);

  /**
   * Loads a comment by identifier.
   *
   * @param commentId comment identifier
   * @return the matching comment
   * @throws IllegalArgumentException if {@code commentId} is {@code null}, empty, or blank
   */
  Comment retrieve(String commentId);

  /**
   * Updates a comment created by the current integration.
   *
   * @param commentId comment identifier
   * @param request comment update payload
   * @return updated comment
   * @throws IllegalArgumentException if {@code commentId} is {@code null}, empty, or blank, if
   *     {@code request} is {@code null}, or if the body is missing or specifies both {@code
   *     rich_text} and {@code markdown}
   */
  Comment update(String commentId, CommentUpdateParams request);

  /**
   * Deletes a comment created by the current integration.
   *
   * @param commentId comment identifier
   * @return deleted comment
   * @throws IllegalArgumentException if {@code commentId} is {@code null}, empty, or blank
   */
  Comment delete(String commentId);

  /**
   * Lists unresolved comments on a page or block using Notion defaults.
   *
   * @param blockId page or block identifier sent as {@code block_id}
   * @return paginated comment list
   * @throws IllegalArgumentException if {@code blockId} is {@code null}, empty, or blank
   */
  CommentList listComments(String blockId);

  /**
   * Lists unresolved comments on a page or block with start cursor and page size.
   *
   * @param blockId page or block identifier sent as {@code block_id}
   * @param startCursor start cursor; {@code null} starts from the first page
   * @param pageSize page size; {@code null} uses the Notion default
   * @return paginated comment list
   * @throws IllegalArgumentException if {@code blockId} is {@code null}, empty, or blank
   */
  CommentList listComments(String blockId, String startCursor, Integer pageSize);
}
