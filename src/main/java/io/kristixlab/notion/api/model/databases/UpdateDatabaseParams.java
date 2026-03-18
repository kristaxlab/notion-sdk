package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.common.RichText;
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
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;

  @JsonProperty("in_trash")
  private Boolean inTrash;

  @JsonProperty("in_locked")
  private Boolean isLocked;
}
