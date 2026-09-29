package io.kristaxlab.notion.model.comment;

import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

/** Paginated list of comments returned by the comment list endpoint. */
@Getter
@Setter
public class CommentList extends NotionList<Comment> {

  /** Empty object present on Notion list responses whose type is {@code comment}. */
  private Object comment;
}
