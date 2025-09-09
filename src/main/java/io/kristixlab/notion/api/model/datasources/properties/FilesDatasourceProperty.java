package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for files columns. Allows uploading and storing files. */
@Data
@EqualsAndHashCode(callSuper = true)
public class FilesDatasourceProperty extends DatasourceProperty {

  @JsonProperty("files")
  private Object files = new Object();
}
