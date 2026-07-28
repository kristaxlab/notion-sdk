package integration.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import integration.BaseIntegrationTest;
import integration.NotionTstPageLogExtension;
import integration.extension.NotionTestPage;
import integration.helper.IntegrationTestAssisstant;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IT-40: Pages - Lock and unlock a page")
public class Pages_IT40_Lock extends BaseIntegrationTest {

  @NotionTestPage
  private static String testPageId;

  @BeforeAll
  public static void setup() {
    NotionTstPageLogExtension.register(Pages_IT40_Lock.class, testPageId);
  }

  @Test
  @DisplayName("IT-40: Pages - Lock and unlock a page")
  public void testLockAndUnlockPage() {
    Page created =
        getNotionClient().pages().create(page -> page.inPage(testPageId).title("Lockable"));

    Page locked =
        getNotionClient()
            .pages()
            .update(created.getId(), UpdatePageParams.builder().locked(true).build());
    assertEquals(Boolean.TRUE, locked.getIsLocked());

    Page unlocked =
        getNotionClient()
            .pages()
            .update(created.getId(), UpdatePageParams.builder().locked(false).build());
    assertEquals(Boolean.FALSE, unlocked.getIsLocked());
  }

  @AfterAll
  public static void tearDown() {}
}
