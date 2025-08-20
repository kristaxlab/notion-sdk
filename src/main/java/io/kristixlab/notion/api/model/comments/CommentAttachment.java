package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import lombok.Data;

@Data
public class CommentAttachment {

  @JsonProperty("category")
  private String category;

  @JsonProperty("file")
  private FileData.File file;
}
