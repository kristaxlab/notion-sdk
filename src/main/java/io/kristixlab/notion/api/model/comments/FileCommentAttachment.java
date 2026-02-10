package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.File;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** File attachment in a comment. */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileCommentAttachment extends CommentAttachment {

  @JsonProperty("file")
  private File file;
}
