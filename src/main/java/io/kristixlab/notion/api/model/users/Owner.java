package io.kristixlab.notion.api.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Owner {

  @JsonProperty("type")
  private String type;

  @JsonProperty("workspace")
  private Boolean workspace;

  @JsonProperty("user")
  private User user;
}
