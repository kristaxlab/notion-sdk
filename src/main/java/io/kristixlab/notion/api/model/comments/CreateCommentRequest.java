package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;

/** Represents a Notion comment object. Comments are attached to pages or discussion threads. */
@Data
public class CreateCommentRequest {

  /* a page parent or block parent */
  @JsonProperty("parent")
  private String parent;

  /* either parent or a discussion id should be presented */
  @JsonProperty("discussion_id")
  private String discussionId;

  @JsonProperty("rich_text")
  private List<RichText> richText;

  @JsonProperty("attachments")
  private List<CommentAttachment> attachments;

  @JsonProperty("display_name")
  private CommentDisplayName displayName;

  public static class CommentDisplayName {

    /* example: "integration" */
    @JsonProperty("type")
    private String type;
  }
}
