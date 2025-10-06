package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for comments retrieve operations. Contains the comments that match the query
 * criteria with pagination support.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentList extends NotionListType<Comment> {

  @JsonProperty("comment")
  private Object commentMetadata;
}
