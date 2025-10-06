package io.kristixlab.notion.api.model.users;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import org.junit.jupiter.api.Test;

public class UserDeserializationTest extends BaseTest {

  @Test
  void testPersonUser() throws Exception {
    User personUser = loadFromFile("users/users-retrieve-rs.json", User.class);

    assertEquals("user", personUser.getObject());
    assertEquals("44444444-4444-43b3-8288-444444444444", personUser.getId());
    assertEquals("userName", personUser.getName());
    assertEquals("https://s3-us-west-2.amazonaws.com/....png", personUser.getAvatarUrl());
    assertEquals("person", personUser.getType());
    assertEquals("14444444-4444-43b3-8288-444444444444", personUser.getRequestId());
    assertNotNull(personUser.getPerson());
    assertEquals("email@gmail.com", personUser.getPerson().getEmail());
    assertNull(personUser.getBot());
  }

  @Test
  void testBotUserBasicFields() throws Exception {
    User botUser = loadFromFile("users/users-retrieve-me-rs.json", User.class);

    assertNotNull(botUser);
    assertEquals("user", botUser.getObject());
    assertEquals("44444444-4444-43b3-8288-444444444444", botUser.getId());
    assertEquals("Integration", botUser.getName());
    assertEquals("bot", botUser.getType());
    assertEquals("14444444-4444-43b3-8288-444444444444", botUser.getRequestId());
    assertNull(botUser.getAvatarUrl()); // Bot user in example doesn't have avatar
    assertNotNull(botUser.getBot());
    assertNull(botUser.getPerson()); // Bot user should not have person object

    // Test bot properties
    Bot bot = botUser.getBot();
    assertEquals("Workspace Name", bot.getWorkspaceName());

    // Test owner
    assertNotNull(bot.getOwner());
    assertEquals("workspace", bot.getOwner().getType());
    assertTrue(bot.getOwner().getWorkspace());
    assertNull(bot.getOwner().getUser()); // Workspace-owned bot

    // Test workspace limits
    assertNotNull(bot.getWorkspaceLimits());
    assertEquals(5368709120L, bot.getWorkspaceLimits().getMaxFileUploadSizeInBytes());
  }

  @Test
  void testUsersListBasicFields() throws Exception {
    UserList userList = loadFromFile("users/users-list-rs.json", UserList.class);

    assertEquals("list", userList.getObject());
    assertEquals("14444444-4444-43b3-8288-444444444444", userList.getRequestId());
    assertFalse(userList.hasMore());
    assertEquals("user", userList.getType());
    assertNotNull(userList.getUser()); // Empty user object in list response
    assertNotNull(userList.getResults());
    assertEquals(3, userList.getResults().size());

    // Test first user (person)
    User firstUser = userList.getResults().get(0);
    assertEquals("user", firstUser.getObject());
    assertEquals("44444444-4444-43b3-8288-444444444444", firstUser.getId());
    assertEquals("userName", firstUser.getName());
    assertEquals("person", firstUser.getType());
    assertNotNull(firstUser.getPerson());
    assertEquals("email@gmail.com", firstUser.getPerson().getEmail());
    assertNull(firstUser.getBot());

    // Test second user (bot with full info)
    User secondUser = userList.getResults().get(1);
    assertEquals("44444444-4444-43b3-8288-444444444422", secondUser.getId());
    assertEquals("Integration", secondUser.getName());
    assertEquals("bot", secondUser.getType());
    assertNotNull(secondUser.getBot());
    assertEquals("Workspace Name", secondUser.getBot().getWorkspaceName());
    assertNull(secondUser.getPerson());

    // Test third user (bot with empty bot object)
    User thirdUser = userList.getResults().get(2);
    assertEquals("44444444-4444-43b3-8288-444444444433", thirdUser.getId());
    assertEquals("Notion MCP (Beta)", thirdUser.getName());
    assertEquals("bot", thirdUser.getType());
    assertNotNull(thirdUser.getBot());
    assertNull(thirdUser.getBot().getWorkspaceName());
    assertNull(thirdUser.getBot().getOwner());
    assertNull(thirdUser.getBot().getWorkspaceLimits());
    assertNull(thirdUser.getPerson());
  }
}
