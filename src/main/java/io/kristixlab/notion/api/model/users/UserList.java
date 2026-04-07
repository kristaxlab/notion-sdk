package io.kristixlab.notion.api.model.users;

import io.kristixlab.notion.api.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserList extends NotionList<User> {

  private Object user;
}
