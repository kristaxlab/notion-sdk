package integration;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.model.users.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UsersIntTests {

  private static NotionApiClient NOTION;

  @BeforeAll
  public static void initClient() {
    NOTION = NotionClientProvider.internalTestingClient();
  }

  /** INT-1: Standalone pages: create empty page nested into another */
  @Test
  public void testGetUser() {
    User user = NOTION.users().me();

    assertNotNull(user);
    assertNotNull(user.getRequestId());
    assertNotNull(user.getBot());
    assertNotNull(user.getBot().getOwner());
    assertNotNull(user.getBot().getOwner().getWorkspace());
  }
}
