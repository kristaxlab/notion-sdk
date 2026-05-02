package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.FileData;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilesProperty extends PageProperty {
  private final String type = PagePropertyType.FILES.type();

  private List<FileData> files;
}
