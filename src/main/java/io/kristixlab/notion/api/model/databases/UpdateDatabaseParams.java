package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import java.util.List;
import lombok.Data;

@Data
public class UpdateDatabaseParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("in_trash")
  private Boolean inTrash;

  @JsonProperty("is_locked")
  private Boolean isLocked;
}
