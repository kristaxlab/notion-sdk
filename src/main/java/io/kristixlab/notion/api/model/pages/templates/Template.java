package io.kristixlab.notion.api.model.pages.templates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Template {

  /* TODO ? should I use UUID for such fields? why yes, why not, what are +/-?
  /* UUIDv4 */
  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("is_default")
  private boolean isDefault;
}
