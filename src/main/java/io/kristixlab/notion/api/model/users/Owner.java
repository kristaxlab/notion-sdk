package io.kristixlab.notion.api.model.users;

import lombok.Data;

@Data
public class Owner {

  private String type;

  private Boolean workspace;

  private User user;
}
