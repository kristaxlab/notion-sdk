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

  /*
   * IANA timezone to use when resolving template variables like @now and @today
   * Examples: "America/New_York""Europe/London""Asia/Tokyo"
   * Defaults to the authorizing user's timezone for public integrations, or UTC for internal integrations
   */
  @JsonProperty("timezone")
  private String timezone;
}
