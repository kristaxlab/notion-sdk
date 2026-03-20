package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for files columns. Allows uploading and storing files. */
@Data
@EqualsAndHashCode(callSuper = true)
public class FilesSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("files")
  private Object files = new Object();
}
