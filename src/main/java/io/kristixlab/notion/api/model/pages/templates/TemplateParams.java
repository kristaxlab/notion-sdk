package io.kristixlab.notion.api.model.pages.templates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TemplateParams {

  @JsonProperty("type")
  private String type; // "none", "default", "template_id"

  /*
   * Required if type is "template_id"
   */
  @JsonProperty("template_id")
  private String templateId;
}
