package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.BlockReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database attachment in a comment. */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseCommentAttachment extends CommentAttachment {

  @JsonProperty("database")
  private BlockReference database;
}
