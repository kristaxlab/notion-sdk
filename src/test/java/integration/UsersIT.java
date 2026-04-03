package integration;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.BaseIntegrationTest;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.model.users.UserList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UsersIT extends BaseIntegrationTest {

  @Test
  @DisplayName("[INT-25]: Users - Retrieve me and then retrieve this same user (me) by id")
  public void testGetUser() {
    User me = getNotion().users().me();

    assertNotNull(me);
    assertNull(me.getPerson());
    assertNotNull(me.getBot());
    assertNotNull(me.getBot().getOwner());

    User userById = getNotion().users().retrieve(me.getId());

    assertNotNull(userById);
    assertEquals(me.getId(), userById.getId());
    assertEquals(me.getName(), userById.getName());
    assertEquals(me.getAvatarUrl(), userById.getAvatarUrl());
    assertEquals(me.getType(), userById.getType());
    assertNotNull(userById.getBot());
    assertNotNull(userById.getBot().getOwner());
    assertEquals(me.getBot().getOwner().getType(), userById.getBot().getOwner().getType());
    assertEquals(me.getBot().getWorkspaceId(), userById.getBot().getWorkspaceId());
    assertEquals(me.getBot().getWorkspaceName(), userById.getBot().getWorkspaceName());
    assertEquals(me.getBot().getWorkspaceLimits(), userById.getBot().getWorkspaceLimits());
  }

  @Test
  @DisplayName("[INT-32]: Users - Retrieve list of all users")
  public void testListUsers() {
    UserList usersList = getNotion().users().listUsers();

    assertNotNull(usersList);
    assertFalse(usersList.getResults().isEmpty());
  }
}
