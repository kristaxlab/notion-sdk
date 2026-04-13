package io.kristaxlab.notion.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.http.base.client.ApiClientStub;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Users endpoint behaviors")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsersEndpointImplTest {

  private ApiClientStub client;
  private UsersEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new UsersEndpointImpl(client);
  }

  @Test
  @DisplayName("works for valid user id")
  void retrieveById() {
    endpoint.retrieve("user-id-42");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/users/{user_id}", client.getLastUrlInfo().getUrl());
    assertEquals("user-id-42", client.getLastUrlInfo().getPathParams().get("user_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null user id")
  void retrieve_rejectsBlankOrNullUserId(String userId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(userId));
  }

  @Test
  @DisplayName("works for default list users request")
  void listUsers() {
    endpoint.listUsers();

    assertEquals("GET", client.getLastMethod());
    assertEquals("/users", client.getLastUrlInfo().getUrl());
    assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
  }

  @Test
  @DisplayName("works with start cursor and no page size")
  void listUsers_withStartCursor() {
    endpoint.listUsers("cursor-xyz", null);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/users", client.getLastUrlInfo().getUrl());
    assertEquals(
        List.of("cursor-xyz"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  @DisplayName("works with page size and no start cursor")
  void listUsers_withPageSize() {
    endpoint.listUsers(null, 50);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/users", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("50"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  @DisplayName("works with both pagination params")
  void listUsers_withBothPaginationParams() {
    endpoint.listUsers("cursor-abc", 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/users", client.getLastUrlInfo().getUrl());
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @Test
  @DisplayName("works for current bot user request")
  void me() {
    endpoint.me();

    assertEquals("GET", client.getLastMethod());
    assertEquals("/users/me", client.getLastUrlInfo().getUrl());
  }
}
