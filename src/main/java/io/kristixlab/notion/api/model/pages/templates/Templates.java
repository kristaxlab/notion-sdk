package io.kristixlab.notion.api.model.pages.templates;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;
import java.util.List;
import lombok.Data;

@Data
public class Templates extends NotionListType<Template> {

  // TODO overrides results field
  @JsonProperty("templates")
  private List<Template> results;
}
