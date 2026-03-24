package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.UsersEndpoint;
import io.kristixlab.notion.api.http.client.ApiClient;
import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.model.users.UserList;

/**
 * API for interacting with Notion Users endpoints. Provides methods to retrieve users and list all
 * users in the workspace.
 */
public class UsersEndpointImpl implements UsersEndpoint {

  private static final String USER_ID = "user_id";

  private final ApiClient client;

  public UsersEndpointImpl(ApiClient client) {
    this.client = client;
  }

  /**
   * Retrieve a user by their ID. Returns a User object for the user with the specified ID.
   *
   * @param userId The ID of the user to retrieve
   * @return The user object
   */
  public User retrieve(String userId) {
    validateUserId(userId);
    ApiPath urlInfo = ApiPath.builder("/users/{user_id}").pathParam(USER_ID, userId).build();
    return client.call("GET", urlInfo, User.class);
  }

  /**
   * List all users in the workspace with default parameters. Returns a paginated list of User
   * objects for all users in the workspace.
   *
   * @return UsersList containing all users in the workspace
   */
  public UserList listUsers() {
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
  public UserList listUsers(String startCursor, Integer pageSize) {
    ApiPath.Builder urlInfo = ApiPath.builder("/users", startCursor, pageSize);
    return client.call("GET", urlInfo.build(), UserList.class);
  }

  /**
   * Retrieve the current bot user. Returns information about the current bot user.
   *
   * @return The current bot user
   */
  public User me() {
    return client.call("GET", ApiPath.from("/users/me"), User.class);
  }

  /** Validates the user ID parameter. */
  private void validateUserId(String userId) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException("User ID cannot be null or empty");
    }
  }
}
