package io.kristixlab.notion.api.model.users;

import lombok.Data;

@Data
public class Bot {

  private Owner owner;

  private String workspaceName;

  private String workspaceId;

  private WorkspaceLimits workspaceLimits;
}
