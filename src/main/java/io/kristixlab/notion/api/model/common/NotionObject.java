package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.BaseNotionObject;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.file.FileUpload;
import io.kristixlab.notion.api.model.page.Page;
import io.kristixlab.notion.api.model.users.User;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = NotionObject.class,
    property = "object",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Page.class, name = "page"),
  @JsonSubTypes.Type(value = FileUpload.class, name = "file_upload"),
  @JsonSubTypes.Type(value = Block.class, name = "block")
})
@Getter
@Setter
public class NotionObject extends BaseNotionObject {

  private String id;

  /** Always null for File Uploads */
  private Parent parent;

  private String createdTime;

  private String lastEditedTime;

  private User createdBy;

  /** Always null for Comments, Databases, File Uploads */
  private User lastEditedBy;

  /** Always null for Comments */
  private Boolean inTrash;

  @Deprecated private Boolean isArchived;
}
