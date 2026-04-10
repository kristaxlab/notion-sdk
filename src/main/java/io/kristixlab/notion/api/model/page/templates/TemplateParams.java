package io.kristixlab.notion.api.model.page.templates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateParams {

  private String type; // "none", "default", "template_id"

  /*
   * Required if type is "template_id"
   */
  private String templateId;

  /*
   * IANA timezone to use when resolving template variables like @now and @today
   * Examples: "America/New_York""Europe/London""Asia/Tokyo"
   * Defaults to the authorizing user's timezone for public integrations, or UTC for internal integrations
   */
  private String timezone;

  public static TemplateParams none() {
    TemplateParams params = new TemplateParams();
    params.setType("none");
    return params;
  }

  public static TemplateParams defaultTemplate() {
    TemplateParams params = new TemplateParams();
    params.setType("default");
    return params;
  }

  public static TemplateParams templateId(String templateId) {
    TemplateParams params = new TemplateParams();
    params.setType("template_id");
    params.setTemplateId(templateId);
    return params;
  }
}
