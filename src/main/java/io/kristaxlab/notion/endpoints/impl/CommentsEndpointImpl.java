package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;
import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.CommentsEndpoint;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.comment.Comment;
import io.kristaxlab.notion.model.comment.CommentCreateParams;
import io.kristaxlab.notion.model.comment.CommentList;
import io.kristaxlab.notion.model.comment.CommentUpdateParams;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;

/** Provides access to Notion comment resources. */
public class CommentsEndpointImpl extends BaseEndpointImpl implements CommentsEndpoint {

  /**
   * Creates a comments endpoint backed by the provided API client.
   *
   * @param client client used to execute comment API requests
   */
  public CommentsEndpointImpl(ApiClient client) {
    super(client);
  }

  /**
   * Creates a comment on a page, a block, or an existing {@code discussion_id}.
   *
   * @param request comment create payload
   * @return created comment
   * @throws IllegalArgumentException if {@code request} is {@code null}, the target is missing or
   *     ambiguous, or the body is missing or specifies both {@code rich_text} and {@code markdown}
   */
  @Override
  public Comment create(CommentCreateParams request) {
    checkNotNull(request, "request");
    validateCreateTarget(request);
    validateCommentBody(request.getRichText(), request.getMarkdown());
    return getClient().call(POST, ApiPath.from("/comments"), request, Comment.class);
  }

  /**
   * Loads a comment by identifier.
   *
   * @param commentId comment identifier
   * @return the matching comment
   * @throws IllegalArgumentException if {@code commentId} is {@code null}, empty, or blank
   */
  @Override
  public Comment retrieve(String commentId) {
    checkNotNullOrEmpty(commentId, "commentId");
    return getClient().call(GET, commentPath(commentId), Comment.class);
  }

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
  @Override
  public Comment update(String commentId, CommentUpdateParams request) {
    checkNotNullOrEmpty(commentId, "commentId");
    checkNotNull(request, "request");
    validateCommentBody(request.getRichText(), request.getMarkdown());
    return getClient().call(PATCH, commentPath(commentId), request, Comment.class);
  }

  /**
   * Deletes a comment created by the current integration.
   *
   * @param commentId comment identifier
   * @return deleted comment
   * @throws IllegalArgumentException if {@code commentId} is {@code null}, empty, or blank
   */
  @Override
  public Comment delete(String commentId) {
    checkNotNullOrEmpty(commentId, "commentId");
    return getClient().call(DELETE, commentPath(commentId), Comment.class);
  }

  /**
   * Lists unresolved comments on a page or block using Notion defaults.
   *
   * @param blockId page or block identifier sent as {@code block_id}
   * @return paginated comment list
   * @throws IllegalArgumentException if {@code blockId} is {@code null}, empty, or blank
   */
  @Override
  public CommentList listComments(String blockId) {
    return listComments(blockId, null, null);
  }

  /**
   * Lists unresolved comments on a page or block with start cursor and page size.
   *
   * @param blockId page or block identifier sent as {@code block_id}
   * @param startCursor start cursor; {@code null} starts from the first page
   * @param pageSize page size; {@code null} uses the Notion default
   * @return paginated comment list
   * @throws IllegalArgumentException if {@code blockId} is {@code null}, empty, or blank
   */
  @Override
  public CommentList listComments(String blockId, String startCursor, Integer pageSize) {
    checkNotNullOrEmpty(blockId, "blockId");
    ApiPath.Builder path = paginatedPath("/comments", startCursor, pageSize);
    path.queryParam("block_id", blockId);
    return getClient().call(GET, path.build(), CommentList.class);
  }

  private static ApiPath commentPath(String commentId) {
    return ApiPath.builder("/comments/{comment_id}").pathParam("comment_id", commentId).build();
  }

  private static void validateCreateTarget(CommentCreateParams request) {
    boolean discussion = request.getDiscussionId() != null && !request.getDiscussionId().isBlank();
    Parent parent = request.getParent();
    boolean page = parent != null && parent.getPageId() != null && !parent.getPageId().isBlank();
    boolean block = parent != null && parent.getBlockId() != null && !parent.getBlockId().isBlank();
    int targets = (discussion ? 1 : 0) + (page ? 1 : 0) + (block ? 1 : 0);
    if (targets != 1) {
      throw new IllegalArgumentException(
          "Exactly one of parent.pageId, parent.blockId, or discussionId must be provided");
    }
  }

  private static void validateCommentBody(List<RichText> richText, String markdown) {
    boolean hasRichText = richText != null && !richText.isEmpty();
    boolean hasMarkdown = markdown != null && !markdown.isBlank();
    if (hasRichText == hasMarkdown) {
      throw new IllegalArgumentException("Exactly one of richText or markdown must be provided");
    }
  }
}
