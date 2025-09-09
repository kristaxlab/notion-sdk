package io.kristixlab.notion.api.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomDisplayName {

  @JsonProperty("name")
  private String name;
}
