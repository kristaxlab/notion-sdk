package io.kristixlab.notion.api.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Bot {

  @JsonProperty("owner")
  private Owner owner;

  @JsonProperty("workspace_name")
  private String workspaceName;

  @JsonProperty("workspace_limits")
  private WorkspaceLimits workspaceLimits;
}
