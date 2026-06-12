package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlProperty extends PageProperty {
  private final String type = PropertyType.URL.type();

  private String url;
}
