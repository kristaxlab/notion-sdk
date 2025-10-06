package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.model.users.UserList;

/**
 * Interface defining operations for Notion Users.
 *
 * @see <a href="https://developers.notion.com/reference/users">Notion Users API</a>
 */
public interface UsersEndpoint {
  UserList listUsers(String startCursor, Integer pageSize);

  UserList listUsers();

  User retrieve(String userId);

  User me();
}
