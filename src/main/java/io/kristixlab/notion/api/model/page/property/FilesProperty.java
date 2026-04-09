package io.kristixlab.notion.api.model.page.property;

import io.kristixlab.notion.api.model.common.FileData;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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

  public static FilesProperty of(Consumer<FileData.Builder> builder) {
    FileData.Builder fileBuilder = FileData.builder();
    builder.accept(fileBuilder);
    return of(fileBuilder.build());
  }
}
