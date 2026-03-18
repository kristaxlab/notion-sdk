package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.CommentsEndpoint;
import io.kristixlab.notion.api.http.transport.HttpTransport;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentList;
import io.kristixlab.notion.api.model.comments.CreateCommentRequest;

/**
 * API for interacting with Notion Comments endpoints. Provides methods to create and retrieve
 * comments.
 */
public class CommentsEndpointImpl implements CommentsEndpoint {

  private static final String BLOCK_ID = "block_id";
  private static final String COMMENT_ID = "comment_id";

  private final HttpTransport transport;

  public CommentsEndpointImpl(HttpTransport transport) {
    this.transport = transport;
  }

  /**
   * Create a new comment.
   *
   * @param request The comment to create
   * @return The created comment
   */
  public Comment create(CreateCommentRequest request) {
    validateRequest(request);
    return transport.call("POST", URLInfo.from("/comments"), request, Comment.class);
  }

  /**
   * Retrieve a comment by its ID.
   *
   * @param commentId The ID of the comment to retrieve
   * @return The comment with the specified ID
   */
  public Comment retrieve(String commentId) {
    validateCommentId(commentId);
    URLInfo urlInfo =
        URLInfo.builder("/comments/{comment_id}").pathParam(COMMENT_ID, commentId).build();
    return transport.call("GET", urlInfo, Comment.class);
  }

  /**
   * Retrieve comments for a block with default parameters.
   *
   * @param blockId The ID of the block to retrieve comments for
   * @return Comments response containing the comments
   */
  public CommentList listComments(String blockId) {
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
  public CommentList listComments(String blockId, String startCursor, Integer pageSize) {
    validateBlockId(blockId);

    URLInfo.Builder urlInfo =
        URLInfo.builder("/comments", startCursor, pageSize)
            .queryParam(BLOCK_ID, blockId);

    return transport.call("GET", urlInfo.build(), CommentList.class);
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
