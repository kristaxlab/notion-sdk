package io.kristaxlab.notion.model.datasource.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectOption {
  private String id;

  private String name;

  private String description;

  private String color;
}
