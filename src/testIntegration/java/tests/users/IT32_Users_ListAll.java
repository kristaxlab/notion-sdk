package tests.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.kristaxlab.notion.model.user.User;
import io.kristaxlab.notion.model.user.UserList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;
import testkit.ext.TestSession;

public class IT32_Users_ListAll extends BaseIntegrationTest {

  @Test
  @DisplayName("IT-32: Users - Retrieve all users list")
  public void testListAllUsers() {
    // the integration running the tests is a common entity resolved once per test session
    String testBotUserId = TestSession.get().getBotUserId();

    UserList users = getNotionClient().users().listUsers();

    assertNotNull(users);
    assertEquals("list", users.getObject());
    assertEquals("user", users.getType());
    assertNotNull(users.getResults());
    assertFalse(users.getResults().isEmpty(), "A workspace always has at least one member");

    if (Boolean.TRUE.equals(users.getHasMore())) {
      assertNotNull(users.getNextCursor());
    } else {
      assertNull(users.getNextCursor());
    }

    // every entry is either a person or a bot, and only carries the details of its own kind
    for (User user : users.getResults()) {
      assertEquals("user", user.getObject());
      assertNotNull(user.getId());
      assertNotNull(user.getName());

      if ("person".equals(user.getType())) {
        assertNotNull(user.getPerson());
        assertNull(user.getBot());
      } else {
        assertEquals("bot", user.getType());
        assertNotNull(user.getBot());
        assertNull(user.getPerson());
      }
    }

    User testBot =
        users.getResults().stream()
            .filter(user -> testBotUserId.equals(user.getId()))
            .findFirst()
            .orElse(null);

    assertNotNull(
        testBot, "The integration running the tests should be listed as a workspace user");
    assertEquals("bot", testBot.getType());
    // unlike the other bots, the calling one sees the workspace it is installed in
    assertNotNull(testBot.getBot().getWorkspaceId());
    assertNotNull(testBot.getBot().getWorkspaceName());
  }
}
