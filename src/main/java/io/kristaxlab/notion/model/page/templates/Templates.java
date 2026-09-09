package io.kristaxlab.notion.model.page.templates;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristaxlab.notion.model.common.NotionList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Templates extends NotionList<Template> {

  // TODO overrides results field
  @JsonProperty("templates")
  private List<Template> results;
}
