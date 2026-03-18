package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import java.util.List;
import java.util.Map;
import lombok.Data;

// TODO major task - consider api check and update with published notion doc (with or without AI)
@Data
public class CreatePageParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("properties")
  private Map<String, PageProperty> properties;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  // TODO
  @JsonProperty("content")
  private List<Block> content;

  // TODO
  @JsonProperty("children")
  private List<Block> children;

  @JsonProperty("markdown")
  private String markdown;

  @JsonProperty("template")
  private TemplateParams template;

  @JsonProperty("position")
  private Position position;
}
