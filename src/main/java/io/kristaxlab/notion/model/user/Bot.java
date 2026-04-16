package io.kristaxlab.notion.model.user;

import lombok.Getter;
import lombok.Setter;

/** Metadata describing a bot user and its owning workspace context. */
@Getter
@Setter
public class Bot {

  private Owner owner;

  private String workspaceName;

  private String workspaceId;

  private WorkspaceLimits workspaceLimits;
}
