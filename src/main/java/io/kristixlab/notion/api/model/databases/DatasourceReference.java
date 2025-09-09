package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Represents a data source reference with ID and name. */
@Data
public class DatasourceReference {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;
}
