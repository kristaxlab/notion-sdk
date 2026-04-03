package io.kristixlab.notion.api.model.users;

import io.kristixlab.notion.api.model.common.NotionListType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserList extends NotionListType<User> {

  private Object user;
}
