package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.BaseNotionObject;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.users.User;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = NotionObject.class,
    property = "object",
    visible = true)
@JsonSubTypes({
  // @JsonSubTypes.Type(value = Page.class, name = "page"),
  // @JsonSubTypes.Type(value = Database.class, name = "database"),
  // @JsonSubTypes.Type(value = DataSource.class, name = "data_source"),
  // @JsonSubTypes.Type(value = Comment.class, name = "comment"),
  @JsonSubTypes.Type(value = Block.class, name = "block")
})
@Getter
@Setter
public class NotionObject extends BaseNotionObject {

  private String id;

  private Parent parent;

  private String createdTime;

  private String lastEditedTime;

  /* TODO missing for Databases */
  private User createdBy;

  /* TODO missing for Comments, Databases */
  private User lastEditedBy;

  /* TODO missing for Comments */
  private Boolean inTrash;
}
