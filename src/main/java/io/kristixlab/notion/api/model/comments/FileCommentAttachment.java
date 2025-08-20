package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * File attachment in a comment.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileCommentAttachment extends CommentAttachment {

  @JsonProperty("file")
  private FileData.File file;

}
