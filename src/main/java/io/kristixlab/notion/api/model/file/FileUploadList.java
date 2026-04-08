package io.kristixlab.notion.api.model.file;

import io.kristixlab.notion.api.model.common.NotionListType;
import lombok.Getter;
import lombok.Setter;

/** Response object for listing file uploads. */
@Getter
@Setter
public class FileUploadList extends NotionListType<FileUpload> {

  private Object fileUpload;
}
