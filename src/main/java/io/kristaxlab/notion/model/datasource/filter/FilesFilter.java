package io.kristaxlab.notion.model.datasource.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilesFilter extends Filter {

  private FilesFilterCondition files;

  public static class FilesFilterCondition {

    private Boolean isEmpty;

    private Boolean isNotEmpty;
  }
}
