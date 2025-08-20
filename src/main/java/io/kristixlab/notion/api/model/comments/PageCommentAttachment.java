package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.PageReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Page attachment in a comment.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageCommentAttachment extends CommentAttachment {

  @JsonProperty("page")
  private PageReference page;

}
