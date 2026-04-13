package io.kristaxlab.notion.model.user;

import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserList extends NotionList<User> {

  private Object user;
}
