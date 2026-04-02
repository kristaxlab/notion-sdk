package io.kristixlab.notion.api.model.users;

import io.kristixlab.notion.api.model.common.NotionListType;
import lombok.Data;

@Data
public class UserList extends NotionListType<User> {

  private Object user;
}
