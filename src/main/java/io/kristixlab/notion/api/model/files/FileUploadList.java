package io.kristixlab.notion.api.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionList;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for listing file uploads.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUploadList extends NotionList<FileUploadResponse> {

  @JsonProperty("file_upload")
  private Object fileUpload;
}
