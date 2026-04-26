package io.kristaxlab.notion.model.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristaxlab.notion.model.BaseNotionObject;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.user.User;
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
