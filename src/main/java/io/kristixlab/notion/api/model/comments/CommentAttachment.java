package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentAttachment {

  @JsonProperty("type")
  private String type;

  @JsonProperty("file_upload_id")
  private String fileUploadId;

  public CommentAttachment() {
    setType("file_upload");
  }
}
