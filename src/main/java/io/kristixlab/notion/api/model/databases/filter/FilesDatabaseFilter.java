package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FilesDatabaseFilter extends DatabaseFilter {

  @JsonProperty("files")
  private FilesFilterCondition files;

  public static class FilesFilterCondition {
    @JsonProperty("is_empty")
    private Boolean isEmpty;

    @JsonProperty("is_not_empty")
    private Boolean isNotEmpty;
  }
}
