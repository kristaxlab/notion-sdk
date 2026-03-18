package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import java.util.List;
import lombok.Data;

@Data
public class CreateDatabaseParams {

  @JsonProperty("parent")
  private Parent parent;

  // TODO try list and single (in the doc looks like there is a single richtext
  @JsonProperty("title")
  private List<RichText> title;

  // TODO try list and single (in the doc looks like there is a single richtext
  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("initial_data_source")
  private InitialDatasource initialDataSource;
}
