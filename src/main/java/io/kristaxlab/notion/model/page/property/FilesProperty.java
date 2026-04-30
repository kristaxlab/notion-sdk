package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.FileData;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilesProperty extends PageProperty {
  private final String type = PagePropertyType.FILES.type();

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
