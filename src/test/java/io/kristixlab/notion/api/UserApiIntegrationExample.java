package io.kristixlab.notion.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.kristixlab.notion.api.model.IntegrationTest;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.model.users.UsersList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserApiIntegrationExample extends IntegrationTest {

  public static final String BOT_ID = "44444444-4444-43b3-8288-444444444444";
  public static final String USER_ID = "22222222-4444-43b3-8288-222222222222";

  public static final String WS_ID = "44444444-4444-43b3-8288-444444444444";

  private static final String TOKEN = "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";

  private static UsersApi usersApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    usersApi = new UsersApi(getTransport());
  }

  @Test
  void testListAllUsers() throws Exception {
    // Retrieve a user by ID
    UsersList users = usersApi.listUsers();
    saveToFile(users, "users-list-rs.json");

    // Validate the retrieved user
    assertNotNull(users);
  }

  @Test
  void testRetrieveUser() throws Exception {
    // Retrieve a user by ID
    User user = usersApi.retrieve(BOT_ID);
    saveToFile(user, "user-retrieve-rs.json");

    // Validate the retrieved user
    assertNotNull(user);
    assertEquals(USER_ID, user.getId());
    assertEquals("user", user.getObject());
  }

  @Test
  void testRetrieveBot() throws Exception {
    // Retrieve a user by ID
    User user = usersApi.me();
    saveToFile(user, "user-retrieve-me-rs.json");

    // Validate the retrieved user
    assertNotNull(user);
    assertEquals(USER_ID, user.getId());
    assertEquals("user", user.getObject());
  }
}
