package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FilesProperty extends PageProperty {
  private final String type = "files";

  @JsonProperty("files")
  private List<FileData> files;

  public static FilesProperty of(List<FileData> files) {
    FilesProperty property = new FilesProperty();
    property.setFiles(files);
    return property;
  }
}
