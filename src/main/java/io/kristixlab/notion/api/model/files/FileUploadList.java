package io.kristixlab.notion.api.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Response object for listing file uploads. */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUploadList extends NotionListType<FileUpload> {

  @JsonProperty("file_upload")
  private Object fileUpload;
}
