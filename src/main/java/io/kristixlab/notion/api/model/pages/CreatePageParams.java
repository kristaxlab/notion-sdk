package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CreatePageParams {

  @JsonProperty("template")
  private TemplateParams template;

  @JsonProperty("properties")
  private Map<String, PageProperty> properties = new HashMap<>();

  // TODO
  @JsonProperty("content")
  private List<Block> content = new ArrayList<>();

  // TODO
  @JsonProperty("children")
  private List<Block> children = new ArrayList<>();

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("parent")
  private Parent parent;
}
