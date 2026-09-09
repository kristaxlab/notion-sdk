package tests.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.kristaxlab.notion.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;

public class IT1_Users_RetrieveMe extends BaseIntegrationTest {

  @Test
  @DisplayName("IT-1: Users - Retrieve me, then retrieve me by id")
  public void testRetrieveMeAndThenById() {
    // 1. Retrieve the user behind the token the client authenticates with
    User me = getNotionClient().users().me();

    assertNotNull(me);
    assertEquals("user", me.getObject());
    assertNotNull(me.getId());
    assertNotNull(me.getName());
    // an integration token always resolves to a bot, never to a person
    assertEquals("bot", me.getType());
    assertNull(me.getPerson(), "A bot user carries no person details");
    assertNotNull(me.getBot());
    assertNotNull(me.getBot().getOwner());
    assertNotNull(me.getBot().getOwner().getType());
    assertNotNull(me.getBot().getWorkspaceId());
    assertNotNull(me.getBot().getWorkspaceName());
    assertNotNull(me.getBot().getWorkspaceLimits());

    // 2. Retrieve the very same user by its id - both endpoints describe it identically
    User byId = getNotionClient().users().retrieve(me.getId());

    assertNotNull(byId);
    assertEquals(me.getId(), byId.getId());
    assertEquals(me.getObject(), byId.getObject());
    assertEquals(me.getName(), byId.getName());
    assertEquals(me.getAvatarUrl(), byId.getAvatarUrl());
    assertEquals(me.getType(), byId.getType());
    assertNull(byId.getPerson());
    assertNotNull(byId.getBot());
    assertEquals(me.getBot().getOwner().getType(), byId.getBot().getOwner().getType());
    assertEquals(me.getBot().getOwner().getWorkspace(), byId.getBot().getOwner().getWorkspace());
    assertEquals(me.getBot().getWorkspaceId(), byId.getBot().getWorkspaceId());
    assertEquals(me.getBot().getWorkspaceName(), byId.getBot().getWorkspaceName());
    assertEquals(
        me.getBot().getWorkspaceLimits().getMaxFileUploadSizeInBytes(),
        byId.getBot().getWorkspaceLimits().getMaxFileUploadSizeInBytes());
  }
}
