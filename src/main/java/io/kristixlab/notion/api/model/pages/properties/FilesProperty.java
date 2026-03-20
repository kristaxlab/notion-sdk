package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FilesProperty extends PageProperty {
  private final String type = PagePropertyType.FILES.type();

  @JsonProperty("files")
  private List<FileData> files;

  public static FilesProperty of(List<FileData> files) {
    FilesProperty property = new FilesProperty();
    property.setFiles(files);
    return property;
  }

  /** Creates a property from one or more {@link FileData} entries (varargs overload). */
  public static FilesProperty of(FileData... files) {
    return of(Arrays.asList(files));
  }
}
