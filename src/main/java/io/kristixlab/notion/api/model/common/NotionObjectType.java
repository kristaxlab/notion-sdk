package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.BaseNotionObject;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.databases.Database;
import io.kristixlab.notion.api.model.datasources.DataSource;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = NotionObjectType.class,
    property = "object",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Page.class, name = "page"),
  @JsonSubTypes.Type(value = Database.class, name = "database"),
  @JsonSubTypes.Type(value = DataSource.class, name = "data_source"),
  @JsonSubTypes.Type(value = Comment.class, name = "comment"),
  @JsonSubTypes.Type(value = Block.class, name = "block")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class NotionObjectType extends BaseNotionObject {

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

  @JsonProperty("in_trash")
  private Boolean inTrash;
}
