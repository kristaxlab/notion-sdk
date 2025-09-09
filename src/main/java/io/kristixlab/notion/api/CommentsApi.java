package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.ApiRequestUtil;
import io.kristixlab.notion.api.exchange.ApiTransport;
import io.kristixlab.notion.api.exchange.NotionApiTransport;
import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentsList;
import java.util.Map;

/**
 * API for interacting with Notion Comments endpoints. Provides methods to create and retrieve
 * comments.
 */
public class CommentsApi {

  private static final String BLOCK_ID = "block_id";
  private static final String COMMENT_ID = "comment_id";

  private final ApiTransport transport;

  public CommentsApi(NotionApiTransport transport) {
    this.transport = transport;
  }

  /**
   * Create a new comment.
   *
   * @param request The comment to create
   * @return The created comment
   */
  public Comment create(Comment request) {
    validateRequest(request);

    return transport.call("POST", "/comments", null, null, request, Comment.class);
  }

  /**
   * Retrieve a comment by its ID.
   *
   * @param commentId The ID of the comment to retrieve
   * @return The comment with the specified ID
   */
  public Comment retrieve(String commentId) {
    validateCommentId(commentId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(COMMENT_ID, commentId);

    return transport.call("GET", "/comments/{comment_id}", null, pathParams, null, Comment.class);
  }

  /**
   * Retrieve comments for a block with default parameters.
   *
   * @param blockId The ID of the block to retrieve comments for
   * @return Comments response containing the comments
   */
  public CommentsList listComments(String blockId) {
    return listComments(blockId, null, null);
  }

  /**
   * Retrieve comments for a block with pagination.
   *
   * @param blockId The ID of the block to retrieve comments for
   * @param startCursor The cursor to start from for pagination
   * @param pageSize The number of results to return (max 100)
   * @return Comments response containing the comments
   */
  public CommentsList listComments(String blockId, String startCursor, Integer pageSize) {
    validateBlockId(blockId);

    Map<String, String[]> queryParams = ApiRequestUtil.createQueryParams(startCursor, pageSize);
    queryParams.put(BLOCK_ID, new String[] {blockId});

    return transport.call("GET", "/comments", queryParams, null, null, CommentsList.class);
  }

  /** Validates the block ID parameter. */
  private void validateCommentId(String commentId) {
    if (commentId == null || commentId.trim().isEmpty()) {
      throw new IllegalArgumentException("Comment ID cannot be null or empty");
    }
  }

  /** Validates the block ID parameter. */
  private void validateBlockId(String blockId) {
    if (blockId == null || blockId.trim().isEmpty()) {
      throw new IllegalArgumentException("Block ID cannot be null or empty");
    }
  }

  /** Validates the request object. */
  private void validateRequest(Object request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }
}
