package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import java.util.List;
import lombok.Data;

/** Request object for creating a database with initial data source. */
@Data
public class CreateDatabaseParams {

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("in_trash")
  private Boolean inTrash;

  @JsonProperty("initial_data_source")
  private InitialDatasource initialDataSource;
}
