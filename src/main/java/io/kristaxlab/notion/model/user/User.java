package io.kristaxlab.notion.model.user;

import io.kristaxlab.notion.model.BaseNotionObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseNotionObject {

  private String id;

  private String name;

  private String avatarUrl;

  private String type;

  private Person person;

  private Bot bot;
}
