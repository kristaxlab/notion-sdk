package io.kristaxlab.notion.model.page.templates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Template {

  /* UUIDv4 */
  private String id;

  private String name;

  @JsonProperty("is_default")
  private boolean isDefault;
}
