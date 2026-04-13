package io.kristaxlab.notion.model.file;

import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

/** Response object for listing file uploads. */
@Getter
@Setter
public class FileUploadList extends NotionList<FileUpload> {

  private Object fileUpload;
}
