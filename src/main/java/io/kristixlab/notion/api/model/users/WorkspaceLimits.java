package io.kristixlab.notion.api.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WorkspaceLimits {

  @JsonProperty("max_file_upload_size_in_bytes")
  private Long maxFileUploadSizeInBytes;
}