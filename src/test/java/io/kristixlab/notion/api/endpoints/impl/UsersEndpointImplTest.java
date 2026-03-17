package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.TransportStub;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UsersEndpointImplTest {

  private TransportStub transport;
  private UsersEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    transport = new TransportStub();
    endpoint = new UsersEndpointImpl(transport);
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("user-id-42");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/users/{user_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("user-id-42", transport.getLastUrlInfo().getPathParams().get("user_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieve_rejectsBlankOrNullUserId(String userId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(userId));
  }

  @Test
  void listUsers() {
    endpoint.listUsers();

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/users", transport.getLastUrlInfo().getUrl());
    assertTrue(transport.getLastUrlInfo().getQueryParams().isEmpty());
  }

  @Test
  void listUsers_withStartCursor() {

    endpoint.listUsers("cursor-xyz", null);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/users", transport.getLastUrlInfo().getUrl());
    assertEquals(
        List.of("cursor-xyz"), transport.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void listUsers_withPageSize() {
    endpoint.listUsers(null, 50);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/users", transport.getLastUrlInfo().getUrl());
    assertEquals(List.of("50"), transport.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void listUsers_withBothPaginationParams() {
    endpoint.listUsers("cursor-abc", 25);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/users", transport.getLastUrlInfo().getUrl());
    assertEquals(
        List.of("cursor-abc"), transport.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), transport.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @Test
  void me() {
    endpoint.me();

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/users/me", transport.getLastUrlInfo().getUrl());
  }
}
