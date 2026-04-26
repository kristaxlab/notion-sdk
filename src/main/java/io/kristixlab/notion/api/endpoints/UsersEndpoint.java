package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.user.User;
import io.kristixlab.notion.api.model.user.UserList;

/**
 * Defines operations for reading users from the Notion API.
 *
 * @see <a href="https://developers.notion.com/reference/users">Notion Users API</a>
 */
public interface UsersEndpoint {

  /**
   * Retrieves users with optional pagination parameters.
   *
   * @param startCursor cursor for the next page; {@code null} starts from the first page
   * @param pageSize number of results to request per page
   * @return a paginated list of users
   */
  UserList listUsers(String startCursor, Integer pageSize);

  /**
   * Retrieves the first page of users using Notion defaults.
   *
   * @return a paginated list of users
   */
  UserList listUsers();

  /**
   * Retrieves a user by identifier.
   *
   * @param userId the Notion user identifier
   * @return the matching user
   * @throws IllegalArgumentException if {@code userId} is {@code null}, empty, or blank
   */
  User retrieve(String userId);

  /**
   * Retrieves the bot user associated with the current authentication token.
   *
   * @return the current bot user
   */
  User me();
}
