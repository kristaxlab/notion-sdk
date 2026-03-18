package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.CoverParams;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import java.util.Map;
import lombok.Data;

@Data
public class UpdatePageParams {

  @JsonProperty("id")
  private String id;

  @JsonProperty("properties")
  private Map<String, PageProperty> properties;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("in_trash")
  private Boolean inTrash;

  @JsonProperty("is_locked")
  private Boolean isLocked;

  @JsonProperty("template")
  private TemplateParams template;

  @JsonProperty("erase_content")
  private Boolean eraseContent;
}
