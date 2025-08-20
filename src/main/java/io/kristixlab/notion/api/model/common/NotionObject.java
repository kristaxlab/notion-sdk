package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotionObject extends BaseNotionResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("created_time")
  private String createdTime;

  @JsonProperty("last_edited_time")
  private String lastEditedTime;

  @JsonProperty("created_by")
  private User createdBy;

  @JsonProperty("last_edited_by")
  private User lastEditedBy;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("archived")
  private Boolean archived;

  @JsonProperty("in_trash")
  private Boolean inTrash;
}