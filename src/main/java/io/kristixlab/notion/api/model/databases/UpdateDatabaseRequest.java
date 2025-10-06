package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;

/** Request object for creating a database with initial data source (API version 2025-09-03+). */
@Data
public class UpdateDatabaseRequest {

  @JsonProperty("id")
  private String id;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("in_trash")
  private Boolean inTrash;
}
