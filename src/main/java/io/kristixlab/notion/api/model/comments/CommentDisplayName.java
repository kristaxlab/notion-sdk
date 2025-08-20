package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Display name configuration for comments. Used to specify how the commenter's name should be
 * displayed.
 */
@Data
public class CommentDisplayName {

  @JsonProperty("type")
  private String type; // "person" or "bot"

  @JsonProperty("resolved_name")
  private String resolvedName;

  @JsonProperty("custom")
  private CustomDisplayName custom;
}
