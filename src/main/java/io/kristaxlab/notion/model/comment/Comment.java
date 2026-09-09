package io.kristaxlab.notion.model.comment;

import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Comment on a Notion page or block.
 *
 * <p>When the integration can insert comments but cannot read them, create, update, and delete
 * responses may contain only {@code object} and {@code id}.
 *
 * @see <a href="https://developers.notion.com/reference/comment-object">Comment object</a>
 */
@Getter
@Setter
public class Comment extends NotionObject {

  /** Identifier sent as {@code discussion_id}. */
  private String discussionId;

  /** Comment body as rich text. */
  private List<RichText> richText;

  /** Files attached to the comment. */
  private List<CommentAttachment> attachments;

  /** Author name shown for the comment. */
  private CommentDisplayName displayName;

  /** Whether the original comment body was deleted. */
  private Boolean originalContentDeleted;
}
