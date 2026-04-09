package io.kristixlab.notion.api.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Owner {

  private String type;

  private Boolean workspace;

  private User user;
}
