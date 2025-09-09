package io.kristixlab.notion.api.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsersList extends NotionList<User> {

  @JsonProperty("user")
  private Object user;
}
