package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CreatePageParams {

  @JsonProperty("template")
  private TemplateParams template;

  @JsonProperty("properties")
  private Map<String, PageProperty> properties = new HashMap<>();

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;

  @JsonProperty("parent")
  private Parent parent;
}
