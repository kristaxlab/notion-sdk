package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.ApiRequestUtil;
import io.kristixlab.notion.api.exchange.transport.ApiTransport;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.model.users.UsersList;
import java.util.Map;

/**
 * API for interacting with Notion Users endpoints. Provides methods to retrieve users and list all
 * users in the workspace.
 */
public class UsersApi {

  private static final String USER_ID = "user_id";

  private final ApiTransport transport;

  public UsersApi(NotionApiTransport transport) {
    this.transport = transport;
  }

  /**
   * Retrieve a user by their ID. Returns a User object for the user with the specified ID.
   *
   * @param userId The ID of the user to retrieve
   * @return The user object
   */
  public User retrieve(String userId) {
    validateUserId(userId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(USER_ID, userId);

    return transport.call("GET", "/users/{user_id}", null, pathParams, null, User.class);
  }

  /**
   * List all users in the workspace with default parameters. Returns a paginated list of User
   * objects for all users in the workspace.
   *
   * @return UsersList containing all users in the workspace
   */
  public UsersList listUsers() {
    return listUsers(null, null);
  }

  /**
   * List all users in the workspace with pagination. Returns a paginated list of User objects for
   * all users in the workspace.
   *
   * @param startCursor The cursor to start from for pagination
   * @param pageSize The number of results to return (max 100)
   * @return UsersList containing users in the workspace
   */
  public UsersList listUsers(String startCursor, Integer pageSize) {
    Map<String, String[]> queryParams = ApiRequestUtil.createQueryParams(startCursor, pageSize);

    return transport.call("GET", "/users", queryParams, null, null, UsersList.class);
  }

  /**
   * Retrieve the current bot user. Returns information about the current bot user.
   *
   * @return The current bot user
   */
  public User me() {
    return transport.call("GET", "/users/me", null, null, null, User.class);
  }

  /** Validates the user ID parameter. */
  private void validateUserId(String userId) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException("User ID cannot be null or empty");
    }
  }
}
