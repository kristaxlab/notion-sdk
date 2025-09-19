package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import java.util.Map;
import lombok.Data;

@Data
public class UpdatePageRequest {

  @JsonProperty("id")
  private String id;

  @JsonProperty("properties")
  private Map<String, PageProperty> properties;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("in_trash")
  private Boolean inTrash;
}
