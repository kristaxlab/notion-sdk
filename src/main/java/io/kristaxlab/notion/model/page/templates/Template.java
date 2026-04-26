package io.kristaxlab.notion.model.page.templates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Template {

  /* UUIDv4 */
  private String id;

  private String name;

  private boolean isDefault;
}
