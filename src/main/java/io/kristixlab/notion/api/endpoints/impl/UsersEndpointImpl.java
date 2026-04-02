package io.kristixlab.notion.api.endpoints.impl;

import static io.kristixlab.notion.api.endpoints.util.Validator.*;

import io.kristixlab.notion.api.endpoints.UsersEndpoint;
import io.kristixlab.notion.api.http.base.client.ApiClient;
import io.kristixlab.notion.api.http.base.request.ApiPath;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.model.users.UserList;

/** Provides access to Notion user resources for retrieving individual users and user listings. */
public class UsersEndpointImpl extends BaseEndpointImpl implements UsersEndpoint {

  private static final String USER_ID = "user_id";

  /**
   * Creates a users endpoint backed by the provided API client.
   *
   * @param client client used to execute user API requests
   */
  public UsersEndpointImpl(ApiClient client) {
    super(client);
  }

  /**
   * Retrieves a user by identifier.
   *
   * @param userId the Notion user identifier
   * @return the matching user
   * @throws IllegalArgumentException if {@code userId} is {@code null}, empty, or blank
   */
  public User retrieve(String userId) {
    checkNotNullOrEmpty(userId, "userId");

    ApiPath urlInfo = ApiPath.builder("/users/{user_id}").pathParam(USER_ID, userId).build();
    return getClient().call(GET, urlInfo, User.class);
  }

  /**
   * Retrieves the first page of users using Notion defaults.
   *
   * @return a paginated list of users
   */
  public UserList listUsers() {
    return listUsers(null, null);
  }

  /**
   * Retrieves users with optional pagination parameters.
   *
   * @param startCursor cursor for the next page; {@code null} starts from the first page
   * @param pageSize number of results to request per page (maximum 100)
   * @return a paginated list of users
   */
  public UserList listUsers(String startCursor, Integer pageSize) {
    ApiPath.Builder path = paginatedPath("/users", startCursor, pageSize);
    return getClient().call(GET, path.build(), UserList.class);
  }

  /**
   * Retrieves the bot user associated with the current authentication token.
   *
   * @return the current bot user
   */
  public User me() {
    return getClient().call(GET, ApiPath.from("/users/me"), User.class);
  }
}
