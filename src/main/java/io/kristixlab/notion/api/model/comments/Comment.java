package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionObject;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Represents a Notion comment object. Comments are attached to pages or discussion threads. */
@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends NotionObject {

  @JsonProperty("discussion_id")
  private String discussionId;

  @JsonProperty("rich_text")
  private List<RichText> richText;

  @JsonProperty("attachments")
  private List<CommentAttachment> attachments;

  @JsonProperty("display_name")
  private CommentDisplayName displayName;
}
