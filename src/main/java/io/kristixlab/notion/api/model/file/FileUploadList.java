package io.kristixlab.notion.api.model.file;

import io.kristixlab.notion.api.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

/** Response object for listing file uploads. */
@Getter
@Setter
public class FileUploadList extends NotionList<FileUpload> {

  private Object fileUpload;
}
