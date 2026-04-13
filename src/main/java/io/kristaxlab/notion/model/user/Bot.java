package io.kristaxlab.notion.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bot {

  private Owner owner;

  private String workspaceName;

  private String workspaceId;

  private WorkspaceLimits workspaceLimits;
}
