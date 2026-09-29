package io.kristaxlab.notion.model.datasource.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusGroup {

  private String id;

  private String name;

  private String color;

  private List<String> optionIds;
}
